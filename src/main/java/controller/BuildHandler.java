package controller;

import java.sql.Date;
import java.sql.Timestamp;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import Task.BuildTask;
import model.Buildable;

@ManagedBean(name="buildHandler")
@SessionScoped
public class BuildHandler {

	private PlanetHandler planetHandler;
	private EntityManager em;
	private UserTransaction utx;
	private int userId;
	
	private int id;
	
	private String name;
	private int lvl;	
	private int count;
	private int type;
	
	private long metal;
	private long crystal;
	private long deut;
	private long energy;
	private long time;
	private long timeM;
	private int timeS;
	private String descr;
	private String rec;
	private boolean newPage;
	
	public BuildHandler(PlanetHandler p, EntityManager em, UserTransaction utx) {
		this.planetHandler = p;
		this.em = em;
		this.utx = utx;
		id = 1;
		type = 0;
		name = "alles";
		lvl = 0;
		count = 0;
		metal = 0;
		crystal = 0;
		deut = 0;
		energy = 0;
		time = 0;
		descr = "alles";
		rec = "alles";
	}
	
	public void setActive(int id){
		newPage=false;
		this.id = id;
		Query query = em.createQuery("select k from Buildable k where k.id = :id");
		query.setParameter("id", id);

		try {
			Object res = query.getSingleResult();
			
				Buildable b = (Buildable)res;
				name = b.getName();
				lvl = idToLvl(id)+1;
				type = b.getType();
				if(type == 2) {
					count = idToCount(id);
				}
				System.out.println(b.getBaseCostCrystal()+" "+ b.getResFactor() + " "+ lvl);
				metal = (long)(b.getBaseCostMetal() * Math.pow(b.getResFactor(), lvl));
				crystal = (long)(b.getBaseCostCrystal() * Math.pow(b.getResFactor(), lvl));
				deut = (long)(b.getBaseCostDeut() * Math.pow(b.getResFactor(), lvl));
				if(id == 3) {
					energy = (long) -Math.ceil((b.getBaseCostEnergy() * lvl * Math.pow(b.getEnergyFactor(), lvl)));
				}
				else if(id == 4) {
					energy = (long) -Math.ceil((b.getBaseCostEnergy() * lvl * Math.pow(b.getEnergyFactor(), lvl)));
				}
				else {
					energy = (long) Math.ceil((b.getBaseCostEnergy() * lvl * Math.pow(b.getEnergyFactor(), lvl)));
				}
				if(type == 0) {
					time = (long)Math.ceil((metal+crystal) * 36 / (25 * (1 + planetHandler.getPb().getRoboticFactory()) * Math.pow(2, planetHandler.getPb().getNaniteFactory()) * 4));
					if(time > 100)
						time -= 90;
					timeM = time/60;
					timeS = (int)Math.abs(time-timeM*60);
				}
				descr = b.getDescr();
				rec = b.getRec();
		} catch(NoResultException e){	
			System.out.println("Keine Werte in DB");
		}
	}
	public void build() {
		if(planetHandler.getPb().getTask() == null) {
			System.out.println("ayooo");
			if(checkRes() && checkRec()) {
				System.out.println("qwerty");
				Date d = new Date(System.currentTimeMillis()+(time*1000));
				System.out.println("TIME Calc: " + new Timestamp(d.getTime()));
				new BuildTask(type,d,id,planetHandler.getPg().getPlanetId(),em,utx);
			}
		}
		else {
			System.out.println("Es wird bereits gebaut.");
		}
			
	}
	
	private boolean checkRes() {
		return (planetHandler.getPg().getMetal() >= metal &&
				planetHandler.getPg().getCrystal() >= crystal &&
				planetHandler.getPg().getDeut() >= deut &&
				planetHandler.getPg().getEnergy() >= energy);
	}
	
	private boolean checkRec() {
		return true;
	}
	
	public boolean isNewPage() {
		return newPage;
	}

	public void setNewPage(String str) {
		if(str.equals("true")) {
			this.newPage = true;
		} else {
			this.newPage = false;
		}
	}
	
