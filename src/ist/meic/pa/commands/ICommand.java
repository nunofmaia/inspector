package ist.meic.pa.commands;

import java.lang.reflect.Field;

import ist.meic.pa.InspectionState;
import ist.meic.pa.Utils;

public class ICommand extends Command {

	public ICommand(InspectionState state, String[] args) {
		super(state, args);
		// TODO Auto-generated constructor stub
	}

	@Override
	public InspectionState execute() throws IllegalArgumentException, IllegalAccessException {
		if (this.args.length > 0) {
			Object obj = this.state.getCurrentObject();
			 String attr = this.args[0];
			 for (Field f : Utils.getAllFields(obj)) {
				 if (f.getName().equals(attr)) {
					 f.setAccessible(true);
					 obj = f.get(obj);
					 this.state.setCurrentObject(obj);
					 break;
				 }
			 }
		}
		return this.state;
	}

}
