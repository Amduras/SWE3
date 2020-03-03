package Task;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.enterprise.inject.spi.Bean;
import javax.faces.context.FacesContext;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import controller.PlanetHandler;

public class ResUpdateTask implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		PlanetHandler ph;
		System.out.println("hi");
		try {
			
			ph = (PlanetHandler) context.getScheduler().getContext().get("o");
			ph.updateRes();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}