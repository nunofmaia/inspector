package ist.meic.pa.commands;

import ist.meic.pa.InspectionState;

public abstract class Command {
	
	protected InspectionState state;
	protected String[] args;
	
	public Command(InspectionState state, String[] args) {
		this.state = state;
		this.args = args;
	}
	
	public abstract InspectionState execute() throws IllegalArgumentException, IllegalAccessException; 
}
