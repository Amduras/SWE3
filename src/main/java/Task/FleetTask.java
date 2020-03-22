package Task;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import controller.QHandler;

public class FleetTask implements Task, Serializable{
	
	private EntityManager em;	
	private UserTransaction utx;
	
	private static final long serialVersionUID = 1L;
	// 0-> kolo		1->tf	2-> transport	3-> stationieren	4-> spio	5-> angriff
	private int type;
	private Date time;
	private long duration;
	private boolean mustReturn;
	private int planet;
	private int targetPlanet;
	private int[] ships;
	private long cargoMetal;
	private long cargoCrystal;
	private long cargoDeut;
	

	
	public FleetTask(EntityManager em, UserTransaction utx, int type, Date time, long duration, boolean mustReturn, int planet,
			int targetPlanet, int[] ships, long cargoMetal, long cargoCrystal, long cargoDeut) {
		this.em = em;
		this.utx = utx;
		this.type = type;
		this.time = time;
		this.duration = duration;
		this.mustReturn = mustReturn;
		this.planet = planet;
		this.targetPlanet = targetPlanet;
		this.ships = ships;
		this.cargoMetal = cargoMetal;
		this.cargoCrystal = cargoCrystal;
		this.cargoDeut = cargoDeut;
		
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
		
	}
	@Override
	public void writeToDB() {
		// TODO Auto-generated method stub
		
	}

}