package controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIInput;
import javax.faces.event.AjaxBehaviorEvent;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

@ManagedBean(name="fleetHandler")
@SessionScoped
public class FleetHandler {

	private PlanetHandler planetHandler;
	private EntityManager em;
	private UserTransaction utx;
	
	private int[] ships = new int[13];
	
	private UIInput lf = null;
	
	public FleetHandler(PlanetHandler planetHandler, EntityManager em, UserTransaction utx) {
		super();
		this.planetHandler = planetHandler;
		this.em = em;
		this.utx = utx;
		//planetHandler.getPs().setLightFighter(50);
	}
	public void lfm(AjaxBehaviorEvent e) {
		System.out.println("hi");
		ships[0] = Integer.parseInt(lf.toString());
	}
	public void next() {
		for(int i : ships)
			System.out.print(i+" ");
		System.out.println("");
	}
	
	public int[] getShips() {
		return ships;
	}

	public void setShips(int[] ships) {
		this.ships = ships;
	}
	public UIInput getLf() {
		return lf;
	}
	public void setLf(UIInput lf) {
		this.lf = lf;
	}
}
