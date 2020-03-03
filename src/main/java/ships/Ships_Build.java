package ships;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@SuppressWarnings("serial")
@NamedQuery(name="SelectShipsBuild", query="Select k from Ships_Build k")
@Entity
public class Ships_Build implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int shipId;
	
	private int metalCost;
	private int crisCost;
	private int deutCost;
	private Long time;
	
	public Ships_Build() {
		
	}

	

	public int getShipId() {
		return shipId;
	}



	public void setShipId(int shipId) {
		this.shipId = shipId;
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

	public int getDeutCost() {
		return deutCost;
	}

	public void setDeutCost(int deutCost) {
		this.deutCost = deutCost;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}
	
	
	
	
}
