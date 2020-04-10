package Task;

import java.util.Date;

public interface Task {

	int getType();
	
	Date getTime();
	
	void executeTask();
	
	void saveToDB(int id);
}
