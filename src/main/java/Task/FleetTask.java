package Task;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import controller.FleetHandler;
import controller.QHandler;
import model.Fight;
import model.Flight;
import model.Solarsystem;
import model.User;
import planets.Planets_General;
import planets.Planets_Ships;

public class FleetTask implements Task, Serializable{
	
	private EntityManager em;	
	private UserTransaction utx;
	
	private static final long serialVersionUID = 1L;
	// 0-> kolo		1->tf	2-> transport	3-> stationieren	4-> spio	5-> angriff
	private int type;
	private Date time;
	private long duration;
	private int planet;
	private int targetPlanet;
	private int[] ships;
	private long[] cargo;
	private FleetHandler fleetHandler;
	private Flight flight;

	
	public FleetTask(EntityManager em, UserTransaction utx, int type, Date time, long duration, int planet,
			int targetPlanet, int[] ships, long[] cargo, FleetHandler fleetHandler) {
		this.em = em;
		this.utx = utx;
		this.type = type;
		this.time = time;
		this.duration = duration;
		this.planet = planet;
		this.targetPlanet = targetPlanet;
		this.ships = ships;
		this.cargo = cargo;
		this.fleetHandler = fleetHandler;
		//TODO
		/*** Delete Ships on mission from Planets_General ***/
		System.out.println("Fleettask mit type "+type);
		/** Add to queue for schedule **/
		QHandler.queued.add(this);
	}

	@Override
	public int getType() {
		return type;
	}

	@Override
	public Date getTime() {
		return time;
	}

	@Override
	public void executeTask() {
		// TODO Auto-generated method stub
		switch(type) {
		case 0:
			kolo();
			break;
		case 1:
			tf();
			break;
		case 2:
			transport();
			break;
		case 3:
			station();
			break;
		case 4:
			espionage();
			break;
		case 5:
			attack();
			break;
		}
	}
	private void kolo() {
		Query query = em.createQuery("select k from Planets_General k where k.planetId = :planetId");
		query.setParameter("planetId", planet);
		
		try {
			Object res = query.getSingleResult();
			Planets_General pg = (Planets_General)res;
			
			fleetHandler.getPlanetHandler().getGalaxyHandler().colonize(fleetHandler.getPlanetHandler(), pg.getUserId(), targetPlanet);
			
		} catch(NoResultException e){	
			System.out.println("Keine pg mit id "+targetPlanet+" in DB - station");
		}
		deleteMe();
	}
	private void tf() {
		//TODO
	}
	private void transport() {
		Query query = em.createQuery("select k from Planets_General k where k.planetId = :planetId");
		query.setParameter("planetId", targetPlanet);
		
		try {
			Object res = query.getSingleResult();
			Planets_General pg = (Planets_General)res;

			pg.setMetal(pg.getMetal()+cargo[0]);
			pg.setCrystal(pg.getCrystal()+cargo[1]);
			pg.setDeut(pg.getDeut()+cargo[2]);
				
			cargo[0] = cargo[1] = cargo[2] = 0;
								
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
			new FleetTask(em, utx, 3, new Date(System.currentTimeMillis()+duration), duration, targetPlanet, planet, ships, cargo, fleetHandler);
		} catch(NoResultException e){	
			System.out.println("Keine pg mit id "+targetPlanet+" in DB - station");
		}
		deleteMe();
	}
	private void station() {
		Query query = em.createQuery("select k from Planets_General k where k.planetId = :planetId");
		query.setParameter("planetId", targetPlanet);
		
		try {
			Object res = query.getSingleResult();
			Planets_General pg = (Planets_General)res;
			query = em.createQuery("select k from Planets_Ships k where k.planetId = :planetId");
			query.setParameter("planetId", targetPlanet);
			
			try {
				Object res2 = query.getSingleResult();
				Planets_Ships ps = (Planets_Ships)res2;
				
				pg.setMetal(pg.getMetal()+cargo[0]);
				pg.setCrystal(pg.getCrystal()+cargo[1]);
				pg.setDeut(pg.getDeut()+cargo[2]);
				
				for(int i=0; i<ships.length;++i)
					setById(i,ps,getById(i,ps)+ships[i]);
				
				try {
					utx.begin();
				} catch (NotSupportedException | SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				em.merge(pg);
				em.merge(ps);
				try {
					utx.commit();
				} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
						| HeuristicRollbackException | SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}					
			} catch(NoResultException e){	
				System.out.println("Keine pg mit id "+targetPlanet+" in DB - station");
			}
		} catch(NoResultException e){	
			System.out.println("Keine pg mit id "+targetPlanet+" in DB - station");
		}
		deleteMe();
	}
	private void espionage() {
		//TODO
	}
	private void attack() {
		@SuppressWarnings("unused")
		Fight fight = new Fight(planet, ships, cargo, targetPlanet, em, utx, fleetHandler);
		int res = 0;
		for(int i=0;i<ships.length;++i) {
			res += ships[i];
		}
		if(res != 0)
			new FleetTask(em, utx, 3, new Date(System.currentTimeMillis()+duration), duration, targetPlanet, planet, ships, cargo, fleetHandler);
		deleteMe();
	}
	@Override
	public void saveToDB(int id) {
		try {
			utx.begin();
		} catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		flight = new Flight(planet,targetPlanet,id,type,time);
		em.merge(flight);
		try {
			utx.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	private void deleteMe() {
		try {
			utx.begin();
		} catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		em.remove(flight);
		try {
			utx.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
}