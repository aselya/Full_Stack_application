package edu.bu.met.ateam.issuetracker;

public enum Priority {
	HIGH (1, "High"), MEDIUM (2, "Medium"), LOW (3, "Low");
	
	private final Integer value;
	private final String string;
	
	Priority(Integer value, String string) {
		this.value = value;
		this.string = string;
	}
	
	public Integer getValue() {
		return value;
	}
	
	public String toString() {
		return string;
	}
}