	/*** starting from 1 not 0 ..
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
		int res = 9999;
		switch(id) {
			case 1:
				res = planetHandler.getPb().getMetalMine();
				break;
			case 2:
				res = planetHandler.getPb().getCrystalMine();
				break;
			case 3:
				res = planetHandler.getPb().getDeutSyn();
				break;
			case 4:
				res = planetHandler.getPb().getSolarPlant();
				break;
			case 5:
				res = planetHandler.getPb().getFusionReactor();
				break;
			case 6:
				res = planetHandler.getPb().getMetalStorage();
				break;
			case 7:
				res = planetHandler.getPb().getCrystalStorage();
				break;
			case 8:
				res = planetHandler.getPb().getDeutTank();
				break;
			case 9:
				res = planetHandler.getPb().getRoboticFactory();
				break;
			case 10:
				res = planetHandler.getPb().getShipyard();
				break;
			case 11:
				res = planetHandler.getPb().getResearchlab();
				break;
			case 12:
				res = planetHandler.getPb().getAlliancedepot();
				break;
			case 13:
				res = planetHandler.getPb().getMissleSilo();
				break;
			case 14:
				res = planetHandler.getPb().getNaniteFactory();
				break;
			case 15:
				res = planetHandler.getPb().getTerraformer();
				break;
			case 16:
				res = planetHandler.getPr().getEnergy();
				break;
			case 17:
				res = planetHandler.getPr().getLaser();
				break;
			case 18:
				res = planetHandler.getPr().getIon();
				break;
			case 19:
				res = planetHandler.getPr().getHyperspacetech();
				break;
			case 20:
				res = planetHandler.getPr().getPlasma();
				break;
			case 21:
				res = planetHandler.getPr().getCombustion();
				break;
			case 22:
				res = planetHandler.getPr().getImpulse();
				break;
			case 23:
				res = planetHandler.getPr().getHyperspace();
				break;
			case 24:
				res = planetHandler.getPr().getEspionage();
				break;
			case 25:
				res = planetHandler.getPr().getComputer();
				break;
			case 26:
				res = planetHandler.getPr().getAstrophysics();
				break;
			case 27:
				res = planetHandler.getPr().getGravitation();
				break;
			case 28:
				res = planetHandler.getPr().getWeapon();
				break;
			case 29:
				res = planetHandler.getPr().getShield();
				break;
			case 30:
				res = planetHandler.getPr().getArmor();
				break;
			case 31:
			case 32:
			case 33:
			case 34:
			case 35:
			case 36:
			case 37:
			case 38:
			case 39:
			case 40:
			case 41:
			case 42:
			case 43:
			case 44:
			case 45:
			case 46:
			case 47:
			case 48:
			case 49:
			case 50:
			case 51:
			case 52:
			case 53:
			case 54:
				res = 0;
			break;
		}
		return res;
	}
	private int idToCount(int id) {
		int res = 9999;
		switch(id) {
			case 31:
				res = planetHandler.getPs().getLightFighter();
				break;
			case 32:
				res = planetHandler.getPs().getHeavyFighter();
				break;
			case 33:
				res = planetHandler.getPs().getCruiser();
				break;
			case 34:
				res = planetHandler.getPs().getBattleship();
				break;
			case 35:
				res = planetHandler.getPs().getBattlecruiser();
				break;
			case 36:
				res = planetHandler.getPs().getBomber();
				break;
			case 37:
				res = planetHandler.getPs().getDestroyer();
				break;
			case 38:
				res = planetHandler.getPs().getDeathStar();
				break;
			case 39:
				res = planetHandler.getPs().getSmallCargoShip();
				break;
			case 40:
				res = planetHandler.getPs().getLargeCargoShip();
				break;
			case 41:
				res = planetHandler.getPs().getColonyShip();
				break;
			case 42:
				res = planetHandler.getPs().getRecycler();
				break;
			case 43:
				res = planetHandler.getPs().getEspionageProbe();
				break;
			case 44:
				res = planetHandler.getPs().getSolarSattlelite();
				break;
			case 45:
				res = planetHandler.getPd().getRocketLauncher();
				break;
			case 46:
				res = planetHandler.getPd().getLightLaser();
				break;
			case 47:
				res = planetHandler.getPd().getHeavyLaser();
				break;
			case 48:
				res = planetHandler.getPd().getGaussCannon();
				break;
			case 49:
				res = planetHandler.getPd().getIonCannon();
				break;
			case 50:
				res = planetHandler.getPd().getPlasmaTurret();
				break;
			case 51:
				res = planetHandler.getPd().getSmallShieldDome();
				break;
			case 52:
				res = planetHandler.getPd().getLargeShieldDome();
				break;
			case 53:
				res = planetHandler.getPd().getAntiBallisticMissle();
				break;
			case 54:
				res = planetHandler.getPd().getInterplanetaryMissle();
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
	public void setUserId(int userId) {
		this.userId = userId;
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

	public int getId() {
		return id;
	}

	public long getTimeM() {
		return timeM;
	}

	public int getTimeS() {
		return timeS;
	}

	public int getCount() {
		return count;
	}	
}
