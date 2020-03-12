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
	private int upgradeId;
	private int planet;
	private int targetPlanet;
	private int[] ships;
	private long cargoMetal;
	private long cargoCrystal;
	private long cargoDeut;
	

	
	public FleetTask(EntityManager em, UserTransaction utx, int type, Date time, int upgradeId, int planet,
			int targetPlanet, int[] ships, long cargoMetal, long cargoCrystal, long cargoDeut) {
		this.em = em;
		this.utx = utx;
		this.type = type;
		this.time = time;
		this.upgradeId = upgradeId;
		this.planet = planet;
		this.targetPlanet = targetPlanet;
		this.ships = ships;
		this.cargoMetal = cargoMetal;
		this.cargoCrystal = cargoCrystal;
		this.cargoDeut = cargoDeut;
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
		
	}

	@Override
	public void writeToDB() {
		// TODO Auto-generated method stub
		
	}

}