package model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@SuppressWarnings("serial")
@NamedQuery(name="SelectWorldSettings", query="Select k from WorldSettings k")
@Entity
public class WorldSettings implements Serializable{
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String name;
	private int gameSpeed;
	private int fleetSpeed;
	private int startingPlanetSize;
	private double fleetToDebrisFieldRatio;
	private double defToDebrisFieldRatio;
	private double jumpgateCooldown;
	private int protection;
	
	public WorldSettings() {
		
	}
	
	
	
	public WorldSettings(String name, int gameSpeed, int fleetSpeed, int startingPlanetSize,
			double fleetToDebrisFieldRatio, double defToDebrisFieldRatio, double jumpgateCooldown, int protection) {
		this.name = name;
		this.gameSpeed = gameSpeed;
		this.fleetSpeed = fleetSpeed;
		this.startingPlanetSize = startingPlanetSize;
		this.fleetToDebrisFieldRatio = fleetToDebrisFieldRatio;
		this.defToDebrisFieldRatio = defToDebrisFieldRatio;
		this.jumpgateCooldown = jumpgateCooldown;
		this.protection = protection;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
		System.out.println("gamespeed gesetzt: "+gameSpeed);
		this.gameSpeed = gameSpeed;
	}

	public int getFleetSpeed() {
		return fleetSpeed;
	}

	public void setFleetSpeed(int fleetSpeed) {
		System.out.println("Fleetspeed gesetzt: "+fleetSpeed);
		this.fleetSpeed = fleetSpeed;
	}

	public int getStartingPlanetSize() {
		return startingPlanetSize;
	}

	public void setStartingPlanetSize(int startingPlanetSize) {
		System.out.println("Planetgröße gesetzt: "+startingPlanetSize);
		this.startingPlanetSize = startingPlanetSize;
	}

	public double getFleetToDebrisFieldRatio() {
		return fleetToDebrisFieldRatio;
	}

	public void setFleetToDebrisFieldRatio(double fleetToDebrisFieldRatio) {
		System.out.println("fleetfield gesetzt: "+fleetToDebrisFieldRatio);
		this.fleetToDebrisFieldRatio = fleetToDebrisFieldRatio;
	}

	public double getDefToDebrisFieldRatio() {
		return defToDebrisFieldRatio;
	}

	public void setDefToDebrisFieldRatio(double defToDebrisFieldRatio) {
		System.out.println("deffield gesetzt: "+defToDebrisFieldRatio);
		this.defToDebrisFieldRatio = defToDebrisFieldRatio;
	}

	public double getJumpgateCooldown() {
		return jumpgateCooldown;
	}

	public void setJumpgateCooldown(double jumpgateCooldown) {
		System.out.println("Jumpgate gesetzt: "+jumpgateCooldown);
		this.jumpgateCooldown = jumpgateCooldown;
	}

	public int getProtection() {
		return protection;
	}

	public void setProtection(int protection) {
		System.out.println("Protection gesetzt: "+protection);
		this.protection = protection;
	}
	
	
	
}
