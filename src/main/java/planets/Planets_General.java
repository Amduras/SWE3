package planets;

import java.io.Serializable;

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
	private Long debrisFieldMetal;
	private Long debrisFieldCris;
	private Long debrisFieldDeut;
	private int slots;
	private int moonSlots;
	private int temperature;
	private int metal;
	private int cris;
	private int deut;
	private int energy;
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="user_id", nullable=false)
	private User user;
	
	public Planets_General() {
		
	}

	

	public Planets_General(int metal, int cris, int deut, int energy, String name) {
		this.metal = metal;
		this.cris = cris;
		this.deut = deut;
		this.energy = energy;
		this.name = name;
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

	public Long getDebrisFieldMetal() {
		return debrisFieldMetal;
	}

	public void setDebrisFieldMetal(Long debrisFieldMetal) {
		this.debrisFieldMetal = debrisFieldMetal;
	}

	public Long getDebrisFieldCris() {
		return debrisFieldCris;
	}

	public void setDebrisFieldCris(Long debrisFieldCris) {
		this.debrisFieldCris = debrisFieldCris;
	}

	public Long getDebrisFieldDeut() {
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

	public int getMetal() {
		return metal;
	}

	public void setMetal(int metal) {
		this.metal = metal;
	}

	public int getCris() {
		return cris;
	}

	public void setCris(int cris) {
		this.cris = cris;
	}

	public int getDeut() {
		return deut;
	}

	public void setDeut(int deut) {
		this.deut = deut;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
