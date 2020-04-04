package model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Galaxy implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private int galaxyId;
	private int maxSystems = 100;
	
	public Galaxy() {

	}
	
	public int getGalaxyId() {
		return galaxyId;
	}

	public void setGalaxyId(int galaxyId) {
		this.galaxyId = galaxyId;
	}

	public int getMaxSystems() {
		return maxSystems;
	}

	public void setMaxSystems(int maxSystems) {
		this.maxSystems = maxSystems;
	}
	
	
	
}
