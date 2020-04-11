package controller;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import org.primefaces.context.RequestContext;

import Task.BuildTask;
import model.Buildable;
import model.Ship;
import model.WorldSettings;

@ManagedBean(name="buildHandler")
@SessionScoped
public class BuildHandler {

	private PlanetHandler planetHandler;
	private EntityManager em;
	private UserTransaction utx;

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
	private String buildDone = "f";
	private boolean fromPage = true;
	private long shipAttack;
	private long shipHull;
	private long shipShield;
	private long shipCargo;
	private long shipSpeed;
	private long shipCons;

	private boolean isBuilding = false;
	private int buildTaskId;
	private String buildTaskName;
	private Date remainingBuildTime = new Date();
	
	private boolean isBuildingR = false;
	private int buildTaskIdR;
	private String buildTaskNameR;
	private Date remainingBuildTimeR = new Date();
	
	private boolean isBuildingS = false;
	private int buildTaskIdS;
	private String buildTaskNameS;
	private Date remainingBuildTimeS = new Date();
	
	private boolean isBuildingD = false;
	private int buildTaskIdD;
	private String buildTaskNameD;
	private Date remainingBuildTimeD = new Date();
	public BuildHandler() {

	}

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
		if(fromPage) {
			if(id!=this.id) {
				newPage = false;
				rec="";
			} else {
				newPage = !newPage;
				rec="";
			}
			this.id = id;
		}
		if(id > 30)
			setShipStats(id);
		Query query = em.createQuery("select k from Buildable k where k.id = :id");
		query.setParameter("id", id);

