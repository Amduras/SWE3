package controller;

import java.util.List;
import java.util.ListIterator;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import model.Galaxy;
import model.Solarsystem;
import planets.Planets_Buildings;
import planets.Planets_Def;
import planets.Planets_General;
import planets.Planets_Research;
import planets.Planets_Ships;

@ManagedBean(name="planetHandler")
@SessionScoped
public class PlanetHandler {

	private List<Planets_General> planets;

	private EntityManager em;

	private UserTransaction utx;

	private int ownedPlanets;
	private int activePlanet = 0;

	private Planets_Buildings pb;
	private Planets_Def pd;
	private Planets_General pg;
	private Planets_Research pr;
	private Planets_Ships ps;
	private GalaxyHandler galaxyHandler;

	public PlanetHandler() {

	}

	public PlanetHandler(EntityManager em, UserTransaction utx, GalaxyHandler galaxyHandler) {
		this.em = em;
		this.utx = utx;
		this.galaxyHandler = galaxyHandler;
	}

	/*** Get Lists of owned planets and init active ***/
	public void init(List<Planets_General> planets) {
		this.planets = planets;
		this.setOwnedPlanets(planets.size());
		updateDataset();	
	}

	public void colonizePlanet(int userid, int position, int galaxyid, int systemid) {
		Planets_General pgt = new Planets_General(galaxyid, systemid, position, null, 0, 0, 0, 193, 0, 500, 200, 0, 0, 0, "Kolonie",userid);
		try {
			utx.begin();
		} catch (NotSupportedException | SystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		em.persist(pgt);
		try {
			utx.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Planets_Buildings pbt = new Planets_Buildings(pgt.getPlanetId(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		Planets_Def pdt = new Planets_Def(pgt.getPlanetId(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		Planets_Research prt = new Planets_Research(pgt.getPlanetId(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		Planets_Ships pst = new Planets_Ships(pgt.getPlanetId(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		try {
			utx.begin();
		} catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		em.persist(pbt);
		em.persist(pdt);
		em.persist(prt);
		em.persist(pst);
		try {
			utx.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createNewPlanet(int userID) {
		Query query = em.createQuery("select k from Galaxy k");
		Galaxy galaxy = null;
		@SuppressWarnings("unchecked")
		List<Galaxy> galaxies = query.getResultList();
		if(galaxies.size() == 0) {
			galaxy = new Galaxy();
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
		}
		Solarsystem system;
		int position = 6;
		query = em.createQuery("select k from Solarsystem k");
		@SuppressWarnings("unchecked")
		List<Solarsystem> systems = query.getResultList();
		if(systems.size() == 0) {
			system = new Solarsystem(galaxy.getGalaxyId());
			query = em.createQuery("select k from Galaxy k where k.galaxyId = :galaxyId");
			query.setParameter("galaxyId", galaxy.getGalaxyId());
			galaxy.setMaxSystems(galaxy.getMaxSystems()-1);
			system.setPlanets(system.getPlanets()-1);
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
		} else {
			system = galaxyHandler.getFreeSystem(galaxy.getGalaxyId());
			position = galaxyHandler.getFreePosition(galaxy.getGalaxyId(), system.getSystemId(), true);
		}
		Planets_General pgt = new Planets_General( galaxy.getGalaxyId(), system.getSystemId(), position, null, 0, 0, 0, 193, 0, 500, 200, 0, 0, 0, "Heimatplanet",userID);
		try {
			utx.begin();
		} catch (NotSupportedException | SystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		em.persist(pgt);
		try {
			utx.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Planets_Buildings pbt = new Planets_Buildings(pgt.getPlanetId(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		Planets_Def pdt = new Planets_Def(pgt.getPlanetId(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		Planets_Research prt = new Planets_Research(pgt.getPlanetId(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		Planets_Ships pst = new Planets_Ships(pgt.getPlanetId(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		try {
			utx.begin();
		} catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		em.persist(pbt);
		em.persist(pdt);
		em.persist(prt);
		em.persist(pst);
		try {
			utx.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void changePlanet(int ind) {
		/** save existing dataset ? **/
		activePlanet = ind;
		try {
			utx.begin();
		} catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		em.merge(pg);
		try {
			utx.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateDataset();
	}


	public void updateRes() {
		int m = pg.getMetal();
		int c = pg.getCrystal();
		int d = pg.getDeut();
		pg.setMetal(m+1000);
		pg.setCrystal(c+663);
		pg.setDeut(d+22);
	}

	public Planets_Buildings getPb() {
		return pb;
	}

	public void setPb(Planets_Buildings pb) {
		this.pb = pb;
	}

	public Planets_Def getPd() {
		return pd;
	}

	public void setPd(Planets_Def pd) {
		this.pd = pd;
	}

	public Planets_General getPg() {
		return pg;
	}

	public void setPg(Planets_General pg) {
		this.pg = pg;
	}

	public Planets_Research getPr() {
		return pr;
	}

	public void setPr(Planets_Research pr) {
		this.pr = pr;
	}

	public Planets_Ships getPs() {
		return ps;
	}

	public void setPs(Planets_Ships ps) {
		this.ps = ps;
	}
	public int getOwnedPlanets() {
		return ownedPlanets;
	}
	public void setOwnedPlanets(int ownedPlanets) {
		this.ownedPlanets = ownedPlanets;
	}

	public int getActivePlanet() {
		return activePlanet;
	}

	public void setActivePlanet(int activePlanet) {
		this.activePlanet = activePlanet;
	}

	public int getPlanetsSize() {
		return planets.size();
	}

	public List<Planets_General> getPlanetlist() {
		createPlanetlist();
		return planets;
	}

	@SuppressWarnings("unchecked")
	public void createPlanetlist() {
		int id = planets.get(0).getUserId();
		Query query = em.createQuery("select k from Planets_General k where k.userid = :id");
		query.setParameter("id", id);
		planets = query.getResultList();
	}

	public void changePlanet(boolean left, BuildHandler buildHandler){
		if(planets.size() > 1) {
			if(left) {
				if(activePlanet - 1 < 0) {
					changePlanet(planets.size()-1);
				} else {
					changePlanet(--activePlanet);
				}
			} else {
				if(activePlanet + 1 > planets.size()-1) {
					changePlanet(0);
				} else {
					changePlanet(++activePlanet);
				}
			}
			buildHandler.setNewPage("true", "default");
		}
	}

	public void updateBuildings() {
		Query query = em.createQuery("select k from Planets_Buildings k where k.planetId = :id");
		query.setParameter("id", pg.getPlanetId());
		Object res = query.getSingleResult();
		if(res == null)
			System.err.println("Planets_Buildings with id="+pg.getPlanetId()+"was not found.");
		else
			pb = (Planets_Buildings)res;
	}
	public void updateDef() {
		Query query = em.createQuery("select k from Planets_Def k where k.planetId = :id");
		query.setParameter("id", pg.getPlanetId());
		Object res = query.getSingleResult();
		if(res == null)
			System.err.println("Planets_Buildings with id="+pg.getPlanetId()+"was not found.");
		else
			pd = (Planets_Def)res;
	}
	public void updateResearch() {
		Query query = em.createQuery("select k from Planets_Research k where k.planetId = :id");
		query.setParameter("id", pg.getPlanetId());
		Object res = query.getSingleResult();
		if(res == null)
			System.err.println("Planets_Buildings with id="+pg.getPlanetId()+"was not found.");
		else
			pr = (Planets_Research)res;
	}
	public void updateShips() {
		Query query = em.createQuery("select k from Planets_Ships k where k.planetId = :id");
		query.setParameter("id", pg.getPlanetId());
		Object res = query.getSingleResult();
		if(res == null)
			System.err.println("Planets_Buildings with id="+pg.getPlanetId()+"was not found.");
		else
			ps = (Planets_Ships)res;
	}
	private void updateDataset() {
		pg = planets.get(activePlanet);
		
		updateBuildings();
		updateDef();
		updateResearch();
		updateShips();	
	}
}