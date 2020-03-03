package model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@SuppressWarnings("serial")
@NamedQuery(name="SelectWorldSettings", query="Select k from WorldSettings k")
@Entity
public class WorldSettings implements Serializable{
	

	@Id
	private String name;
	private int gameSpeed;
	private int fleetSpeed;
	private int startingPlanetSize;
	private int fleetToDebrisFieldRatio;
	private int defToDebrisFieldRatio;
	private int jumpgateCooldown;
	private int protection;
	
	public WorldSettings() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGameSpeed() {
		return gameSpeed;
	}

	public void setGameSpeed(int gameSpeed) {
		this.gameSpeed = gameSpeed;
	}

	public int getFleetSpeed() {
		return fleetSpeed;
	}

	public void setFleetSpeed(int fleetSpeed) {
		this.fleetSpeed = fleetSpeed;
	}

	public int getStartingPlanetSize() {
		return startingPlanetSize;
	}

	public void setStartingPlanetSize(int startingPlanetSize) {
		this.startingPlanetSize = startingPlanetSize;
	}

	public int getFleetToDebrisFieldRatio() {
		return fleetToDebrisFieldRatio;
	}

	public void setFleetToDebrisFieldRatio(int fleetToDebrisFieldRatio) {
		this.fleetToDebrisFieldRatio = fleetToDebrisFieldRatio;
	}

	public int getDefToDebrisFieldRatio() {
		return defToDebrisFieldRatio;
	}

	public void setDefToDebrisFieldRatio(int defToDebrisFieldRatio) {
		this.defToDebrisFieldRatio = defToDebrisFieldRatio;
	}

	public int getJumpgateCooldown() {
		return jumpgateCooldown;
	}

	public void setJumpgateCooldown(int jumpgateCooldown) {
		this.jumpgateCooldown = jumpgateCooldown;
	}

	public int getProtection() {
		return protection;
	}

	public void setProtection(int protection) {
		this.protection = protection;
	}
	
	
	
}
