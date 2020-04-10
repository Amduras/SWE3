package controller;

import java.util.Date;
import java.util.List;
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
import model.WorldSettings;
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
	private double maxEnergy;

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
		resDiff();
	}

	public void colonizePlanet(int userid, int position, int galaxyid, int systemid) {
		Planets_General pgt = new Planets_General(galaxyid, systemid, position, null, 0, 0, 0, 193, 0, genTemp(position), 200, 0, 0, 0, 0, "Kolonie",userid);
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
	
	private int genTemp(int min, int max) {
		Random rand = new Random();
		return rand.nextInt(max - min + 1) + min;
	}
	
	private int genTemp(int position) {
		switch(position) {
		case 0:
			return genTemp(220,260);
		case 1:
			return genTemp(170,210);
		case 2:
			return genTemp(120,160);
		case 3:
			return genTemp(70,110);
		case 4:
			return genTemp(60,100);
		case 5:
			return genTemp(50,90);
		case 6:
			return genTemp(40,80);
		case 7:
			return genTemp(30,70);
		case 8:
			return genTemp(20,60);
		case 9:
			return genTemp(10,50);
		case 10:
			return genTemp(0,40);
		case 11:
			return genTemp(-10,300);
		case 12:
			return genTemp(-50,-10);
		case 13:
			return genTemp(-90,-50);
		case 14:
			return genTemp(-130,-90);
		default:
			return 50;
			
		}
	}
	
	public void createNewPlanet(int userID) {
		Galaxy galaxy = galaxyHandler.getGalaxy();
		Solarsystem system = galaxyHandler.getFreeSystem(galaxy);
		int position = galaxyHandler.getPosition(galaxy.getGalaxyId(), system.getSystemId());
		Planets_General pgt = new Planets_General( galaxy.getGalaxyId(), system.getSystemId(), position, null, 0, 0, 0, 193, 0, genTemp(position), 200, 200, 0, 0, 0, "Heimatplanet",userID);
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
		Planets_Ships pst = new Planets_Ships(pgt.getPlanetId(), 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
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


	public void updateRes(long seconds) {
		Query query = em.createQuery("select max(id) from WorldSettings k");
		int id =  (int)query.getSingleResult();
		query = em.createQuery("select k from WorldSettings k where k.id = :id");
		query.setParameter("id", id);
		int geologist = 1;
		int engineer = 1;
		float workload = 1f;
		int item = 1;
		try {
			Object res = query.getSingleResult();
			WorldSettings ws = (WorldSettings)res;
		
			double m =  pg.getMetal();
			double c = pg.getCrystal();
			double d = pg.getDeut();			 
			m += seconds * ((30 * pb.getMetalMine() * Math.pow(1.1, pb.getMetalMine()) * geologist * workload * item + 120) * ((100 + 1 * pr.getPlasma()) / 100) * ws.getGameSpeed())/3600;
			c += seconds * ((20 * pb.getCrystalMine() * Math.pow(1.1, pb.getCrystalMine()) * geologist * workload * item + 60) * ((100 + 0.66 * pr.getPlasma()) / 100) * ws.getGameSpeed())/3600;
			d += seconds * ((10 * pb.getDeutSyn() * Math.pow(1.1, pb.getDeutSyn()) * (1.44 - 0.004 * pg.getTemperature()) * geologist * workload) * ((100 + 0.33 * pr.getPlasma()) / 100) * ws.getGameSpeed() - Math.ceil(10 * pb.getFusionReactor() * Math.pow(1.1, pb.getFusionReactor()) * ws.getGameSpeed()))/3600;
			pg.setMetal(Math.min(m,Math.round(2.5 * Math.pow(Math.E,(20 * pb.getMetalStorage() / 33)) * 5000)));
			pg.setCrystal(Math.min(c,Math.round(2.5 * Math.pow(Math.E,(20 * pb.getCrystalStorage() / 33)) * 5000)));
			pg.setDeut(Math.min(d,Math.round(2.5 * Math.pow(Math.E,(20 * pb.getDeutTank() / 33)) * 5000)));
			
			int mE = (int) Math.round(Math.ceil(20 * pb.getSolarPlant() * Math.pow(1.1, pb.getSolarPlant()) * engineer) +
									 Math.ceil(30 * pb.getFusionReactor() * Math.pow((1.05 + pr.getEnergy() * 0.01), pb.getFusionReactor()) * engineer) +
									 Math.ceil((pg.getTemperature() + 140) / 6) * ps.getSolarSattlelite() * engineer);
			
			pg.setMaxEnergy(mE);
		} catch(NoResultException e){	
			System.out.println("Keine WS in DB");
		}		
	}
	
	private void resDiff() {
		long diff = (System.currentTimeMillis() - pg.getLastUpdate().getTime()) / 1000;
		updateRes(diff);
		pg.setLastUpdate(new Date(System.currentTimeMillis()));
		
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
	}
	@SuppressWarnings("unchecked")
	public void createPlanetlist() {
		int id = planets.get(0).getUserId();
		Query query = em.createQuery("select k from Planets_General k where k.userid = :id");
		query.setParameter("id", id);
		planets = query.getResultList();
	}
	//TODO MISSING ARG??
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
	public void updateGeneral() {
		Query query = em.createQuery("select k from Planets_General k where k.planetId = :id");
		query.setParameter("id", pg.getPlanetId());
		try {
			pg = (Planets_General)query.getSingleResult();
		} catch(NoResultException e) {
			System.err.println("Planets_General with id="+pg.getPlanetId()+"was not found.");
		}		
	}
	
	public void updateBuildings() {
		Query query = em.createQuery("select k from Planets_Buildings k where k.planetId = :id");
		query.setParameter("id", pg.getPlanetId());
		try {
			pb = (Planets_Buildings)query.getSingleResult();
		} catch(NoResultException e) {
			System.err.println("Planets_Buildings with id="+pg.getPlanetId()+"was not found.");
		}
	}
	
	public void updateDef() {
		Query query = em.createQuery("select k from Planets_Def k where k.planetId = :id");
		query.setParameter("id", pg.getPlanetId());
		try {
			pd = (Planets_Def)query.getSingleResult();
		} catch(NoResultException e) {
			System.err.println("Planets_Buildings with id="+pg.getPlanetId()+"was not found.");
		}
	}
	
	public void updateResearch() {
		Query query = em.createQuery("select k from Planets_Research k where k.planetId = :id");
		query.setParameter("id", pg.getPlanetId());
		try {
			pr = (Planets_Research)query.getSingleResult();
		} catch(NoResultException e) {
			System.err.println("Planets_Buildings with id="+pg.getPlanetId()+"was not found.");
		}
	}
	
	public void updateShips() {
		Query query = em.createQuery("select k from Planets_Ships k where k.planetId = :id");
		query.setParameter("id", pg.getPlanetId());
		try {
			ps = (Planets_Ships)query.getSingleResult();
		} catch(NoResultException e) {
			System.err.println("Planets_Buildings with id="+pg.getPlanetId()+"was not found.");
		}
	}
	public void updateDataset() {
		pg = planets.get(activePlanet);
		updateGeneral();
		updateBuildings();
		updateDef();
		updateResearch();
		updateShips();	
		resDiff();
		
	}
	
	public void save() {
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
		try {
			utx.begin();
		} catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		em.merge(pb);
		em.merge(pr);
		em.merge(ps);
		em.merge(pd);
		try {
			utx.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	public double getStorageMAsP() {
		return pg.getMetal()/Math.round(2.5 * Math.pow(Math.E,(20 * pb.getMetalStorage() / 33)) * 5000);
	}
	
	public double getStorageCAsP() {
		return pg.getCrystal()/Math.round(2.5 * Math.pow(Math.E,(20 * pb.getCrystalStorage() / 33)) * 5000);
	}
	
	public double getStorageDAsP() {
		return pg.getDeut()/Math.round(2.5 * Math.pow(Math.E,(20 * pb.getDeutTank() / 33)) * 5000);
	}
	
	public int getMaxEnergyAsInt() {
		return (int)maxEnergy;
	}

	public GalaxyHandler getGalaxyHandler() {
		return galaxyHandler;
	}

	public void setGalaxyHandler(GalaxyHandler galaxyHandler) {
		this.galaxyHandler = galaxyHandler;
	}
}