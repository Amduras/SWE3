package planets;

import java.io.Serializable;

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
	private int ionCannon;
	private int gaussCannon;
	private int plasmaTurret;
	private int smallShieldDome;
	private int largeShieldDome;
	private int antiBallisticMissle;
	private int interplanetaryMissle;
	
	public Planets_Def() {
		
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
	
	
}
