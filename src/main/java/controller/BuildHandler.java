package controller;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import model.Buildable;
import model.User;

@ManagedBean(name="buildHandler")
@SessionScoped
public class BuildHandler {

	private PlanetHandler planetHandler;
	private EntityManager em;
	
	private String name;
	private int lvl;	

	private long metal;
	private long crystal;
	private long deut;
	private long energy;
	private long time;
	private String descr;
	private String rec;
	
	public BuildHandler(PlanetHandler p, EntityManager em) {
		this.planetHandler = p;
		this.em = em;
		name = "alles";
		lvl = 0;
		metal = 0;
		crystal = 0;
		deut = 0;
		energy = 0;
		time = 0;
		descr = "alles";
		rec = "alles";
	}
	
	public void setActive(int id){
		Query query = em.createQuery("select k from Buildable k where k.id = :id");
		query.setParameter("id", id+1);

		try {
			Object res = query.getSingleResult();
			
				Buildable b = (Buildable)res;
				name = b.getName();
				lvl = idToLvl(id);
				metal = (long)(b.getBaseCostMetal() * Math.pow(b.getResFactor(), lvl));
				crystal = (long)(b.getBaseCostCrystal() * Math.pow(b.getResFactor(), lvl));
				deut = (long)(b.getBaseCostDeut() * Math.pow(b.getResFactor(), lvl));
				if(id == 3) {
					energy = (long) Math.ceil((b.getBaseCostEnergy() * lvl * Math.pow(b.getResFactor(), lvl)));
				}
				else if(id == 4) {
					energy = (long) Math.ceil((b.getBaseCostEnergy() * lvl * Math.pow(b.getResFactor(), lvl)));
				}
				else {
					energy = (long) Math.ceil((b.getBaseCostEnergy() * lvl * Math.pow(b.getResFactor(), lvl)));
				}
				if(b.getType() == 0) {
					time = Math.round(((crystal + metal) / (2500 * (1 + planetHandler.getPb().getRoboticFactory()))) * Math.pow(0.5, planetHandler.getPb().getNaniteFactory()));
				}
				descr = b.getDescr();
				rec = b.getRec();
		} catch(NoResultException e){	
			System.out.println("KEine werte in DB");
		}
	}
		/*	
		name = idToName(id);
		lvl = idToLvl(id);
		metal = getMetalCost(id);
		crystal = getCrystalCost(id);
		deut = getDeutCost(id);
		energy = getEnergyCost(id);
		time = Math.round(((crystal + metal) / (2500 * (1 + planetHandler.getPb().getRoboticFactory()))) * Math.pow(0.5, planetHandler.getPb().getNaniteFactory()));
		descr = idToDescr(id);
		rec = "alles";*/
	/***
	 * 00	mm
	 * 01	cm
	 * 02	ds
	 * 03	sp
	 * 04	fr
	 * 05	ms
	 * 06	cs
	 * 07	ds
	 * 08	rf
	 * 09	warft
	 * 10	lab
	 * 11	depot
	 * 12	silo
	 * 13 	nf
	 * 14	tf
	 * 15	etech
	 * 16	lasertech
	 * 17	impulstech
	 * 18	hypertech
	 * 19	plasmatech
	 * 20	verbrennungstriebwerke
	 * 21	impulstrieb
	 * 22	hyperantrieb
	 * 23	spiotech
	 * 24	computertech
	 * 25	astrophysik
	 * 26	gravi
	 * 27	waffentech
	 * 28	schildtech
	 * 29	hulltech
	 * 30	lj
	 * 31	sj
	 * 32	kreuzer
	 * 33	ss
	 * 34	sk
	 * 35	bomber
	 * 36	zerstörer
	 * 37	ts
	 * 38	kt
	 * 39	gt
	 * 40	kolo
	 * 41	recycler
	 * 42	spiosonde
	 * 43	solarsat
	 * 44	rak
	 * 45	llaser
	 * 46	slaser
	 * 47	gauss
	 * 48	iokanone
	 * 49	plasmawaerfer
	 * 50	kschild
	 * 51	gschild
	 * 52	abfangrak
	 * 53	interplanetarrak
	 */
	

	
	private long getMetalCost(int id){
		long res = 0;
		switch(id) {
		case 0:
			res = (long)(40 * Math.pow(1.5, planetHandler.getPb().getMetalMine()));
			break;
		case 1:
			res = (long)(30 * Math.pow(1.6, planetHandler.getPb().getCrystalMine()));
			break;
		case 2:
			res = (long)(15 * Math.pow(1.5, planetHandler.getPb().getDeutSyn()));
			break;
		case 3:
			res = (long)(50 * Math.pow(1.5, planetHandler.getPb().getSolarPlant()));
			break;
		case 4:
			res = (long)(500 * Math.pow(1.8, planetHandler.getPb().getFusionReactor()));
			break;
		case 5:
			res = (long)(500 * Math.pow(2, planetHandler.getPb().getMetalStorage()));
			break;
		case 6:
			res = (long)(500 * Math.pow(2, planetHandler.getPb().getCrystalStorage()));
			break;
		case 7:
			res = (long)(500 * Math.pow(2, planetHandler.getPb().getDeutTank()));
			break;
		}
		return res;
	}
	private long getCrystalCost(int id) {
		long res = 0;
		switch(id) {
		case 0:
			res = (long)(10 * Math.pow(1.5, planetHandler.getPb().getMetalMine()));
			break;
		case 1:
			res = (long)(15 * Math.pow(1.6, planetHandler.getPb().getCrystalMine()));
			break;
		case 2:
			res = (long)(50 * Math.pow(1.5, planetHandler.getPb().getDeutSyn()));
			break;
		case 3:
			res = (long)(20 * Math.pow(1.5, planetHandler.getPb().getSolarPlant()));
			break;
		case 4:
			res = (long)(200 * Math.pow(1.8, planetHandler.getPb().getFusionReactor()));
			break;
		case 5:
			res = (long)(0 * Math.pow(2, planetHandler.getPb().getMetalStorage()));
			break;
		case 6:
			res = (long)(250 * Math.pow(2, planetHandler.getPb().getCrystalStorage()));
			break;
		case 7:
			res = (long)(500 * Math.pow(2, planetHandler.getPb().getDeutTank()));
			break;
		}
		return res;
	}
	private long getDeutCost(int id) {
		long res = 0;
		switch(id) {
		case 0:
			res = (long)(0 * Math.pow(1.5, planetHandler.getPb().getMetalMine()));
			break;
		case 1:
			res = (long)(0 * Math.pow(1.6, planetHandler.getPb().getCrystalMine()));
			break;
		case 2:
			res = (long)(0 * Math.pow(1.5, planetHandler.getPb().getDeutSyn()));
			break;
		case 3:
			res = (long)(0 * Math.pow(1.5, planetHandler.getPb().getSolarPlant()));
			break;
		case 4:
			res = (long)(100 * Math.pow(1.8, planetHandler.getPb().getFusionReactor()));
			break;
		case 5:
			res = (long)(0 * Math.pow(2, planetHandler.getPb().getMetalStorage()));
			break;
		case 6:
			res = (long)(0 * Math.pow(2, planetHandler.getPb().getCrystalStorage()));
			break;
		case 7:
			res = (long)(0 * Math.pow(2, planetHandler.getPb().getDeutTank()));
			break;
		}
		return res;
	}
	private long getEnergyCost(int id) {
		long res = 0;
		switch(id) {
		case 0:
			res = (long)Math.ceil((10 * planetHandler.getPb().getMetalMine() * Math.pow(1.1, planetHandler.getPb().getMetalMine())));
			break;
		case 1:
			res = (long)Math.ceil((10 * planetHandler.getPb().getCrystalMine() *Math.pow(1.1, planetHandler.getPb().getCrystalMine())));
			break;
		case 2:
			res = (long)Math.ceil((20 * planetHandler.getPb().getDeutSyn() *Math.pow(1.1, planetHandler.getPb().getDeutSyn())));
			break;
		case 3:
			res = (long)(0 * Math.pow(1.5, planetHandler.getPb().getSolarPlant()));
			break;
		case 4:
			res = (long)(0 * Math.pow(1.8, planetHandler.getPb().getFusionReactor()));
			break;
		case 5:
			res = (long)(0 * Math.pow(2, planetHandler.getPb().getMetalStorage()));
			break;
		case 6:
			res = (long)(0 * Math.pow(2, planetHandler.getPb().getCrystalStorage()));
			break;
		case 7:
			res = (long)(0 * Math.pow(2, planetHandler.getPb().getDeutTank()));
			break;
		}
		return res;
	}
	private int idToLvl(int id) {
		int res = 0;
		switch(id) {
		case 0:
			res = planetHandler.getPb().getMetalMine();
			break;
		case 1:
			res = planetHandler.getPb().getCrystalMine();
			break;
		case 2:
			res = planetHandler.getPb().getDeutSyn();
			break;
		case 3:
			res = planetHandler.getPb().getSolarPlant();
			break;
		case 4:
			res = planetHandler.getPb().getFusionReactor();
			break;
		case 5:
			res = planetHandler.getPb().getMetalStorage();
			break;
		case 6:
			res = planetHandler.getPb().getCrystalStorage();
			break;
		case 7:
			res = planetHandler.getPb().getDeutTank();
			break;
		}
		return res;
	}
	private String idToName(int id) {
		String res = "";
		switch(id) {
		case 0:
			res = "Metalmine";
			break;
		case 1:
			res = "Kristallmine";
			break;
		case 2:
			res = "Deuterium-Synthetisierer";
			break;
		case 3:
			res = "Solarkraftwerk";
			break;
		case 4:
			res = "Fusionskraftwerk";
			break;
		case 5:
			res = "Metallspeicher";
			break;
		case 6:
			res = "Kristallspeicher";
			break;
		case 7:
			res = "Deuteriumtank";
			break;
		}
		return res;
	}
	private String idToDescr(int id) {
		String res = "";
		switch(id) {
		case 0:
			res = "Hauptrohstoff für den Bau tragender Strukturen von Bauwerken und Schiffen.";
			break;
		case 1:
			res = "Hier wird Kristall abgebaut - der Hauptrohstoff für elektronische Bauteile und Legierungen.";
			break;
		case 2:
			res = "Deuterium-Synthetisierer entziehen dem Wasser eines Planeten den geringen Deuteriumanteil.";
			break;
		case 3:
			res = "Solarkraftwerke gewinnen aus Sonneneinstrahlung die Energie, die einige Gebäude für den Betrieb benötigen.";
			break;
		case 4:
			res = "Das Fusionskraftwerk gewinnt Energie aus der Fusion von 2 schweren Wasserstoffatomen zu einem Heliumatom.";
			break;
		case 5:
			res = "Lagerstätte für rohe Metallerze, bevor sie weiter verarbeitet werden.";
			break;
		case 6:
			res = "Lagerstätte für rohe Kristalle, bevor sie weiterverarbeitet werden.";
			break;
		case 7:
			res = "Riesige Tanks zur Lagerung des neu gewonnenen Deuteriums.";
			break;
		}
		return res;
	}
	public String getName() {
		return name;
	}
	public int getLvl() {
		return lvl;
	}

	public long getTime() {
		return time;
	}

	public long getEnergy() {
		return energy;
	}

	public long getMetal() {
		return metal;
	}

	public long getCrystal() {
		return crystal;
	}

	public long getDeut() {
		return deut;
	}

	public String getDescr() {
		return descr;
	}

	public String getRec() {
		return rec;
	}

	
	
}
