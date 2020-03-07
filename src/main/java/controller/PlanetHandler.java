package controller;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import Task.ResUpdateTask;
import planets.*;

@ManagedBean(name="planetHandler")
@SessionScoped
public class PlanetHandler {
	
	private List<Planets_General> planets;
	private EntityManager em;
	
	private int ownedPlanets;
	private int activePlanet = 0;
	
	private Planets_Buildings pb;
	private Planets_Def pd;
	private Planets_General pg;
	private Planets_Research pr;
	private Planets_Ships ps;
	
	public PlanetHandler() {		
	}
	/*** Get Lists of owned planets and init active ***/
	public void init(List<Planets_General> planets, EntityManager em) {
		this.planets = planets;
		this.em = em;
		this.setOwnedPlanets(planets.size());
		
		updateDataset();	
	}

	public void changePlanet(int ind) {
		/** save existing dataset ? **/
		
		activePlanet = ind;
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
	private void updateBuildings() {
		Query query = em.createQuery("select k from PlanetsBuildings k where k.planetId = :id");
		query.setParameter("id", pg.getPlanetId());
		@SuppressWarnings("unchecked")
		Object res = query.getSingleResult();
		if(res == null)
			System.err.println("Planets_Buildings with id="+pg.getPlanetId()+"was not found.");
		else
			pb = (Planets_Buildings)res;
	}
	private void updateDef() {
		Query query = em.createQuery("select k from Planets_Def k where k.planetId = :id");
		query.setParameter("id", pg.getPlanetId());
		@SuppressWarnings("unchecked")
		Object res = query.getSingleResult();
		if(res == null)
			System.err.println("Planets_Buildings with id="+pg.getPlanetId()+"was not found.");
		else
			pd = (Planets_Def)res;
	}
	private void updateResearch() {
		Query query = em.createQuery("select k from Planets_Research k where k.planetId = :id");
		query.setParameter("id", pg.getPlanetId());
		@SuppressWarnings("unchecked")
		Object res = query.getSingleResult();
		if(res == null)
			System.err.println("Planets_Buildings with id="+pg.getPlanetId()+"was not found.");
		else
			pr = (Planets_Research)res;
	}
	private void updateShips() {
		Query query = em.createQuery("select k from Planets_Ships k where k.planetId = :id");
		query.setParameter("id", pg.getPlanetId());
		@SuppressWarnings("unchecked")
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