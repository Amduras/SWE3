package planets;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@SuppressWarnings("serial")
@NamedQuery(name="SelectPlanets_Buildings", query="Select k from Planets_Buildings k")
@Entity
public class Planets_Buildings implements Serializable {

	@Id
	private int planetId;
	
	private int metalMine;
	private int crystalMine;
	private int deutSyn;
	private int solarPlant;
	private int fusionReactor;
	private int metalStorage;
	private int crystalStorage;
	private int deutTank;
	private int metallHideout;
	private int crystalHideout;
	private int deutHideout;
	private int roboticFactory;
	private int shipyard;
	private int researchlab;
	private int alliancedepot;
	private int missleSilo;
	private int naniteFactory;
	private int terraformer;
	private int spaceDock;
	private int lunarBase;
	private int sensorPhalanx;
	private int jumpgate;
	
	
	
	public Planets_Buildings() {
		
	}

	public Planets_Buildings(int planetId, int metalMine, int crystalMine, int deutSyn, int solarPlant,
			int fusionReactor, int metalStorage, int crystalStorage, int deutTank, int metallHideout,
			int crystalHideout, int deutHideout, int roboticFactory, int shipyard, int researchlab, int alliancedepot,
			int missleSilo, int naniteFactory, int terraformer, int spaceDock, int lunarBase, int sensorPhalanx,
			int jumpgate) {
		this.planetId = planetId;
		this.metalMine = metalMine;
		this.crystalMine = crystalMine;
		this.deutSyn = deutSyn;
		this.solarPlant = solarPlant;
		this.fusionReactor = fusionReactor;
		this.metalStorage = metalStorage;
		this.crystalStorage = crystalStorage;
		this.deutTank = deutTank;
		this.metallHideout = metallHideout;
		this.crystalHideout = crystalHideout;
		this.deutHideout = deutHideout;
		this.roboticFactory = roboticFactory;
		this.shipyard = shipyard;
		this.researchlab = researchlab;
		this.alliancedepot = alliancedepot;
		this.missleSilo = missleSilo;
		this.naniteFactory = naniteFactory;
		this.terraformer = terraformer;
		this.spaceDock = spaceDock;
		this.lunarBase = lunarBase;
		this.sensorPhalanx = sensorPhalanx;
		this.jumpgate = jumpgate;
	}

	public int getPlanetId() {
		return planetId;
	}

	public void setPlanetId(int planetId) {
		this.planetId = planetId;
	}

	public int getMetalMine() {
		return metalMine;
	}

	public void setMetalMine(int metalMine) {
		this.metalMine = metalMine;
	}

	public int getCrystalMine() {
		return crystalMine;
	}

	public void setCrystalMine(int crisMine) {
		this.crystalMine = crisMine;
	}

	public int getDeutSyn() {
		return deutSyn;
	}

	public void setDeutSyn(int deutSyn) {
		this.deutSyn = deutSyn;
	}

	public int getSolarPlant() {
		return solarPlant;
	}

	public void setSolarPlant(int solarPlant) {
		this.solarPlant = solarPlant;
	}

	public int getFusionReactor() {
		return fusionReactor;
	}

	public void setFusionReactor(int fusionReactor) {
		this.fusionReactor = fusionReactor;
	}

	public int getMetalStorage() {
		return metalStorage;
	}

	public void setMetalStorage(int metalStorage) {
		this.metalStorage = metalStorage;
	}

	public int getCrystalStorage() {
		return crystalStorage;
	}

	public void setCrystalStorage(int crisStorage) {
		this.crystalStorage = crisStorage;
	}

	public int getDeutTank() {
		return deutTank;
	}

	public void setDeutTank(int deutTank) {
		this.deutTank = deutTank;
	}

	public int getMetallHideout() {
		return metallHideout;
	}

	public void setMetallHideout(int metallHideout) {
		this.metallHideout = metallHideout;
	}

	public int getCrystalHideout() {
		return crystalHideout;
	}

	public void setCrystalHideout(int crisHideout) {
		this.crystalHideout = crisHideout;
	}

	public int getDeutHideout() {
		return deutHideout;
	}

	public void setDeutHideout(int deutHideout) {
		this.deutHideout = deutHideout;
	}

	public int getRoboticFactory() {
		return roboticFactory;
	}

	public void setRoboticFactory(int roboticFactory) {
		this.roboticFactory = roboticFactory;
	}

	public int getShipyard() {
		return shipyard;
	}

	public void setShipyard(int shipyard) {
		this.shipyard = shipyard;
	}

	public int getResearchlab() {
		return researchlab;
	}

	public void setResearchlab(int researchlab) {
		this.researchlab = researchlab;
	}

	public int getAlliancedepot() {
		return alliancedepot;
	}

	public void setAlliancedepot(int alliancedepot) {
		this.alliancedepot = alliancedepot;
	}

	public int getMissleSilo() {
		return missleSilo;
	}

	public void setMissleSilo(int missleSilo) {
		this.missleSilo = missleSilo;
	}

	public int getNaniteFactory() {
		return naniteFactory;
	}

	public void setNaniteFactory(int naniteFactory) {
		this.naniteFactory = naniteFactory;
	}

	public int getTerraformer() {
		return terraformer;
	}

	public void setTerraformer(int terraformer) {
		this.terraformer = terraformer;
	}

	public int getSpaceDock() {
		return spaceDock;
	}

	public void setSpaceDock(int spaceDock) {
		this.spaceDock = spaceDock;
	}

	public int getLunarBase() {
		return lunarBase;
	}

	public void setLunarBase(int lunarBase) {
		this.lunarBase = lunarBase;
	}

	public int getSensorPhalanx() {
		return sensorPhalanx;
	}

	public void setSensorPhalanx(int sensorPhalanx) {
		this.sensorPhalanx = sensorPhalanx;
	}

	public int getJumpgate() {
		return jumpgate;
	}

	public void setJumpgate(int jumpgate) {
		this.jumpgate = jumpgate;
	}
	
	
}
