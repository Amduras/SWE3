package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import model.Flight;
import planets.Planets_General;

@SuppressWarnings("serial")
@ManagedBean(name="flightHandler")
@SessionScoped
public class FlightHandler implements Serializable{
	EntityManager em;
	UserTransaction utx;
	
	private PlanetHandler planetHandler;
	private List<Flight> flightsInc = new ArrayList<Flight>();
	private List<Flight> flightsOut = new ArrayList<Flight>();
	
	public FlightHandler() {
		
	}

	public FlightHandler(EntityManager em, UserTransaction utx) {
		this.em = em;
		this.utx = utx;
	}
	
	@SuppressWarnings("unchecked")
	public void flightList() {
		Query query = em.createQuery("select k from Flight k where k.planetFromId = :planetID");
		query.setParameter("planetID", planetHandler.getPg().getPlanetId());
		flightsOut = query.getResultList();
		query = em.createQuery("select k from Flight k where k.planetToId = :planetID");
		query.setParameter("planetID", planetHandler.getPg().getPlanetId());
		flightsInc = query.getResultList();
		
	}
	
	public long getRemainingTime(Flight flight) {
		return Math.abs(flight.getFleetTaskTime().getTime()-System.currentTimeMillis());
	}
	
	public String getPlanet(int id) {
		Query query = em.createQuery("select k from Planets_General k where k.planetId = :id ");
		query.setParameter("id", id);
		try {
			Planets_General pg = (Planets_General)query.getSingleResult();
			return pg.getName();
		} catch(NoResultException e) {
			return "Neuer Planet";
		}
		
	}
	
	public List<Flight> getFlightsInc() {
		return flightsInc;
	}

	public void setFlightsInc(List<Flight> flightsInc) {
		this.flightsInc = flightsInc;
	}

	public List<Flight> getFlightsOut() {
		return flightsOut;
	}

	public void setFlightsOut(List<Flight> flightsOut) {
		this.flightsOut = flightsOut;
	}

	public PlanetHandler getPlanetHandler() {
		return planetHandler;
	}

	public void setPlanetHandler(PlanetHandler planetHandler) {
		this.planetHandler = planetHandler;
	}
	

}
