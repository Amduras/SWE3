package model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@SuppressWarnings("serial")
@Entity
public class Flight implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int flightID;
	
	private int planetFromId;
	private int planetToId;
	private int fleetTaskID;
	private int fleetTaskType;
	private Date fleetTaskTime;
	
	public Flight(int planetFromId, int planetToId, int fleetTaskID, int fleetTaskType, Date fleetTaskTime) {
		super();
		this.planetFromId = planetFromId;
		this.planetToId = planetToId;
		this.fleetTaskID = fleetTaskID;
		this.fleetTaskType = fleetTaskType;
		this.fleetTaskTime = fleetTaskTime;
	}
	public Flight() {
		
	}
	public int getPlanetFromId() {
		return planetFromId;
	}
	public void setPlanetFromId(int planetFromId) {
		this.planetFromId = planetFromId;
	}
	public int getPlanetToId() {
		return planetToId;
	}
	public void setPlanetToId(int planetToId) {
		this.planetToId = planetToId;
	}
	public int getFleetTaskID() {
		return fleetTaskID;
	}
	public void setFleetTaskID(int fleetTaskID) {
		this.fleetTaskID = fleetTaskID;
	}
	public int getFleetTaskType() {
		return fleetTaskType;
	}
	public void setFleetTaskType(int fleetTaskType) {
		this.fleetTaskType = fleetTaskType;
	}
	public Date getFleetTaskTime() {
		return fleetTaskTime;
	}
	public void setFleetTaskTime(Date fleetTaskTime) {
		this.fleetTaskTime = fleetTaskTime;
	}
	
	
}
