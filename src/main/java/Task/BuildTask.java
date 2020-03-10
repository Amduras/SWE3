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

import controller.PlanetHandler;
import controller.QHandler;
import planets.Planets_Buildings;

public class BuildTask implements Task, Serializable{
	
	
	
	private EntityManager em;
	
	private UserTransaction utx;
	
	private static final long serialVersionUID = 1L;
	//private JobType type = JobType.BUILD_DEF;
	private int type;
	private Date time;
	//private int time = 40;
	private int upgradeId;
	private int player;
	private int planet;
	
	public BuildTask(int type, Date time, int upgradeId, int player, int planet, EntityManager em, UserTransaction utx) {
		this.type = type;
		this.time = time;
		this.upgradeId = upgradeId;
		this.player = player;
		this.planet = planet;
		this.em = em;
		this.utx = utx;
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
		System.out.println("---------------------------> EX BT " + type);
		Query query = em.createQuery("select k from Planets_Buildings k where k.planetId = :id");
		query.setParameter("id", planet);
		try {
			Object res = query.getSingleResult();
			Planets_Buildings b = (Planets_Buildings)res;
			b.setSolarPlant(b.getSolarPlant()+1);
			b.setTask(null);
			try {
				utx.begin();
			} catch (NotSupportedException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			em.merge(b);
			try {
				utx.commit();
			} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
					| HeuristicRollbackException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch(NoResultException e){	
			System.out.println("Keine Werte in DB");
		}	
	}
	

	@Override
	public void writeToDB() {
		// TODO Auto-generated method stub
		
	}

}
