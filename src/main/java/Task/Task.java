package Task;

import java.util.Date;

import enums.JobType;

public interface Task {

	JobType getType();
	
	Date getTime();
	
	void executeTask();
	
	void writeToDB();
}
