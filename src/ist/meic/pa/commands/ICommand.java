package ist.meic.pa.commands;

import java.lang.reflect.Field;
import ist.meic.pa.InspectionState;
import ist.meic.pa.Utils;

public class ICommand extends Command {

	public ICommand(InspectionState state, String[] args) {
		super(state, args);
	}

	@Override
	public InspectionState execute() throws NoSuchFieldException {

		if (this.args.length == 1) {
			Class<?> clazz = this.state.getCurrentObject().getClass();
			String attr = this.args[0];

			try {
				Field field = Utils.getField(clazz, attr);
				getField(field);
			} catch (IllegalArgumentException e) {
				System.err
						.println("The inspected object does not have the specified field.");
			} catch (IllegalAccessException e) {
				System.err
						.println("Cannot access the specified field.");
			}

		}

		return this.state;
	}

	private void getField(Field f) throws IllegalArgumentException, IllegalAccessException {
		Object obj = this.state.getCurrentObject();
		f.setAccessible(true);
		obj = f.get(obj);
		if (!f.getType().isPrimitive()) {
			this.state.setCurrentObject(obj);
			Utils.dumpObject(obj);
		} else {
			System.err.println(obj);
		}
	}

	@Override
	public String usage() {
		return "Usage: i <field>";
	}

}
