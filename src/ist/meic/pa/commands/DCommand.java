package ist.meic.pa.commands;

import ist.meic.pa.InspectionState;
import ist.meic.pa.Utils;

public class DCommand extends Command {

	public DCommand(InspectionState state, String[] args) {
		super(state, args);
	}

	@Override
	public InspectionState execute() throws IllegalArgumentException, IllegalAccessException {
		
		Utils.dumpObject(this.state.getCurrentObject());
		
		return this.state;
	}
}
