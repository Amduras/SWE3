package model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;


@SuppressWarnings("serial")
@NamedQuery(name="SelectBuildable", query="Select k from Buildable k")
@Entity
public class Buildable implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String name;
	private int type;
	private int baseCostMetal;
	private int baseCostCrystal;
	private int baseCostDeut;
	private int baseCostEnergy;
	private double resFactor;
	private double energyFactor;
	private String descr;
	private String rec;
	
	public Buildable(String name, int type, int baseCostMetal, int baseCostCrystal, int baseCostDeut, int baseCostEnergy, double resFactor, double energyFactor, String descr, String rec) {
		super();
		this.name = name;
		this.type = type;
		this.baseCostMetal = baseCostMetal;
		this.baseCostCrystal = baseCostCrystal;
		this.baseCostDeut = baseCostDeut;
		this.baseCostEnergy = baseCostEnergy;
		this.resFactor = resFactor;
		this.energyFactor = energyFactor;
		this.descr = descr;
		this.rec = rec;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getBaseCostMetal() {
		return baseCostMetal;
	}
	public void setBaseCost(int baseCost) {
		this.baseCostMetal = baseCost;
	}
	public double getResFactor() {
		return resFactor;
	}
	public void setResFactor(double resFactor) {
		this.resFactor = resFactor;
	}
	public double getEnergyFactor() {
		return energyFactor;
	}
	public void setEnergyFactor(double energyFactor) {
		this.energyFactor = energyFactor;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public String getRec() {
		return rec;
	}
	public void setRec(String rec) {
		this.rec = rec;
	}

	public int getBaseCostCrystal() {
		return baseCostCrystal;
	}

	public void setBaseCostCrystal(int baseCostCrystal) {
		this.baseCostCrystal = baseCostCrystal;
	}

	public int getBaseCostDeut() {
		return baseCostDeut;
	}

	public void setBaseCostDeut(int baseCostDeut) {
		this.baseCostDeut = baseCostDeut;
	}

	public int getBaseCostEnergy() {
		return baseCostEnergy;
	}

	public void setBaseCostEnergy(int baseCostEnergy) {
		this.baseCostEnergy = baseCostEnergy;
	}

}
