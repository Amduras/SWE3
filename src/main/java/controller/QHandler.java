package controller;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;

import Task.Task;
import Task.SchedulingTask;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class QHandler implements ServletContextListener {

		public static BlockingQueue<Task> queued = new LinkedBlockingQueue<Task>();
		public static Map<Integer,Task> waiting = Collections.synchronizedMap(new HashMap<Integer,Task>());
		public static AtomicInteger uniqueCounter = new AtomicInteger(0);
		
		private SchedulerFactory factory = new org.quartz.impl.StdSchedulerFactory();
		private Scheduler s;
		
		private boolean isEnabled = false;
		
	public void contextInitialized(ServletContextEvent sce) {
		/** Code will run when JBoss starts **/
		if(!isEnabled) {
			isEnabled = true;
			
			loadTasksFromDB();
				
			try {
				s = factory.getScheduler();
				s.start();
				/** Schedule Job checking for new Task **/
				/*s.scheduleJob(
						newJob(SchedulingTask.class)
							.build(),
						newTrigger()
							.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever())
							.startNow()
							.build());*/
				
			} catch (SchedulerException e) {
					e.printStackTrace();
			}				
		}
	}
	
	private void loadTasksFromDB() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		try {
			s.shutdown();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		//** Save both queues **//
		// TODO 
	}
}
