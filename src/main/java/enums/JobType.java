package enums;

public enum JobType {
	BUILD_STRUCT("build_struct"), BUILD_FLEET("build_fleet"), BUILD_DEF("build_def"), BUILD_TECH("build_tech"), MOVE_FLEET("move_fleet");
	
	private final String label;
	
	private JobType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}