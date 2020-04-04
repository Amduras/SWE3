package controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import model.Galaxy;
import model.Solarsystem;
import model.User;
import planets.Planets_General;

@ManagedBean(name="galaxyHandler")
@SessionScoped
public class GalaxyHandler {

	private EntityManager em;
	private UserTransaction utx;

	private int galaxyForTable;
	private int systemForTable;
	private List<Planets_General> planets = new ArrayList<>();
	private User user = null;

	public GalaxyHandler() {

	}

	public GalaxyHandler(EntityManager em, UserTransaction utx) {
		this.em = em;
		this.utx = utx;
	}
	
	@SuppressWarnings("unchecked")
	public Solarsystem getFreeSystem(int galaxyId) {
		Query query = em.createQuery("select k from Solarsystem k where k.galaxyId = :galaxyId");
		query.setParameter("galaxyId", galaxyId);
		List<Solarsystem> systems = query.getResultList();
		for(int i = 0; i < systems.size(); ++i) {
			Solarsystem system = systems.get(i);
			if(system.getPlanets() > 0) {
				if(getFreePosition(galaxyId, system.getSystemId(), true) != 0) {
					system.setFreeStartpositions(system.getFreeStartpositions()-1);
					system.setPlanets(system.getPlanets()-1);
					try {
						utx.begin();
					} catch (NotSupportedException | SystemException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					em.merge(system);
					try {
						utx.commit();
					} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
							| HeuristicRollbackException | SystemException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return system;
				}
			}
		}
		Solarsystem system = new Solarsystem(galaxyId);
		query = em.createQuery("select k from Galaxy k where k.galaxyId = :galaxyId");
		query.setParameter("galaxyId", galaxyId);
		Galaxy galaxy = (Galaxy) query.getSingleResult();
		galaxy.setMaxSystems(galaxy.getMaxSystems()-1);
		try {
			utx.begin();
		} catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		em.merge(galaxy);
		em.persist(system);
		try {
			utx.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return system;
	}


	public int getFreePosition(int galaxyId, int systemId, boolean start) {
		if(start) {
			for(int i = 4; i <= 12; ++i) {
				Query query = em.createQuery("select k from Planets_General k where k.galaxy = :galaxyId"
						+ " and k.solarsystem = :systemId"
						+ " and k.position = :position");
				query.setParameter("galaxyId", galaxyId);
				query.setParameter("systemId", systemId);
				query.setParameter("position", i);
				try {
					Object res = query.getSingleResult();
				}catch (NoResultException e) {
					return i;
				}
			}
		} else {
			for(int i = 0; i < 16; ++i) {
				Query query = em.createQuery("select k from Planets_General k where k.galaxy = :galaxyId"
						+ " and k.solarsystem = :systemId"
						+ " and k.position = :position");
				query.setParameter("galaxyId", galaxyId);
				query.setParameter("systemId", systemId);
				query.setParameter("position", i);
				try {
					Object res = query.getSingleResult();
				}catch (NoResultException e) {
					return i;
				}
			}
		}

		return 0;
	}

	public void changeGalaxy(boolean left) {
		if(left) {
			int min = getMinGalaxy();
			if(getGalaxyForTable() - 1 < min) {
				setGalaxyForTable(min);
			} else {
				setGalaxyForTable(getGalaxyForTable()-1);
			}
		}else {
			int max = getMaxGalaxy();
			if(getGalaxyForTable() + 1 > max) {
				setGalaxyForTable(max);
			} else {
				setGalaxyForTable(getGalaxyForTable()+1);
			}
		}
	}

	private int getMinGalaxy() {
		Query query = em.createQuery("select min(galaxyId) from Galaxy k");
		int min = (int) query.getSingleResult();
		return min;
	}

	private int getMaxGalaxy() {
		Query query = em.createQuery("select max(galaxyId) from Galaxy k");
		int max = (int) query.getSingleResult();
		return max;
	}

	public void changeSystem(boolean left) {
		if(left) {
			int min = getMinSystem();
			if(getSystemForTable() - 1 < min) {
				setSystemForTable(min);
			} else {
				setSystemForTable(getSystemForTable()-1);
			}
		}else {
			int max = getMaxSystem();
			if(getSystemForTable() + 1 > max) {
				setSystemForTable(max);
			} else {
				setSystemForTable(getSystemForTable()+1);
			}
		}
	}

	private int getMinSystem() {
		Query query = em.createQuery("select min(systemId) from Solarsystem k");
		int min = (int) query.getSingleResult();
		return min;
	}

	private int getMaxSystem() {
		Query query = em.createQuery("select max(systemId) from Solarsystem k");
		int max = (int) query.getSingleResult();
		return max;
	}

	public int getGalaxyForTable() {
		return galaxyForTable;
	}

	public void setGalaxyForTable(int galaxyForTable) {
		this.galaxyForTable = galaxyForTable;
	}

	public int getSystemForTable() {
		return systemForTable;
	}

	public void setSystemForTable(int systemForTable) {
		this.systemForTable = systemForTable;
	}

	public boolean existPlanet(int i) {
		if(planets.get(i) != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean ownedBy(int i) {
		if(existPlanet(i)) {
			if(planets.get(i).getUserId() == user.getUserID()) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}
	
	public String getOwner(int i) {
		if(existPlanet(i)) {
			int userid = planets.get(i).getUserId();
			Query query = em.createQuery("select k from User k where k.userID = :id");
			query.setParameter("id", userid);
			User user = (User) query.getSingleResult();
			return user.getUsername();
		} else {
			return "";
		}
	}
	
	@SuppressWarnings("unchecked")
	public void createPlanetList() {
		Query query = em.createQuery("select k from Planets_General k where k.solarsystem = :system and k.galaxy = :galaxy");
		query.setParameter("system", getSystemForTable());
		query.setParameter("galaxy", getGalaxyForTable());
		planets = query.getResultList();
		List<Planets_General> tmpList = new ArrayList<>();
		for(int i = 0; i < 15; ++i) {
			tmpList.add(null);
		}
		if(planets.size() != 0) {
			for(int i = 0; i < planets.size(); ++i) {
				tmpList.set(planets.get(i).getPosition(), planets.get(i));
			}
			planets=tmpList;
		}
	}
	
	public void message(IncludeController includeController, MessageHandler messageHandler, int id) {
		messageHandler.setNewMessageUser(getOwner(id));
		messageHandler.setMessage(2, true);
		includeController.setPage("messageView");
	}
	
	public void colonize(PlanetHandler planetHandler, int userid, int rowid) {
		planetHandler.colonizePlanet(userid, rowid, getGalaxyForTable(), getSystemForTable());
		Query query = em.createQuery("select k from Solarsystem k where k.systemId = :id");
		query.setParameter("id", getSystemForTable());
		Solarsystem system = (Solarsystem) query.getSingleResult();
		system.setPlanets(system.getPlanets()-1);
		if(rowid >=3 || rowid <= 12) {
			system.setFreeStartpositions(system.getFreeStartpositions()-1);
		}
		
		try {
			utx.begin();
		} catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		em.merge(system);
		try {
			utx.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		planetHandler.createPlanetlist();
	}

	public void spy(int position) {

	}

	public void attack(int position) {
		
	}

	public List<Planets_General> getPlanets() {
		createPlanetList();
		return planets;
	}

	public void setPlanets(List<Planets_General> planets) {
		this.planets = planets;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}

