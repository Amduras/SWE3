package planets;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@SuppressWarnings("serial")
@NamedQuery(name="SelectPlanets_Def", query="Select k from Planets_Def k")
@Entity
public class Planets_Def implements Serializable {
	
	@Id
	private int planetId;
	
	

	private int rocketLauncher;
	private int lightLaser;
	private int heavyLaser;
	private int gaussCannon;
	private int ionCannon;	
	private int plasmaTurret;
	private int smallShieldDome;
	private int largeShieldDome;
	private int antiBallisticMissle;
	private int interplanetaryMissle;
	
	private Date qTime = new Date(0);
	private int task = -1;

	public Planets_Def() {
		
	}
	
	public Planets_Def(int planetId, int rocketLauncher, int lightLaser, int heavyLaser, int gaussCannon, int ionCannon,
			int plasmaTurret, int smallShieldDome, int largeShieldDome, int antiBallisticMissle,
			int interplanetaryMissle) {
		this.planetId = planetId;
		this.rocketLauncher = rocketLauncher;
		this.lightLaser = lightLaser;
		this.heavyLaser = heavyLaser;
		this.gaussCannon = gaussCannon;
		this.ionCannon = ionCannon;
		this.plasmaTurret = plasmaTurret;
		this.smallShieldDome = smallShieldDome;
		this.largeShieldDome = largeShieldDome;
		this.antiBallisticMissle = antiBallisticMissle;
		this.interplanetaryMissle = interplanetaryMissle;
	}
	
	public int getTask() {
		return task;
	}

	public void setTask(int task) {
		this.task = task;
	}
	public int getPlanetId() {
		return planetId;
	}

	public void setPlanetId(int planetId) {
		this.planetId = planetId;
	}

	public int getRocketLauncher() {
		return rocketLauncher;
	}

	public void setRocketLauncher(int rocketLauncher) {
		this.rocketLauncher = rocketLauncher;
	}

	public int getLightLaser() {
		return lightLaser;
	}

	public void setLightLaser(int lightLaser) {
		this.lightLaser = lightLaser;
	}

	public int getHeavyLaser() {
		return heavyLaser;
	}

	public void setHeavyLaser(int heavyLaser) {
		this.heavyLaser = heavyLaser;
	}

	public int getIonCannon() {
		return ionCannon;
	}

	public void setIonCannon(int ionCannon) {
		this.ionCannon = ionCannon;
	}

	public int getGaussCannon() {
		return gaussCannon;
	}

	public void setGaussCannon(int gaussCannon) {
		this.gaussCannon = gaussCannon;
	}

	public int getPlasmaTurret() {
		return plasmaTurret;
	}

	public void setPlasmaTurret(int plasmaTurret) {
		this.plasmaTurret = plasmaTurret;
	}

	public int getSmallShieldDome() {
		return smallShieldDome;
	}

	public void setSmallShieldDome(int smallShieldDome) {
		this.smallShieldDome = smallShieldDome;
	}

	public int getLargeShieldDome() {
		return largeShieldDome;
	}

	public void setLargeShieldDome(int largeShieldDome) {
		this.largeShieldDome = largeShieldDome;
	}

	public int getAntiBallisticMissle() {
		return antiBallisticMissle;
	}

	public void setAntiBallisticMissle(int antiBallisticMissle) {
		this.antiBallisticMissle = antiBallisticMissle;
	}

	public int getInterplanetaryMissle() {
		return interplanetaryMissle;
	}

	public void setInterplanetaryMissle(int interplanetaryMissle) {
		this.interplanetaryMissle = interplanetaryMissle;
	}

//	public List<BuildTask> getTask() {
//		return task;
//	}
//	public void addTask(BuildTask t) {
//		task.add(t);
//	}
//	public synchronized void removeTask(Date time) {
//		synchronized(task) {
//			Iterator<BuildTask> it = task.iterator();
//			
//			while(it.hasNext()) {
//				BuildTask b = it.next();
//				if(b.getTime() == time) {
//					it.remove();
//					return;
//				}				
//			}
//		}		
//	}
//	public void setTask(List<BuildTask> task) {
//		this.task = task;
//	}

	public Date getqTime() {
		return qTime;
	}

	public void setqTime(Date qTime) {
		this.qTime = qTime;
	}
	
	
}
