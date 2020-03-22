package model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@SuppressWarnings("serial")
@NamedQuery(name="SelectShip", query="Select k from Ship k")
@Entity
public class Ship implements Serializable{

	@Id
	private int shipId;
	
	private int hull;
	private int shield;
	private int attack;
	private int cargoSpace;
	private long speed;
	private int consumption;
	private int driveScaleTechId;
	private int[] rapidFire;
		
	public Ship(int shipId, int hull, int shield, int attack, int cargoSpace, long speed, int consumption, int driveScaleTechId, int[] rapidFire) {
		this.shipId = shipId;
		this.hull = hull;
		this.shield = shield;
		this.attack = attack;
		this.cargoSpace = cargoSpace;
		this.speed = speed;
		this.consumption = consumption;
		this.driveScaleTechId = driveScaleTechId;
		this.rapidFire = rapidFire;
	}
	public Ship() {
		
	}
	public int getShipId() {
		return shipId;
	}
	public void setShipId(int shipId) {
		this.shipId = shipId;
	}
	public int getHull() {
		return hull;
	}
	public void setHull(int hull) {
		this.hull = hull;
	}
	public int getShield() {
		return shield;
	}
	public void setShield(int shield) {
		this.shield = shield;
	}
	public int getAttack() {
		return attack;
	}
	public void setAttack(int attack) {
		this.attack = attack;
	}
	public int getCargoSpace() {
		return cargoSpace;
	}
	public void setCargoSpace(int cargoSpace) {
		this.cargoSpace = cargoSpace;
	}
	public long getSpeed() {
		return speed;
	}
	public void setSpeed(long speed) {
		this.speed = speed;
	}
	public int getConsumption() {
		return consumption;
	}
	public void setConsumption(int consumption) {
		this.consumption = consumption;
	}
	public int getDriveScaleTechId() {
		return driveScaleTechId;
	}
	public void setDriveScaleTechId(int driveScaleTechId) {
		this.driveScaleTechId = driveScaleTechId;
	}

	public int[] getRapidFire() {
		return rapidFire;
	}

	public void setRapidFire(int[] rapidFire) {
		this.rapidFire = rapidFire;
	}
}
