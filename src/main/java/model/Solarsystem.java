package model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Solarsystem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private int systemId;	
	
	private int galaxyId;
	private int planets = 15;
	private int freeStartpositions = 9;
	
	public Solarsystem() {
	}
	
	public Solarsystem(int galaxyId) {
		this.galaxyId = galaxyId;
	}
	
	public int getSystemId() {
		return systemId;
	}

	public void setSystemId(int systemId) {
		this.systemId = systemId;
	}

	public int getGalaxyId() {
		return galaxyId;
	}

	public void setGalaxyId(int galaxyId) {
		this.galaxyId = galaxyId;
	}

	public int getPlanets() {
		return planets;
	}

	public void setPlanets(int planets) {
		this.planets = planets;
	}

	public int getFreeStartpositions() {
		return freeStartpositions;
	}

	public void setFreeStartpositions(int freeStartpositions) {
		this.freeStartpositions = freeStartpositions;
	}
	
}
