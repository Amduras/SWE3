package Task;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import controller.QHandler;
import model.Fight;

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
	

	
	public FleetTask(EntityManager em, UserTransaction utx, int type, Date time, long duration, int planet,
			int targetPlanet, int[] ships, long[] cargo) {
		this.em = em;
		this.utx = utx;
		this.type = type;
		this.time = time;
		this.duration = duration;
		this.planet = planet;
		this.targetPlanet = targetPlanet;
		this.ships = ships;
		this.cargo = cargo;
		//TODO
		/*** Delete Ships on mission from Planets_General ***/
		
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
		
	}
	private void tf() {
		
	}
	private void transport() {
		
	}
	private void station() {
		
	}
	private void espionage() {
		
	}
	private void attack() {
		@SuppressWarnings("unused")
		Fight fight = new Fight(planet, ships, cargo, targetPlanet, em, utx);
		int res = 0;
		for(int i=0;i<ships.length;++i) {
			res += ships[i];
		}
		if(res != 0)
			new FleetTask(em, utx, 3, new Date(System.currentTimeMillis()+duration), duration, targetPlanet, planet, ships, cargo);
	}
	@Override
	public void writeToDB() {
		// TODO Auto-generated method stub
		
	}

}