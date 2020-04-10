package planets;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import Task.BuildTask;

@SuppressWarnings("serial")
@NamedQuery(name="SelectPlanets_Research", query="Select k from Planets_Research k")
@Entity
public class Planets_Research implements Serializable {

	@Id
	private int planetId;

	private int energy;
	private int laser;
	private int ion;
	private int hyperspacetech;
	private int plasma;
	private int combustion;
	private int impulse;
	private int hyperspace;
	private int espionage;
	private int computer;
	private int astrophysics;
	private int gravitation;
	private int armor;
	private int weapon;
	private int shield;
	
	private int task = -1;
	
	public Planets_Research() {
	
	}

	public Planets_Research(int planetId, int energy, int laser, int ion, int hyperspacetech, int plasma,
			int combustion, int impulse, int hyperspace, int espionage, int computer, int astrophysics, int gravitation,
			int armor, int weapon, int shield) {
		this.planetId = planetId;
		this.energy = energy;
		this.laser = laser;
		this.ion = ion;
		this.hyperspacetech = hyperspacetech;
		this.plasma = plasma;
		this.combustion = combustion;
		this.impulse = impulse;
		this.hyperspace = hyperspace;
		this.espionage = espionage;
		this.computer = computer;
		this.astrophysics = astrophysics;
		this.gravitation = gravitation;
		this.armor = armor;
		this.weapon = weapon;
		this.shield = shield;
	}





	public int getPlanetId() {
		return planetId;
	}

	public void setPlanetId(int planetId) {
		this.planetId = planetId;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public int getLaser() {
		return laser;
	}

	public void setLaser(int laser) {
		this.laser = laser;
	}

	public int getIon() {
		return ion;
	}

	public void setIon(int ion) {
		this.ion = ion;
	}

	public int getPlasma() {
		return plasma;
	}

	public void setPlasma(int plasma) {
		this.plasma = plasma;
	}

	public int getCombustion() {
		return combustion;
	}

	public void setCombustion(int combustion) {
		this.combustion = combustion;
	}

	public int getImpulse() {
		return impulse;
	}

	public void setImpulse(int impulse) {
		this.impulse = impulse;
	}

	public int getHyperspace() {
		return hyperspace;
	}

	public void setHyperspace(int hyperspace) {
		this.hyperspace = hyperspace;
	}

	public int getEspionage() {
		return espionage;
	}

	public void setEspionage(int espionage) {
		this.espionage = espionage;
	}

	public int getComputer() {
		return computer;
	}

	public void setComputer(int computer) {
		this.computer = computer;
	}

	public int getAstrophysics() {
		return astrophysics;
	}

	public void setAstrophysics(int astrophysics) {
		this.astrophysics = astrophysics;
	}

	public int getGravitation() {
		return gravitation;
	}

	public void setGravitation(int gravitation) {
		this.gravitation = gravitation;
	}

	public int getArmor() {
		return armor;
	}

	public void setArmor(int armour) {
		this.armor = armour;
	}

	public int getWeapon() {
		return weapon;
	}

	public void setWeapon(int weapon) {
		this.weapon = weapon;
	}

	public int getShield() {
		return shield;
	}

	public void setShield(int shield) {
		this.shield = shield;
	}

	public int getHyperspacetech() {
		return hyperspacetech;
	}

	public void setHyperspacetech(int hyperspacetech) {
		this.hyperspacetech = hyperspacetech;
	}

	public int getTask() {
		return task;
	}

	public void setTask(int task) {
		this.task = task;
	}


}
