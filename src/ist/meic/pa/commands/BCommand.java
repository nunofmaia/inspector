package ist.meic.pa.commands;

import java.util.Stack;

import ist.meic.pa.InspectionState;

public class BCommand extends Command {

	public BCommand(InspectionState state, String[] args) {
		super(state, args);
	}

	@Override
	public InspectionState execute() throws IllegalArgumentException,
			IllegalAccessException {
		if (args.length > 0) {
			Stack<Object> history = this.state.getHistory();
			Object nextObject = this.state.getCurrentObject();
			int jumpIndex = Integer.parseInt(args[0]);
			if (jumpIndex > history.size()) {
				jumpIndex = history.size();
			}
			
			for (int i = 0; i < jumpIndex; i++) {
				nextObject = history.pop();
			}
			
			this.state.updateState(nextObject);
			this.state.setHistory(history);
		}
		
		return this.state;
	}

}
