package enums;

public enum AuthLvl {
	BANNED("banned"), RESTRICTED("restricted"), USER("user"), GA("ga"), SGA("sga");
	
	private final String label;
	
	private AuthLvl(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
