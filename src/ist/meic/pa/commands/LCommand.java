package ist.meic.pa.commands;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import ist.meic.pa.InspectionState;
import ist.meic.pa.Utils;

public class LCommand extends Command {

	public LCommand(InspectionState state, String[] args) {
		super(state, args);
	}

	@Override
	public InspectionState execute() throws Exception {
		ArrayList<Method> methods = Utils.getAllMethods(this.state
				.getCurrentObject());
		for (Method m : methods) {
			int mod = m.getModifiers();
			if (!Modifier.isStatic(mod)) {
				System.err.println(m);
			}
		}

		return this.state;
	}

	@Override
	public String usage() {
		return "Usage: l";
	}

}
