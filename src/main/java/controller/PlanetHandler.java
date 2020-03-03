package controller;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.Query;

import org.primefaces.context.RequestContext;

import planets.Planet;

@ManagedBean(name="planetHandler")
@SessionScoped
public class PlanetHandler {
	private Planet activePlanet;
	
	public PlanetHandler() {
		UserHandler
	}

	public static void updateRes() {
		
		RequestContext.getCurrentInstance().update("res_table");
	}
}