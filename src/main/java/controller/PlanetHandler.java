package controller;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.Query;

import org.primefaces.context.RequestContext;

import planets.Planet;
import planets.Planets_General;

@ManagedBean(name="planetHandler")
@SessionScoped
public class PlanetHandler {
	private Planets_General activePlanet;
	
	public PlanetHandler() {
		
	}

	public static void updateRes() {
		
		RequestContext.getCurrentInstance().update("res_table");
	}

	public Planets_General getActivePlanet() {
		return activePlanet;
	}

	public void setActivePlanet(Planets_General activePlanet) {
		this.activePlanet = activePlanet;
		System.out.println("Planet: "+activePlanet.getName());
	}
	
}