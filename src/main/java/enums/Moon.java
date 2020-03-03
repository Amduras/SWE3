package enums;

public enum Moon {
	TRUE("True"), FALSE("False");
	
	private final String label;
	
	private Moon(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
