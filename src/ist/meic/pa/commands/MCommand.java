package ist.meic.pa.commands;

import ist.meic.pa.InspectionState;
import ist.meic.pa.Utils;

import java.lang.reflect.Field;

public class MCommand extends Command {

	public MCommand(InspectionState state, String[] args) {
		super(state, args);
	}

	@Override
	public InspectionState execute() throws IllegalArgumentException, IllegalAccessException {
		// TODO Auto-generated method stub
		if (this.args.length > 1) {
			Object obj = this.state.getCurrentObject();
			String attr = this.args[0];
			String value = this.args[1];
			for (Field f : Utils.getAllFields(obj)) {
				if (f.getName().equals(attr)) {
					f.setAccessible(true);
					Class<?> c = f.get(obj).getClass();
					if (c.getSimpleName().equals("Integer")) {
							 f.set(obj, Integer.parseInt(value));
							 this.state.updateState(obj);
					}

					break;
				}
			}
		}
		
		return this.state;

	}

}
