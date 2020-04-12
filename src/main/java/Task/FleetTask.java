package Task;

import java.io.Serializable;
import java.util.Arrays;
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
import model.Messages;
import model.Solarsystem;
import model.User;
import planets.Planets_Buildings;
import planets.Planets_Def;
import planets.Planets_General;
import planets.Planets_Research;
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
	private int[] ships =  new int[13];
	private long[] cargo = new long[3];
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
		for (int i=0;i<ships.length;++i)
			this.ships[i] = ships[i];
		this.cargo[0] = cargo[0];
		this.cargo[1] = cargo[1];
		this.cargo[2] = cargo[2];
		this.fleetHandler = fleetHandler;
		//TODO
		/*** Delete Ships on mission from Planets_General ***/
		System.out.println("Fleettask mit type "+type);
		for(int i : this.ships)
			System.out.println("Ship - const: "+ i);
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
		System.out.println("Kolonisieren gestartet");
		fleetHandler.kolo((int)cargo[0], (int)cargo[1], (int)cargo[2], false);
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
			
			System.out.println("pId:"+targetPlanet+"m: "+pg.getMetal()+" c: "+pg.getCrystal()+" d: "+pg.getDeut());
			System.out.println("m: "+cargo[0]+" c: "+cargo[1]+" d: "+cargo[2]);
			
			pg.setMetal(pg.getMetal()+cargo[0]);
			pg.setCrystal(pg.getCrystal()+cargo[1]);
			pg.setDeut(pg.getDeut()+cargo[2]);
				
			
			System.out.println("After: pId:"+targetPlanet+"m: "+pg.getMetal()+" c: "+pg.getCrystal()+" d: "+pg.getDeut());					
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
			
			
			query = em.createQuery("select k from Planets_General k where k.planetId = :planetId");
			query.setParameter("planetId", planet);
			
			Object res2 = query.getSingleResult();
			Planets_General pg_self = (Planets_General)res2;
			
			query = em.createQuery("select k from User k where k.userID = :userId");
			query.setParameter("userId", pg_self.getUserId());
			
			Object res3 = query.getSingleResult();
			User user_self = (User)res3;
			
			query = em.createQuery("select k from User k where k.userID = :userId");
			query.setParameter("userId", pg.getUserId());
			
			Object res4 = query.getSingleResult();
			User user_other = (User)res4;
			
			
			
			String subject = "Transport zu " +pg.getName();
			String fromUser = "Flottenadmiral";
			String toUser = user_self.getUsername();
			
			String msg = "Unsere Flotte hat den Planeten "+pg.getName()+" beliefert.</br>"
					+ "Folgende Rohstoffe wurden entladen:</br>"
					+ "<table id=\"buildings\">"
					+"<tr>"
					+"<td><img width=\"40\" height=\"40\" src=\"resources/images/res_icon/metal.png\" alt=\"Res\"></td>"
					+"<td><img width=\"40\" height=\"40\" src=\"resources/images/res_icon/crystal.png\" alt=\"Res\"></td>"
					+"<td><img width=\"40\" height=\"40\" src=\"resources/images/res_icon/deuterium.png\" alt=\"Res\"></td>"
					+"</tr>"
					+"<tr>"
					+"<td>"+cargo[0]+"</td>"
					+"<td>"+cargo[1]+"</td>"
					+"<td>"+cargo[2]+"</td>"
					+"</tr>"
					+ "</table></br>"
					+"Die Flotte befindet sich nun auf dem Rückweg.";
			submitMSG(fromUser,toUser,msg,subject);
			if(user_self.getUserID() != user_other.getUserID()) {
				subject = "Transport von " +pg_self.getName();
				fromUser = "Flottenadmiral";
				toUser = user_other.getUsername();
				
				msg = "Eine Flotte vom Planeten "+pg_self.getName()+" hat uns beliefert.</br>"
						+ "Folgende Rohstoffe wurden entladen:</br>"
						+ "<table id=\"buildings\">"
						+"<tr>"
						+"<td><img width=\"40\" height=\"40\" src=\"resources/images/res_icon/metal.png\" alt=\"Res\"></td>"
						+"<td><img width=\"40\" height=\"40\" src=\"resources/images/res_icon/crystal.png\" alt=\"Res\"></td>"
						+"<td><img width=\"40\" height=\"40\" src=\"resources/images/res_icon/deuterium.png\" alt=\"Res\"></td>"
						+"</tr>"
						+"<tr>"
						+"<td>"+cargo[0]+"</td>"
						+"<td>"+cargo[1]+"</td>"
						+"<td>"+cargo[2]+"</td>"
						+"</tr>"
						+ "</table></br>"
						+"Die Flotte befindet sich nun auf dem Rückweg.";
				submitMSG(fromUser,toUser,msg,subject);
			}
			cargo[0] = cargo[1] = cargo[2] = 0;
			for(int i : ships)
				System.out.println("Ship - before: "+ i);
			new FleetTask(em, utx, 3, new Date(System.currentTimeMillis()+duration*1000), duration, targetPlanet, planet, ships, cargo, fleetHandler);
			for(int i : ships)
				System.out.println("Ship - After: "+ i);
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
				System.out.println("Station: pId: "+targetPlanet);
				for(int i : ships)
					System.out.println("Ship: "+ i);
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
				query = em.createQuery("select k from User k where k.userID = :userId");
				query.setParameter("userId", pg.getUserId());
				
				Object res4 = query.getSingleResult();
				User user_self = (User)res4;
				
				
				
				String subject = "Flotte erreicht "+pg.getName();
				String fromUser = "Flottenadmiral";
				String toUser = user_self.getUsername();
				
				String msg = "Unsere Flotte hat nach Ihrer Mission erfolgreich "+pg.getName()+" erreicht.</br>"
						+ "Folgende Rohstoffe wurden entladen:</br>"
						+ "<table id=\"buildings\">"
						+"<tr>"
						+"<td><img width=\"40\" height=\"40\" src=\"resources/images/res_icon/metal.png\" alt=\"Res\"></td>"
						+"<td><img width=\"40\" height=\"40\" src=\"resources/images/res_icon/crystal.png\" alt=\"Res\"></td>"
						+"<td><img width=\"40\" height=\"40\" src=\"resources/images/res_icon/deuterium.png\" alt=\"Res\"></td>"
						+"</tr>"
						+"<tr>"
						+"<td>"+cargo[0]+"</td>"
						+"<td>"+cargo[1]+"</td>"
						+"<td>"+cargo[2]+"</td>"
						+"</tr>"
						+"</table></br>"
						+"Die Flotte erwartet eure Befehle.";
				submitMSG(fromUser,toUser,msg,subject);
				cargo[0] = cargo[1] = cargo[2] = 0;
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
	private void submitMSG(String fromUser, String toUser, String msg, String subject) {
		Messages newMessage = new Messages(fromUser, toUser, msg, subject);
		try {
			utx.begin();
		} catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		em.persist(newMessage);
		try {
			utx.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fleetHandler.setMissionDone("t");
	}
}