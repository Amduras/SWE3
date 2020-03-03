package enums;

public enum IsActive {
	TRUE("active"), FALSE("inactive");

	private final String label;

	private IsActive(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
