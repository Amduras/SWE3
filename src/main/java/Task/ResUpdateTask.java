package Task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import controller.PlanetHandler;

public class ResUpdateTask implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		PlanetHandler.updateRes();
	}
}