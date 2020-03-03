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
	private int crisMine;
	private int deutSyn;
	private int solarPlant;
	private int fusionReactor;
	private int metalStorage;
	private int crisStorage;
	private int deutTank;
	private int metallHideout;
	private int crisHideout;
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
	
	@OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Planets_General planet;
	
	public Planets_Buildings() {
		
	}

	public Planets_General getPlanet() {
		return planet;
	}

	public void setPlanet(Planets_General planet) {
		this.planet = planet;
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

	public int getCrisMine() {
		return crisMine;
	}

	public void setCrisMine(int crisMine) {
		this.crisMine = crisMine;
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

	public int getCrisStorage() {
		return crisStorage;
	}

	public void setCrisStorage(int crisStorage) {
		this.crisStorage = crisStorage;
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

	public int getCrisHideout() {
		return crisHideout;
	}

	public void setCrisHideout(int crisHideout) {
		this.crisHideout = crisHideout;
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
