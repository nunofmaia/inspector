package ist.meic.pa.commands;

import java.util.Map;

import ist.meic.pa.InspectionState;

public class SCommand extends Command {

	public SCommand(InspectionState state, String[] args) {
		super(state, args);
	}

	@Override
	public InspectionState execute() throws IllegalArgumentException,
			IllegalAccessException {
		Map<String, Object> savedObjects = state.getSavedObjects();
		String name = args[0];
		if(!savedObjects.keySet().contains(name)){
			savedObjects.put(name, state.getCurrentObject());
			System.out.println("Objects saved: " +  savedObjects);
		}else{
			System.err.println("Error: Object with that name already exists");
		}
		
		return state;
	}

	@Override
	public String usage() {
		return "Usage: s <name>";
	}

}
