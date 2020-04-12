package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
	public Solarsystem getFreeSystem(Galaxy galaxy) {
		Solarsystem system = null;
		Query query = em.createQuery("select k from Solarsystem k where galaxyId = :galaxyID");
		query.setParameter("galaxyID", galaxy.getGalaxyId());
		List<Solarsystem> systems = query.getResultList();
		if(systems.size() > 0) {
			int i = 0;
			while(i < systems.size()) {
				system = systems.get(i);
				if(system.getFreeStartpositions() > 0) {
					system.setFreeStartpositions(system.getFreeStartpositions() - 1);
					system.setPlanets(system.getPlanets() - 1);
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
				++i;
			}
		} 
		system = new Solarsystem();
		int id = getMaxSystem();
		system.setSystemId(id+1);
		system.setGalaxyId(galaxy.getGalaxyId());
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

	public Galaxy getGalaxy() {
		Galaxy galaxy = null;
		Query query = em.createQuery("select k from Galaxy k");
		@SuppressWarnings("unchecked")
		List<Galaxy> galaxies = query.getResultList();
		if(galaxies.size() == 0) {
			galaxy = new Galaxy();
			galaxy.setGalaxyId(1);
			try {
				utx.begin();
			} catch (NotSupportedException | SystemException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			em.persist(galaxy);
			try {
				utx.commit();
			} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
					| HeuristicRollbackException | SystemException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		} else {
			ListIterator<Galaxy> iter = galaxies.listIterator();
			boolean found = false;
			while(iter.hasNext() && !found) {
				Galaxy next = iter.next();
				if(next.getMaxSystems() > 0) {
					galaxy = next;
					found = true;
				}
			}
			if(!found) {
				galaxy = new Galaxy();
				galaxy.setGalaxyId(getMaxGalaxy()+1);
				try {
					utx.begin();
				} catch (NotSupportedException | SystemException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				em.persist(galaxy);
				try {
					utx.commit();
				} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
						| HeuristicRollbackException | SystemException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
		}
		return galaxy;
	}

	public int getPosition(int galaxyId, int systemId) {
		boolean blocked = true;
		int position = 0;
		Random rand = new Random();
		while(blocked) {
			position = rand.nextInt(12 - 4 + 1) + 4;
			Query query = em.createQuery("select k from Planets_General k where k.galaxy = :galaxyId"
					+ " and k.solarsystem = :systemId"
					+ " and k.position = :position");
			query.setParameter("galaxyId", galaxyId);
			query.setParameter("systemId", systemId);
			query.setParameter("position", position);
			try {
				Object res = query.getSingleResult();
			}catch (NoResultException e) {
				blocked = false;
			}
		}
		return position;
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

	public void message(IncludeController includeController, MessageHandler messageHandler, int id, FleetHandler fleetHandler) {
		messageHandler.setNewMessageUser(getOwner(id));
		messageHandler.setMessage(2, true);
		includeController.setPage("messageView", fleetHandler);
	}
	
	public void colonize(PlanetHandler planetHandler, int userid, int rowid, FleetHandler fleetHandler) {
		if(planetHandler.getPs().getColonyShip() > 0) {
			fleetHandler.setUserid(userid);
			fleetHandler.kolo(getGalaxyForTable(), getSystemForTable(), rowid, true);
		} else {
			setMessage("Es wird ein Kolonieshiff benötigt.");
		}
	}
	
	private void setMessage(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		if(context != null) {
			context.addMessage(null, new FacesMessage(msg));
		} else {
			System.out.println("context: null");
		}
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
}

