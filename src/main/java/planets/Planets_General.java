package planets;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

import enums.Moon;
import model.User;

@SuppressWarnings("serial")
@NamedQuery(name="SelectPlanets_General", query="Select k from Planets_General k")
@Entity
public class Planets_General implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int planetId;

	private int galaxy;
	private int solarsystem;
	private int position;
	private Moon moon;
	private long debrisFieldMetal;
	private long debrisFieldCris;
	private long debrisFieldDeut;
	private int slots;
	private int moonSlots;
	private int temperature;
	private double metal;
	private double crystal;
	private double deut;
	private int usedEnergy;
	private int maxEnergy; 
	private String name = "DingDong des Todes";
	private int userid;
	private Date lastUpdate;

	
	public Planets_General() {
		
	}
	
	public Planets_General( int galaxy, int solarsystem, int position, Moon moon, long debrisFieldMetal,
			long debrisFieldCris, long debrisFieldDeut, int slots, int moonSlots, int temperature, long metal,
			long crystal, long deut, int usedEnergy, int maxEnergy, String name, int userID) {
		this.galaxy = galaxy;
		this.solarsystem = solarsystem;
		this.position = position;
		this.moon = moon;
		this.debrisFieldMetal = debrisFieldMetal;
		this.debrisFieldCris = debrisFieldCris;
		this.debrisFieldDeut = debrisFieldDeut;
		this.slots = slots;
		this.moonSlots = moonSlots;
		this.temperature = temperature;
		this.metal = metal;
		this.crystal = crystal;
		this.deut = deut;
		this.usedEnergy = usedEnergy;
		this.maxEnergy = maxEnergy;
		this.name = name;
		this.userid = userID;
		this.setLastUpdate(new Date(System.currentTimeMillis()));
	}
	
	public int getPlanetId() {
		return planetId;
	}

	public void setPlanetId(int planetId) {
		this.planetId = planetId;
	}

	public int getGalaxy() {
		return galaxy;
	}

	public void setGalaxy(int galaxy) {
		this.galaxy = galaxy;
	}

	public int getSolarsystem() {
		return solarsystem;
	}

	public void setSolarsystem(int solarsystem) {
		this.solarsystem = solarsystem;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Moon getMoon() {
		return moon;
	}

	public void setMoon(Moon moon) {
		this.moon = moon;
	}

	public long getDebrisFieldMetal() {
		return debrisFieldMetal;
	}

	public void setDebrisFieldMetal(Long debrisFieldMetal) {
		this.debrisFieldMetal = debrisFieldMetal;
	}

	public long getDebrisFieldCris() {
		return debrisFieldCris;
	}

	public void setDebrisFieldCris(Long debrisFieldCris) {
		this.debrisFieldCris = debrisFieldCris;
	}

	public long getDebrisFieldDeut() {
		return debrisFieldDeut;
	}

	public void setDebrisFieldDeut(Long debrisFieldDeut) {
		this.debrisFieldDeut = debrisFieldDeut;
	}

	public int getSlots() {
		return slots;
	}

	public void setSlots(int slots) {
		this.slots = slots;
	}

	public int getMoonSlots() {
		return moonSlots;
	}

	public void setMoonSlots(int moonSlots) {
		this.moonSlots = moonSlots;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public double getMetal() {
		return metal;
	}

	public void setMetal(double metal) {
		this.metal = metal;
	}

	public double getCrystal() {
		return crystal;
	}

	public void setCrystal(double cris) {
		this.crystal = cris;
	}

	public double getDeut() {
		return deut;
	}

	public void setDeut(double deut) {
		this.deut = deut;
	}

	public int getUsedEnergy() {
		return usedEnergy;
	}

	public void setUsedEnergy(int usedEnergy) {
		this.usedEnergy = usedEnergy;
	}
	
	public int getMaxEnergy() {
		return maxEnergy;
	}

	public void setMaxEnergy(int maxEnergy) {
		this.maxEnergy = maxEnergy;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getUserId() {
		return userid;
	}

	public void setUserId(int userid) {
		this.userid = userid;
	}
	
	public int getMetalAsInt() {
		return (int) metal;
	}
	
	public int getCrystalAsInt() {
		return (int) crystal;
	}
	
	public int getDeutAsInt() {
		return (int) deut;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
}
