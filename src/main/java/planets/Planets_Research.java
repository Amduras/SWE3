package planets;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@SuppressWarnings("serial")
@NamedQuery(name="SelectPlanets_Research", query="Select k from Planets_Research k")
@Entity
public class Planets_Research implements Serializable {
	
	@Id
	private int planetsId;
	


	private int energy;
	private int laser;
	private int ion;
	private int plasma;
	private int combustion;
	private int impulse;
	private int hyperspace;
	private int espionage;
	private int computer;
	private int astrophysics;
	private int gravitation;
	private int armour;
	private int weapon;
	private int shield;
	
	@OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Planets_General planet;
	
	public Planets_Research(int planetsId, int energy, int laser, int ion, int plasma, int combustion, int impulse,
			int hyperspace, int espionage, int computer, int astrophysics, int gravitation, int armour, int weapon,
			int shield) {
		this.planetsId = planetsId;
		this.energy = energy;
		this.laser = laser;
		this.ion = ion;
		this.plasma = plasma;
		this.combustion = combustion;
		this.impulse = impulse;
		this.hyperspace = hyperspace;
		this.espionage = espionage;
		this.computer = computer;
		this.astrophysics = astrophysics;
		this.gravitation = gravitation;
		this.armour = armour;
		this.weapon = weapon;
		this.shield = shield;
	}

	public int getPlanetsId() {
		return planetsId;
	}

	public void setPlanetsId(int planetsId) {
		this.planetsId = planetsId;
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

	public int getArmour() {
		return armour;
	}

	public void setArmour(int armour) {
		this.armour = armour;
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
	
	
}
