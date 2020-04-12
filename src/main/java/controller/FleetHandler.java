package controller;

import java.sql.Date;
import java.util.Arrays;

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

import org.primefaces.context.RequestContext;

import Task.FleetTask;
import model.Ship;
import model.Solarsystem;
import model.WorldSettings;
import planets.Planets_General;
import planets.Planets_Ships;

@ManagedBean(name="fleetHandler")
@SessionScoped
public class FleetHandler {

	private PlanetHandler planetHandler;
	private EntityManager em;
	private UserTransaction utx;

	private int[] ships = new int[13];
	private int stage = 0;
	private int galaxy = 0;//planetHandler.getPg().getGalaxy();
	private int solarSystem = 0;//planetHandler.getPg().getSolarsystem();	
	private int position = 0;//planetHandler.getPg().getPosition();
	private Date duration = new Date(0);
	private int consumption = 0;
	private long speed = 0;
	private long travelTime = 0;
	private Date arrival = new Date(System.currentTimeMillis());
	private Date rturn = new Date(System.currentTimeMillis());
	private int cargoSpace = 0;
	private int distance = 0;
	private String name = "Kein Planet an Zielkoordinaten.";
	private Planets_General target;
	private boolean isValidTarget = false;
	private long[] cargo = {0,0,0};
	private String missionDone = "f";
	private int userid;
	
	private int mission;

	public FleetHandler(PlanetHandler planetHandler, EntityManager em, UserTransaction utx) {
		super();
		this.planetHandler = planetHandler;
		this.em = em;
		this.utx = utx;
		//planetHandler.getPs().setLightFighter(50);
	}
	public void changeCoords() {
		if(galaxy == planetHandler.getPg().getGalaxy() &&
				solarSystem == planetHandler.getPg().getSolarsystem() &&
				position == planetHandler.getPg().getPosition()) {
			isValidTarget = false;
			name = planetHandler.getPg().getName();
			System.out.println("Start kann nicht ziel sein idk xd.");
			setMessage("Start kann nicht Ziel sein");
		}
		else {
			Query query = em.createQuery("select k from Planets_General k where k.galaxy = :galaxy and k.solarsystem = :solarsystem and k.position = :position");
			query.setParameter("galaxy", galaxy);
			query.setParameter("solarsystem", solarSystem);
			query.setParameter("position", position);
			try {
				Object res = query.getSingleResult();

				target = (Planets_General)res;

				name = target.getName();
				calcDistance();
				calcTravelTime();
				isValidTarget = true;
			} catch(NoResultException e){
				isValidTarget = false;
				calcDistance();
				calcTravelTime();
				System.out.println("Kein Planet an "+galaxy+":"+solarSystem+":"+position);
				setMessage("Kein Planet an "+galaxy+":"+solarSystem+":"+(position+1));
			}
		}	
	}
	public void changeRes() {
		//TODO
	}
	private void calcDistance() {
		int galaxyDiff = Math.abs(planetHandler.getPg().getGalaxy()-galaxy);
		int solarDiff = Math.abs(planetHandler.getPg().getSolarsystem()-solarSystem);
		int posDiff = Math.abs(planetHandler.getPg().getPosition()-position);

		distance = galaxyDiff*20000;
		distance += solarDiff == 0 ? 0 : solarDiff*95+2700;
		distance += posDiff*5 + 1000;
	}

	public void next() {
		for(int i : ships)
			System.out.print(i+" ");
		System.out.println("");
	}
	public void updateTime() {
		arrival = new Date(System.currentTimeMillis()+travelTime*1000);
		rturn = new Date(System.currentTimeMillis()+2*travelTime*1000);
	}