		try {
			Object res = query.getSingleResult();

			Buildable b = (Buildable)res;
			query = em.createQuery("select max(id) from WorldSettings k");
			int wId =  (int)query.getSingleResult();
			query = em.createQuery("select k from WorldSettings k where k.id = :id");
			query.setParameter("id", wId);
			try {
				Object res2 = query.getSingleResult();
				WorldSettings ws = (WorldSettings)res2;

				name = b.getName();
				lvl = idToLvl(id)+1;
				type = b.getType();
				if(type == 2) {
					count = idToCount(id);
				}
				//System.out.println(b.getBaseCostCrystal()+" "+ b.getResFactor() + " "+ lvl);
				metal = (long)(b.getBaseCostMetal() * Math.pow(b.getResFactor(), lvl));
				crystal = (long)(b.getBaseCostCrystal() * Math.pow(b.getResFactor(), lvl));
				deut = (long)(b.getBaseCostDeut() * Math.pow(b.getResFactor(), lvl));
				if(id == 4) {
					energy = (long) -Math.ceil((b.getBaseCostEnergy() * lvl * Math.pow(b.getEnergyFactor(), lvl)));
				}
				else if(id == 5) {
					energy = (long) -Math.ceil((b.getBaseCostEnergy() * lvl * Math.pow(b.getEnergyFactor(), lvl)));
				}
				else {
					energy = (long) Math.ceil((b.getBaseCostEnergy() * lvl * Math.pow(b.getEnergyFactor(), lvl)));
				}
				if(type == 0) {
					time = (long)Math.ceil((metal+crystal) * 36 / (25 * (1 + planetHandler.getPb().getRoboticFactory()) * Math.pow(2, planetHandler.getPb().getNaniteFactory()) * ws.getGameSpeed()))+5;
					if(time > 100)
						time -= 90;
					timeM = time/60;
					timeS = (int)Math.abs(time-timeM*60);
				}
				else if(type == 1) {
					time = (long)Math.ceil(((metal+crystal) / (1000 * (1 + planetHandler.getPb().getResearchlab())) * 3600 - 1) / ws.getGameSpeed())+5;
					timeM = time/60;
					timeS = (int)Math.abs(time-timeM*60);
				}
				else if(type == 2) {
					time = (long)Math.ceil(((metal+crystal) / (2500 * (1 + planetHandler.getPb().getShipyard())) * Math.pow(2, planetHandler.getPb().getNaniteFactory())) / ws.getGameSpeed() * 60)+5;
					timeM = time/60;
					timeS = (int)Math.abs(time-timeM*60);
				}
				descr = b.getDescr();
				rec = b.getRec();
			} catch(NoResultException e){	
				System.out.println("Keine WS in DB");
			}
		} catch(NoResultException e){	
			System.out.println("Keine Werte in DB");
		}
	}

	private void setShipStats(int id) {
		Query query = em.createQuery("select k from Ship k where k.shipId = :id");
		query.setParameter("id", id);
		try {
			Ship ship = (Ship) query.getSingleResult();
			shipHull = ship.getHull()  * (1 + planetHandler.getPr().getArmor() / 10);
			shipAttack = ship.getAttack()  * (1 + planetHandler.getPr().getWeapon() / 10);
			shipShield = ship.getShield() * (1 + planetHandler.getPr().getShield() / 10);;
			shipCargo = ship.getCargoSpace();
			shipSpeed =  ship.getSpeed() + ship.getSpeed()/10*idToLvl(ship.getDriveScaleTechId());
			shipCons = ship.getConsumption();
		} catch(NoResultException e) {

		}
	}

	private int getMethode(String str) {
		switch(str.toLowerCase()) {
		case "energietechnik":
			return planetHandler.getPr().getEnergy();
		case "computertechnik":
			return planetHandler.getPr().getComputer();
		case "schildtechnik":
			return planetHandler.getPr().getShield();
		case "hyperraumtechnik":
			return planetHandler.getPr().getHyperspacetech();
		case "lasertechnik":
			return planetHandler.getPr().getLaser();
		case "ionentechnik":
			return planetHandler.getPr().getIon();
		case "spionagetechnik":
			return planetHandler.getPr().getEspionage();
		case "impulstriebwerk":
			return planetHandler.getPr().getImpulse();
		case "verbrennungstriebwerk":
			return planetHandler.getPr().getCombustion();
		case "raumschiffpanzerung":
			return planetHandler.getPr().getArmor();
		case "hyperraumantrieb":
			return planetHandler.getPr().getHyperspace();
		case "plasmatechnik":
			return planetHandler.getPr().getPlasma();
		case "gravitontechnik":
			return planetHandler.getPr().getGravitation();
		case "waffentechnik":
			return planetHandler.getPr().getWeapon();
		case "raketensilo":
			return planetHandler.getPb().getMissleSilo();
		case "schiffswerft":
			return planetHandler.getPb().getShipyard();
		case "forschungslabor": 
			return planetHandler.getPb().getResearchlab();
		case "nanitenfabrik":
			return planetHandler.getPb().getNaniteFactory();
		case "roboterfabrik":
			return planetHandler.getPb().getRoboticFactory();
		case "deuterium-synthetisierer":
			return planetHandler.getPb().getDeutSyn();
		default:
			return 99999999;
		}
	}
	
	public void checkBuildTask() {
		int btId = planetHandler.getPb().getTask();
		if(btId == -1)
			isBuilding = false;
		else {
			BuildTask bt = (BuildTask) QHandler.waiting.get(btId);
			if(bt != null) {
				isBuilding = true;
				remainingBuildTime.setTime(Math.abs(bt.getTime().getTime()-System.currentTimeMillis()));
				buildTaskId = bt.getUpgradeId();
				Query query = em.createQuery("select k from Buildable k where k.id = :id");
				query.setParameter("id", buildTaskId);
				try {
					Object res = query.getSingleResult();
					Buildable b = (Buildable)res;
					buildTaskName = b.getName();			
				} catch(NoResultException e){	
					System.out.println("Keine Werte in DB");
				}
			}
		}
	}
	public void checkResearchTask() {
		int btId = planetHandler.getPr().getTask();
		if(btId == -1)
			isBuildingR = false;
		else {
			BuildTask bt = (BuildTask) QHandler.waiting.get(btId);
			if(bt != null) {
				isBuildingR = true;
				remainingBuildTimeR.setTime(Math.abs(bt.getTime().getTime()-System.currentTimeMillis()));
				System.out.println(remainingBuildTimeR);
				buildTaskIdR = bt.getUpgradeId();
				Query query = em.createQuery("select k from Buildable k where k.id = :id");
				query.setParameter("id", buildTaskIdR);
				try {
					Object res = query.getSingleResult();
					Buildable b = (Buildable)res;
					buildTaskNameR = b.getName();			
				} catch(NoResultException e){	
					System.out.println("Keine Werte in DB");
				}
			}
		}
	}
	public void checkShipTask() {
		int btId = planetHandler.getPs().getTask();
		if(btId == -1)
			isBuildingS = false;
		else {
			BuildTask bt = (BuildTask) QHandler.waiting.get(btId);
			if(bt != null) {
				isBuildingS = true;
				remainingBuildTimeS.setTime(Math.abs(bt.getTime().getTime()-System.currentTimeMillis()));
				buildTaskIdS = bt.getUpgradeId();
				Query query = em.createQuery("select k from Buildable k where k.id = :id");
				query.setParameter("id", buildTaskIdS);
				try {
					Object res = query.getSingleResult();
					Buildable b = (Buildable)res;
					buildTaskNameS = b.getName();			
				} catch(NoResultException e){	
					System.out.println("Keine Werte in DB");
				}
			}
		}
		btId = planetHandler.getPd().getTask();
		if(btId == -1)
			isBuildingD = false;
		else {
			BuildTask bt = (BuildTask) QHandler.waiting.get(btId);
			if(bt != null) {
				isBuildingD = true;
				remainingBuildTimeD.setTime(Math.abs(bt.getTime().getTime()-System.currentTimeMillis()));
				buildTaskIdD = bt.getUpgradeId();
				Query query = em.createQuery("select k from Buildable k where k.id = :id");
				query.setParameter("id", buildTaskIdD);
				try {
					Object res = query.getSingleResult();
					Buildable b = (Buildable)res;
					buildTaskNameD = b.getName();			
				} catch(NoResultException e){	
					System.out.println("Keine Werte in DB");
				}
			}
		}
	}

	public void build() {
		buildDone = "f";
		//		planetHandler.updateDataset();
		//TODO
		//could have used type?
		if(id < 16) {//building
			if(planetHandler.getPb().getTask() == -1) {
				if(checkRes() && checkRec()) {
					Date d = new Date(System.currentTimeMillis()+(time*1000));
					//System.out.println("TIME Calc: " + new Timestamp(d.getTime()));
					applyCost();
					new BuildTask(type,d,id,planetHandler.getPg().getPlanetId(), em,utx, planetHandler, this);
					setBuildMessage("Bau gestartet");
					planetHandler.save();
				}
				else {
					System.out.println("Res oder rec fehlen");
				}
			}
			else {
				System.out.println("Es wird bereits gebaut.");
				setBuildMessage("Es wird bereits gebaut.");
			}
		}
		else if(id < 31) { //research
			if(planetHandler.getPr().getTask() == -1) {
				if(checkRes() && checkRec()) {
					Date d = new Date(System.currentTimeMillis()+(time*1000));
					System.out.println("Date: "+d);
					applyCost();
					new BuildTask(type,d,id,planetHandler.getPg().getPlanetId(),em,utx,planetHandler, this);
					setBuildMessage("Forschung gestartet");
					planetHandler.save();
				}
				else {
					System.out.println("Res oder rec fehlen");
				}
			}
			else {
				System.out.println("Es wird bereits geforscht.");
				setBuildMessage("Es wird bereits geforscht.");
			}
		}
		else if(id < 45) { //ship
			if(checkRes() && checkRec()) {
				/*Date qTime = planetHandler.getPs().getqTime();
				qTime = qTime.getTime() < System.currentTimeMillis() ? new Date(System.currentTimeMillis()+(time*1000)) : new Date((time*1000)+qTime.getTime());
				applyCost();
				//				planetHandler.getPs().addTask(new BuildTask(type,qTime,id,planetHandler.getPg().getPlanetId(),em,utx));*/
				Date d = new Date(System.currentTimeMillis()+(time*1000));
				applyCost();
				new BuildTask(type,d,id,planetHandler.getPg().getPlanetId(),em,utx,planetHandler, this);
				setBuildMessage("Bau gestartet");
				//planetHandler.getPs().setqTime(qTime);
				planetHandler.save();
			}
			else {
				System.out.println("Res oder rec fehlen");
			}
		}
		else { // def
			if(checkRes() && checkRec()) {
				/*Date qTime = planetHandler.getPd().getqTime();
				qTime = qTime.getTime() < System.currentTimeMillis() ? new Date(System.currentTimeMillis()+(time*1000)) : new Date((time*1000)+qTime.getTime());			
				applyCost();
				//				planetHandler.getPd().addTask(new BuildTask(type,qTime,id,planetHandler.getPg().getPlanetId(),em,utx));
				planetHandler.getPd().setqTime(qTime);*/
				Date d = new Date(System.currentTimeMillis()+(time*1000));
				applyCost();
				new BuildTask(type,d,id,planetHandler.getPg().getPlanetId(),em,utx,planetHandler, this);
				setBuildMessage("Bau gestartet");
				planetHandler.save();
			}
			else {
				System.out.println("Res oder rec fehlen");
			}
		}
	}

	

	public void afterBuild() {
		if(buildDone.equals("t")) {
			fromPage = false;
			setActive(id);
			fromPage = true;
			FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("mainForm:center_body_top");
			FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("mainForm:center_body_bottom");
			RequestContext.getCurrentInstance().update("mainForm:growl");
			setBuildMessage("Bau abgeschlossen");
		}

	}

	private void setBuildMessage(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		if(context != null) {
			context.addMessage(null, new FacesMessage(msg));
		} else {
			System.out.println("context: null");
		}
	}

	private void applyCost() {
		double m = planetHandler.getPg().getMetal();
		double c = planetHandler.getPg().getCrystal();
		double d = planetHandler.getPg().getDeut();
		int uE = planetHandler.getPg().getUsedEnergy();
		if(energy > 0)
			planetHandler.getPg().setUsedEnergy((int)(uE+energy));
		planetHandler.getPg().setMetal(m - metal);
		planetHandler.getPg().setCrystal(c - crystal);
		planetHandler.getPg().setDeut(d - deut);
	}
	private boolean checkRes() {
		boolean ressourcen = true;
		if(planetHandler.getPg().getMetal() < metal) {
			setBuildMessage("Nicht genug Metal");
			ressourcen = false;
		}
		if(planetHandler.getPg().getCrystal() < crystal) {
			setBuildMessage("Nicht genug Kristall");
			ressourcen = false;
		}
		if(planetHandler.getPg().getDeut() < deut) {
			setBuildMessage("Nicht genug Deuterium");
			ressourcen = false;
		}
		if(planetHandler.getPg().getMaxEnergy()-planetHandler.getPg().getUsedEnergy() < energy) {
			setBuildMessage("Nicht genug Energie");
			ressourcen = false;
		}
		return ressourcen;
	}

	private boolean checkRec() {
		boolean buildable = true;
		String[] recs = rec.split("<br/>");
		if(recs[0].equals("")) {
			return true;
		} else {
			int i = 0;
			while(buildable && i < recs.length) {
				String[] recs2 = recs[i].split(":");
				if(getMethode(recs2[0])!=99999999) {
					if(Integer.valueOf(recs2[1]) <= getMethode(recs2[0])){
						++i;
					} else {
						System.out.println("Gesetzt");
						buildable = false;
					}
				}
			}
			if(buildable){
				System.out.println("erfüllt");
				return true;
			} else {
				System.out.println("nicht erfüllt");
				setBuildMessage("Anforderung nicht erfüllt");
				return false;
			}
		}
	}

	public boolean isNewPage() {
		return newPage;
	}

	public void setNewPage(String str, String update) {
		this.id = 0;
		if(str.equals("true")) {
			this.newPage = true;
		} else {
			this.newPage = false;
		}

		switch(update) {
		case "buildings":
			planetHandler.updateBuildings();
			break;
		case "def":
			planetHandler.updateDef();
			break;
		case "research":
			planetHandler.updateResearch();
			break;
		case "ships":
			planetHandler.updateShips();
			break;
		default: 
			break;
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

	public String getBuildDone() {
		return buildDone;
	}

	public void setBuildDone(String str) {
		this.buildDone = str;
	}
	public boolean isBuilding() {
		return isBuilding;
	}

	public void setIsBuilding(boolean isBuilding) {
		this.isBuilding = isBuilding;
	}

	public int getBuildTaskId() {
		return buildTaskId;
	}

	public void setBuildTaskId(int buildTaskId) {
		this.buildTaskId = buildTaskId;
	}

	public String getBuildTaskName() {
		return buildTaskName;
	}

	public void setBuildTaskName(String buildTaskName) {
		this.buildTaskName = buildTaskName;
	}

	public Date getRemainingBuildTime() {
		return remainingBuildTime;
	}

	public void setRemainingBuildTime(Date remainingBuildTime) {
		this.remainingBuildTime = remainingBuildTime;
	}

	public long getShipAttack() {
		return shipAttack;
	}

	public void setShipAttack(long shipAttack) {
		this.shipAttack = shipAttack;
	}

	public long getShipHull() {
		return shipHull;
	}

	public void setShipHull(long shipHull) {
		this.shipHull = shipHull;
	}

	public long getShipShield() {
		return shipShield;
	}

	public void setShipShield(long shipShield) {
		this.shipShield = shipShield;
	}

	public long getShipCargo() {
		return shipCargo;
	}

	public void setShipCargo(long shipCargo) {
		this.shipCargo = shipCargo;
	}

	public long getShipSpeed() {
		return shipSpeed;
	}

	public void setShipSpeed(long shipSpeed) {
		this.shipSpeed = shipSpeed;
	}

	public long getShipCons() {
		return shipCons;
	}

	public void setShipCons(long shipCons) {
		this.shipCons = shipCons;
	}
	
	public boolean isBuildingR() {
		return isBuildingR;
	}

	public void setBuildingR(boolean isBuildingR) {
		this.isBuildingR = isBuildingR;
	}

	public int getBuildTaskIdR() {
		return buildTaskIdR;
	}

	public void setBuildTaskIdR(int buildTaskIdR) {
		this.buildTaskIdR = buildTaskIdR;
	}

	public String getBuildTaskNameR() {
		return buildTaskNameR;
	}

	public void setBuildTaskNameR(String buildTaskNameR) {
		this.buildTaskNameR = buildTaskNameR;
	}

	public Date getRemainingBuildTimeR() {
		return remainingBuildTimeR;
	}

	public void setRemainingBuildTimeR(Date remainingBuildTimeR) {
		this.remainingBuildTimeR = remainingBuildTimeR;
	}

	public boolean isBuildingS() {
		return isBuildingS;
	}

	public void setBuildingS(boolean isBuildingS) {
		this.isBuildingS = isBuildingS;
	}

	public int getBuildTaskIdS() {
		return buildTaskIdS;
	}

	public void setBuildTaskIdS(int buildTaskIdS) {
		this.buildTaskIdS = buildTaskIdS;
	}

	public String getBuildTaskNameS() {
		return buildTaskNameS;
	}

	public void setBuildTaskNameS(String buildTaskNameS) {
		this.buildTaskNameS = buildTaskNameS;
	}

	public Date getRemainingBuildTimeS() {
		return remainingBuildTimeS;
	}

	public void setRemainingBuildTimeS(Date remainingBuildTimeS) {
		this.remainingBuildTimeS = remainingBuildTimeS;
	}

	public boolean isBuildingD() {
		return isBuildingD;
	}

	public void setBuildingD(boolean isBuildingD) {
		this.isBuildingD = isBuildingD;
	}

	public int getBuildTaskIdD() {
		return buildTaskIdD;
	}

	public void setBuildTaskIdD(int buildTaskIdD) {
		this.buildTaskIdD = buildTaskIdD;
	}

	public String getBuildTaskNameD() {
		return buildTaskNameD;
	}

	public void setBuildTaskNameD(String buildTaskNameD) {
		this.buildTaskNameD = buildTaskNameD;
	}

	public Date getRemainingBuildTimeD() {
		return remainingBuildTimeD;
	}

	public void setRemainingBuildTimeD(Date remainingBuildTimeD) {
		this.remainingBuildTimeD = remainingBuildTimeD;
	}
}
