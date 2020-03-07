package controller;

import java.util.List;

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
	
	public PlanetHandler() {
		
	}
	
	public PlanetHandler(EntityManager em, UserTransaction utx) {
		this.em = em;
		this.utx = utx;
	}
	/*** Get Lists of owned planets and init active ***/
	public void init(List<Planets_General> planets) {
		this.planets = planets;
		this.setOwnedPlanets(planets.size());
		updateDataset();	
	}
	
	public void createNewPlanet(int userID) {
		Planets_General pgt = new Planets_General( 0, 0, 0, null, 0, 0, 0, 193, 0, 500, 200, 0, 0, 0, "Heimatplanet",userID);
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
		Planets_Research prt = new Planets_Research(pgt.getPlanetId(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
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
		updateDataset();
	}

	public void updateRes() {
		System.out.println(pg);
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
	private void updateBuildings() {
		Query query = em.createQuery("select k from Planets_Buildings k where k.planetId = :id");
		query.setParameter("id", pg.getPlanetId());
		Object res = query.getSingleResult();
		if(res == null)
			System.err.println("Planets_Buildings with id="+pg.getPlanetId()+"was not found.");
		else
			pb = (Planets_Buildings)res;
	}
	private void updateDef() {
		Query query = em.createQuery("select k from Planets_Def k where k.planetId = :id");
		query.setParameter("id", pg.getPlanetId());
		Object res = query.getSingleResult();
		if(res == null)
			System.err.println("Planets_Buildings with id="+pg.getPlanetId()+"was not found.");
		else
			pd = (Planets_Def)res;
	}
	private void updateResearch() {
		Query query = em.createQuery("select k from Planets_Research k where k.planetId = :id");
		query.setParameter("id", pg.getPlanetId());
		Object res = query.getSingleResult();
		if(res == null)
			System.err.println("Planets_Buildings with id="+pg.getPlanetId()+"was not found.");
		else
			pr = (Planets_Research)res;
	}
	private void updateShips() {
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