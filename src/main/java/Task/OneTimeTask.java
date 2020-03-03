package Task;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import controller.QHandler;

public class OneTimeTask implements Job{

	public OneTimeTask() {
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		JobDataMap dM = context.getJobDetail().getJobDataMap();
		Task t = QHandler.waiting.remove(dM.getInt("o"));
		if(t != null)
			t.executeTask();
		else
			System.err.print("Task with givin key does not exist in QHandler.waiting map.");
	}	
}
