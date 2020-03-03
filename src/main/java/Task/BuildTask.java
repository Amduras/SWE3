package Task;

import java.util.Date;

import controller.QHandler;
import enums.JobType;

public class BuildTask implements Task{
	
	private JobType type = JobType.BUILD_DEF;
	private Date time;
	//private int time = 40;
	private int upgradeId;
	private int player;
	private int planet;
	
	public BuildTask(JobType type, Date time, int upgradeId, int player, int planet) {
		this.type = type;
		this.time = time;
		this.upgradeId = upgradeId;
		this.player = player;
		this.planet = planet;
		
		/** Add to queue for schedule **/
		QHandler.queued.add(this);
	}
	
	@Override
	public JobType getType() {
		return type;
	}

	@Override
	public Date getTime() {
		return time;
	}

	@Override
	public void executeTask() {
		System.out.println("EX BT " + type.getLabel());	
	}

	@Override
	public void writeToDB() {
		// TODO Auto-generated method stub
		
	}

}