	public void setCoords(int galaxy, int solarSystem, int position) {
		this.setSolarSystem(solarSystem);
		this.setGalaxy(galaxy);
		this.setPosition(position);
	}
	public int getStage() {
		return stage;
	}
	public void setStage(int stage) {
		if(stage == 0) {
			this.stage = stage;
			Arrays.fill(ships, 0);
			Arrays.fill(cargo, 0);
		}
		if(stage == 1) {
			if(checkShips()) {
				galaxy = planetHandler.getPg().getGalaxy();
				solarSystem = planetHandler.getPg().getSolarsystem();
				position = planetHandler.getPg().getPosition();
				name = planetHandler.getPg().getName();
				calcSpeed();
				calcCargoSpace();
				calcTravelTime();
				this.stage = stage;
			}
			else {
				System.out.println("Keine Schiffe zum versenden Ausgewählt");
				setMessage("Kein Schiff zum versenden Ausgewählt");
			}
		}
		else if(stage == 2)		{
			if(isValidTarget) {
				this.stage = stage;
			}
			else {
				System.out.println("gebe gültige Koordinaten ein.");
				setMessage("gebe gültige Koordinaten ein");
			}
		}
		else if(stage == 3)		{
			if(isValidMission()) {
				if(isCargoValid()) {
					setMessage("Mission gestartet");
					new FleetTask(em, utx, mission, arrival, travelTime, planetHandler.getPg().getPlanetId(),target.getPlanetId(), ships, cargo, this);
					
					planetHandler.getPg().setMetal(planetHandler.getPg().getMetal()-cargo[0]);
					planetHandler.getPg().setCrystal(planetHandler.getPg().getCrystal()-cargo[1]);
					planetHandler.getPg().setDeut(planetHandler.getPg().getDeut()-cargo[2]);
					
					subtractShips();
					System.out.println("m: "+cargo[0]+" c: "+cargo[1]+" d: "+cargo[2]);
					planetHandler.save();
					setStage(0);
				}
				else {
					System.out.println("Cargospace ist zu klein");
					setMessage("Cargospace zu klein");
				}			
			}
			else {
				System.out.println("gebe gültige Mission ein.");
				setMessage("gebe eine Gültige Mission ein.");
			}
		}
	}
	public void kolo(int galaxyid, int systemid, int position, boolean fromGalaxy) {
		if(fromGalaxy) {
			System.out.println("Berechnung");
			calcSpeed();
			calcTravelTime();
			arrival = new Date(System.currentTimeMillis()+travelTime*1000);
			for(int i = 0; i < ships.length; ++i) {
				if(i == 6) {
					ships[i] = 1;
				} else {
					ships[i] = 0;
				}
			}
			cargo[0] = galaxyid;
			cargo[1] = systemid;
			cargo[2] = position;
			new FleetTask(em, utx, 0, arrival, travelTime, -1, -1, ships, cargo, this);
		} else {
			System.out.println("Kolonisieren");
			planetHandler.colonizePlanet(userid, position, galaxyid, systemid);
			Query query = em.createQuery("select k from Solarsystem k where k.systemId = :id");
			query.setParameter("id", galaxyid);
			Solarsystem system = (Solarsystem) query.getSingleResult();
			system.setPlanets(system.getPlanets()-1);
			if(position >=3 || position <= 12) {
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
	}
	private void subtractShips() {
		for(int i=0; i<ships.length;++i)
			setById(i,planetHandler.getPs(),getById(i,planetHandler.getPs())-ships[i]);
	}
	private void setMessage(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		if(context != null)
			context.addMessage(null, new FacesMessage(msg));
	}

	private boolean checkShips() {
		int sum = 0;
		for(int i=0;i<ships.length;++i) {
			int actualShips = idToLvl(31+i);
			System.out.println("Shiff: "+i+" Anzahl: "+actualShips);
			sum += ships[i] > actualShips ? actualShips : ships[i];
		}
		System.out.println("Summe: "+sum);
		if(sum == 0) {
			Arrays.fill(ships, 0);
		}
		return sum != 0 ? true : false;
	}
	private boolean isValidMission() {
		boolean res = false;
		int temp = 0;
		switch(mission) {
		case 0:
			for(int i=0;i<ships.length-1;++i)
				if(i != 10)
					temp += ships[i];
			res = temp == 0 && ships[10] == 1;
			break;
		case 1:
			for(int i=0;i<ships.length-1;++i)
				if(i != 11)
					temp += ships[i];
			res = temp == 0;
			break;
		case 2:
			res = true;
			break;
		case 3:
			res = true;
			break;
		case 4:
			for(int i=0;i<ships.length-1;++i)
				temp += ships[i];
			res = temp == 0;
			break;
		case 5:
			res = true;
			break;
		}
		return res;
	}
	private boolean isCargoValid() {
		return cargo[0]+cargo[1]+cargo[2] <= cargoSpace ? true : false;
	}
	private void calcSpeed() {
		long minSpeed = Long.MAX_VALUE;
		for(int i=0;i<ships.length;++i) {
			if(ships[i] != 0) {
				Query query = em.createQuery("select k from Ship k where k.shipId = :id");
				query.setParameter("id", 31+i);
				try {
					Object res = query.getSingleResult();

					Ship s = (Ship)res;
					long tspeed = s.getSpeed() + s.getSpeed()/10*idToLvl(s.getDriveScaleTechId());					
					minSpeed = tspeed < minSpeed ? tspeed : minSpeed;
					System.out.println(i+" "+tspeed+" "+minSpeed+" "+speed);
				} catch(NoResultException e){	
					System.out.println("Kein Schiff mit id"+(31+i)+" in der db.");
					setMessage("Kein Schiff vorhanden");
				}
			}			
		}
		speed = minSpeed;
	}
	
	public void afterMission() {
		if(missionDone.equals("t")) {
			FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("mainForm:center_body_top");
			FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("mainForm:center_body_bottom");
			RequestContext.getCurrentInstance().update("mainForm:growl");
			setMessage("Mission abgeschlossen");
		}
	}
	
	private int idToLvl(int id) {
		int res = 0;
		switch(id) {
		case 21:
			res = planetHandler.getPr().getCombustion();
			break;
		case 22:
			res = planetHandler.getPr().getImpulse();
			break;
		case 23:
			res = planetHandler.getPr().getHyperspace();
			break;
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
		}
		return res;
	}

	private void calcCargoSpace() {
		for(int i=0;i<ships.length;++i) {
			if(ships[i] != 0) {
				Query query = em.createQuery("select k from Ship k where k.shipId = :id");
				query.setParameter("id", 31+i);
				try {
					Object res = query.getSingleResult();

					Ship s = (Ship)res;
					cargoSpace += s.getCargoSpace()*ships[i];

				} catch(NoResultException e){	
					System.out.println("Kein Schiff mit id"+(31+i)+" in der db.");
					setMessage("Kein Cargo Schiff vorhanden");
				}
			}			
		}
	}

	private void calcTravelTime() {
		Query query = em.createQuery("select max(id) from WorldSettings k");
		int id =  (int)query.getSingleResult();
		query = em.createQuery("select k from WorldSettings k where k.id = :id");
		query.setParameter("id", id);

		try {
			Object res = query.getSingleResult();
			WorldSettings ws = (WorldSettings)res;
			
			travelTime = (long) (((3500 / 2) * Math.pow((distance * 10 / speed),0.5)+10) / ws.getFleetSpeed());
			duration.setTime(travelTime*1000);
		} catch(NoResultException e){	
			System.out.println("Keine WS in DB");
		}		
	}
	private int getById(int id, Planets_Ships ps) {
		int res = 999999;
		switch(id+31) {
		case 31:
			res = ps.getLightFighter();
			break;
		case 32:
			res = ps.getHeavyFighter();
			break;
		case 33:
			res = ps.getCruiser();
			break;
		case 34:
			res = ps.getBattleship();
			break;
		case 35:
			res = ps.getBattlecruiser();
			break;
		case 36:
			res = ps.getBomber();
			break;
		case 37:
			res = ps.getDestroyer();
			break;
		case 38:
			res = ps.getDeathStar();
			break;
		case 39:
			res = ps.getSmallCargoShip();
			break;
		case 40:
			res = ps.getLargeCargoShip();
			break;
		case 41:
			res = ps.getColonyShip();
			break;
		case 42:
			res = ps.getRecycler();
			break;
		case 43:
			res = ps.getEspionageProbe();
			break;
		case 44:
			res = ps.getSolarSattlelite();
			break;
		}
		return res;
	}
	private void setById(int id, Planets_Ships ps, int val) {
		switch(id+31) {
		case 31:
			ps.setLightFighter(val);
			break;
		case 32:
			ps.setHeavyFighter(val);
			break;
		case 33:
			ps.setCruiser(val);
			break;
		case 34:
			ps.setBattleship(val);
			break;
		case 35:
			ps.setBattlecruiser(val);
			break;
		case 36:
			ps.setBomber(val);
			break;
		case 37:
			ps.setDestroyer(val);
			break;
		case 38:
			ps.setDeathStar(val);
			break;
		case 39:
			ps.setSmallCargoShip(val);
			break;
		case 40:
			ps.setLargeCargoShip(val);
			break;
		case 41:
			ps.setColonyShip(val);
			break;
		case 42:
			ps.setRecycler(val);
			break;
		case 43:
			ps.setEspionageProbe(val);
			break;
		case 44:
			ps.setSolarSattlelite(val);
			break;
		}
	}
	public long[] getCargo() {
		return cargo;
	}
	public void setCargo(long[] cargo) {
		this.cargo = cargo;
	}
	public void setMission(int mission) {
		setMessage("Mission gesetzt!");
		this.mission = mission;
	}
	public int getMission() {
		return mission;
	}
	public int[] getShips() {
		return ships;
	}

	public void setShips(int[] ships) {
		this.ships = ships;
	}
	public int getSolarSystem() {
		return solarSystem;
	}
	public void setSolarSystem(int solarSystem) {
		this.solarSystem = solarSystem;
	}
	public int getGalaxy() {
		return galaxy;
	}
	public void setGalaxy(int galaxy) {
		this.galaxy = galaxy;
	}
	public int getPosition() {
		return position+1;
	}
	public void setPosition(int position) {
		if(position == 0 ) {
			this.position = 0;
		} else {
			this.position = position-1;
		}
	}
	public Date getDuration() {
		return duration;
	}
	public void setDuration(Date duration) {
		this.duration = duration;
	}
	public int getConsumption() {
		return consumption;
	}
	public void setConsumption(int consumption) {
		this.consumption = consumption;
	}
	public long getSpeed() {
		return speed;
	}
	public void setSpeed(long speed) {
		this.speed = speed;
	}
	public Date getArrival() {
		return arrival;
	}
	public void setArrival(Date arrival) {
		this.arrival = arrival;
	}
	public Date getRturn() {
		return rturn;
	}
	public void setRturn(Date rturn) {
		this.rturn = rturn;
	}
	public int getCargoSpace() {
		return cargoSpace;
	}
	public void setCargoSpace(int cargoSpace) {
		this.cargoSpace = cargoSpace;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getTravelTime() {
		return travelTime;
	}
	public void setTravelTime(long travelTime) {
		this.travelTime = travelTime;
	}
	public String getMissionDone() {
		return missionDone;
	}
	public void setMissionDone(String missionDone) {
		this.missionDone = missionDone;
	}
	public PlanetHandler getPlanetHandler() {
		return planetHandler;
	}
	public void setPlanetHandler(PlanetHandler planetHandler) {
		this.planetHandler = planetHandler;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
}
