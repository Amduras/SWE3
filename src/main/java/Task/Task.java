package Task;

import java.util.Date;

import enums.JobType;

public interface Task {

	int getType();
	
	Date getTime();
	
	void executeTask();
	
	void writeToDB();
}
