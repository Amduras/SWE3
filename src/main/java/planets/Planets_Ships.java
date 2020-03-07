package planets;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@SuppressWarnings("serial")
@NamedQuery(name="SelectPlanets_Ships", query="Select k from Planets_Ships k")
@Entity
public class Planets_Ships implements Serializable {
	
	@Id
	private int planetId;
	


	private int lightFighter;
	private int heavyFighter;
	private int cruiser;
	private int battleship;
	private int battlecruiser;
	private int bomber;
	private int destroyer;
	private int deathStar;
	private int smallCargoShip;
	private int largeCargoShip;
	private int colonyShip;
	private int recycler;
	private int espionageProbe;
	private int solarSattlelite;
	
	@OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Planets_General planet;
	
	public Planets_Ships(int planetId, int lightFighter, int heavyFighter, int cruiser, int battleship,
			int battlecruiser, int bomber, int destroyer, int deathStar, int smallCargoShip, int largeCargoShip,
			int colonyShip, int recycler, int espionageProbe, int solarSattlelite) {
		this.planetId = planetId;
		this.lightFighter = lightFighter;
		this.heavyFighter = heavyFighter;
		this.cruiser = cruiser;
		this.battleship = battleship;
		this.battlecruiser = battlecruiser;
		this.bomber = bomber;
		this.destroyer = destroyer;
		this.deathStar = deathStar;
		this.smallCargoShip = smallCargoShip;
		this.largeCargoShip = largeCargoShip;
		this.colonyShip = colonyShip;
		this.recycler = recycler;
		this.espionageProbe = espionageProbe;
		this.solarSattlelite = solarSattlelite;
	}

	public int getPlanetId() {
		return planetId;
	}

	public void setPlanetId(int planetId) {
		this.planetId = planetId;
	}

	public int getLightFighter() {
		return lightFighter;
	}

	public void setLightFighter(int lightFighter) {
		this.lightFighter = lightFighter;
	}

	public int getHeavyFighter() {
		return heavyFighter;
	}

	public void setHeavyFighter(int heavyFighter) {
		this.heavyFighter = heavyFighter;
	}

	public int getCruiser() {
		return cruiser;
	}

	public void setCruiser(int cruiser) {
		this.cruiser = cruiser;
	}

	public int getBattleship() {
		return battleship;
	}

	public void setBattleship(int battleship) {
		this.battleship = battleship;
	}

	public int getBattlecruiser() {
		return battlecruiser;
	}

	public void setBattlecruiser(int battlecruiser) {
		this.battlecruiser = battlecruiser;
	}

	public int getBomber() {
		return bomber;
	}

	public void setBomber(int bomber) {
		this.bomber = bomber;
	}

	public int getDestroyer() {
		return destroyer;
	}

	public void setDestroyer(int destroyer) {
		this.destroyer = destroyer;
	}

	public int getDeathStar() {
		return deathStar;
	}

	public void setDeathStar(int deathStar) {
		this.deathStar = deathStar;
	}

	public int getSmallCargoShip() {
		return smallCargoShip;
	}

	public void setSmallCargoShip(int smallCargoShip) {
		this.smallCargoShip = smallCargoShip;
	}

	public int getLargeCargoShip() {
		return largeCargoShip;
	}

	public void setLargeCargoShip(int largeCargoShip) {
		this.largeCargoShip = largeCargoShip;
	}

	public int getColonyShip() {
		return colonyShip;
	}

	public void setColonyShip(int colonyShip) {
		this.colonyShip = colonyShip;
	}

	public int getRecycler() {
		return recycler;
	}

	public void setRecycler(int recycler) {
		this.recycler = recycler;
	}

	public int getEspionageProbe() {
		return espionageProbe;
	}

	public void setEspionageProbe(int espionageProbe) {
		this.espionageProbe = espionageProbe;
	}

	public int getSolarSattlelite() {
		return solarSattlelite;
	}

	public void setSolarSattlelite(int solarSattlelite) {
		this.solarSattlelite = solarSattlelite;
	}
	
	
}
