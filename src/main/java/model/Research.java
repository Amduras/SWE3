package model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@SuppressWarnings("serial")
@NamedQuery(name="SelectResearch", query="Select k from Research k")
@Entity
public class Research implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int researchId;
	
	private int metalCost;
	private int crisCost;
	private int deuCost;
	private int energyCost;
	private Long time;
	private int max;
	
	public Research() {
		
	}

	public int getResearchId() {
		return researchId;
	}

	public void setResearchId(int researchId) {
		this.researchId = researchId;
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

	public int getEnergyCost() {
		return energyCost;
	}

	public void setEnergyCost(int energyCost) {
		this.energyCost = energyCost;
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
