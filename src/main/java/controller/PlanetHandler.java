package controller;


import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.primefaces.context.RequestContext;
import org.quartz.JobDataMap;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;

import Task.ResUpdateTask;
import planets.*;

@ManagedBean(name="planetHandler")
@SessionScoped
public class PlanetHandler {
	
	private SchedulerFactory factory = new org.quartz.impl.StdSchedulerFactory();
	private Scheduler s ;
	
	private Planets_Buildings pb;
	private Planets_Def pd;
	private Planets_General activePlanet = new Planets_General();
	private Planets_Research pr;
	private Planets_Ships ps;
	
	public PlanetHandler() {
		try {
			s = factory.getScheduler();
			s.start();
			s.getContext().put("o", this);
			s.scheduleJob(
					newJob(ResUpdateTask.class)
						.build(),
					newTrigger()
						.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever())
						.startNow()
						.build());
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void updateRes() {
		System.out.println("eyyyyyyyyyyyyyyyyyyyyyy");
		int m = activePlanet.getMetal();
		activePlanet.setMetal(m+1000);
		RequestContext.getCurrentInstance().update("update_res");
	}

	public Planets_General getActivePlanet() {
		return activePlanet;
	}

	public void setActivePlanet(Planets_General activePlanet) {
		this.activePlanet = activePlanet;
	}
}