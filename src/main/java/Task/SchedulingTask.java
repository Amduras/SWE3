package Task;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import controller.QHandler;

public class SchedulingTask implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//System.out.println("Checking for new tasks..");
		while(!QHandler.queued.isEmpty()) {
			Task t = QHandler.queued.poll();
			int uId = QHandler.uniqueCounter.getAndIncrement();
			try {
				context.getScheduler().scheduleJob(
						newJob(OneTimeTask.class)
							.usingJobData("o", uId)
							.build(),
						newTrigger()
							.startAt(t.getTime())
							.withSchedule(simpleSchedule()
									.withMisfireHandlingInstructionFireNow())
							.build());
				QHandler.waiting.put(uId,t);
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}