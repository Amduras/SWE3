package model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@SuppressWarnings("serial")
@NamedQuery(name="SelectBuildings", query="Select k from Buildings k")
@Entity
public class Buildings implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int buildingsId;
	
	private int metalCost;
	private int crisCost;
	private int deuCost;
	private Long time;
	private int max;
	
	public Buildings() {
		
	}

	public int getBuildingsId() {
		return buildingsId;
	}

	public void setBuildingsId(int buildingsId) {
		this.buildingsId = buildingsId;
	}

	public int getMetalCost() {
		return metalCost;
	}

	public void setMetalCost(int metalCost) {
		this.metalCost = metalCost;
	}

	public int getCrisCost() {
		return crisCost;
	}

	public void setCrisCost(int crisCost) {
		this.crisCost = crisCost;
	}

	public int getDeuCost() {
		return deuCost;
	}

	public void setDeuCost(int deuCost) {
		this.deuCost = deuCost;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
	
	
}
