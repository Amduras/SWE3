package planets;

import javax.faces.bean.SessionScoped;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;

import Task.SchedulingTask;

@SessionScoped
public class Planet {

	private SchedulerFactory factory = new org.quartz.impl.StdSchedulerFactory();
	private Scheduler s ;
	
	private long metal = 0;
	private long crystal = 0;
	private long deut = 0;
	private int energy = 0;
	private long metalStorage = 0;
	private long crystalStorage = 0;
	private long deutStorage = 0;
	
	public Planet() {
		try {
			s = factory.getScheduler();
			s.start();
			s.scheduleJob(
					newJob(SchedulingTask.class)
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
	
	public long getMetal() {
		return metal;
	}
	public void setMetal(long metal) {
		this.metal = metal;
	}
	public long getCrystal() {
		return crystal;
	}
	public void setCrystal(long crystal) {
		this.crystal = crystal;
	}
	public long getDeut() {
		return deut;
	}
	public void setDeut(long deut) {
		this.deut = deut;
	}
	public int getEnergy() {
		return energy;
	}
	public void setEnergy(int energy) {
		this.energy = energy;
	}
	public long getMetalStorage() {
		return metalStorage;
	}
	public void setMetalStorage(long metalStorage) {
		this.metalStorage = metalStorage;
	}
	public long getCrystalStorage() {
		return crystalStorage;
	}
	public void setCrystalStorage(long crystalStorage) {
		this.crystalStorage = crystalStorage;
	}
	public long getDeutStorage() {
		return crystalStorage;
	}
	public void setDeutStorage(long crystalStorage) {
		this.crystalStorage = crystalStorage;
	}
}
