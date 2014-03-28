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
		if(nameIsValid(name)){
			if(!savedObjects.keySet().contains(name)){
				Object o = state.getCurrentObject();
				savedObjects.put(name, o);
				System.err.println("The object " + o + " was saved with the name \"" + name + "\"");
			}else{
				System.err.println("Object with that name already exists.");
			}
		}else{
			System.err.println("Variable names must either start with '_' or with a letter from 'a' to 'z'");
		}
		return state;
	}

	private boolean nameIsValid(String name) {
		if(name.charAt(0) == '_' || (name.charAt(0) >= 'a' && name.charAt(0) <= 'z'   )){
			return true;
		}
		return false;
	}

	@Override
	public String usage() {
		return "Usage: s <name>";
	}

}
