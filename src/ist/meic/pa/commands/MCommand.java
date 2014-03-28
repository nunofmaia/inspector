package ist.meic.pa.commands;

import ist.meic.pa.InspectionState;
import ist.meic.pa.Utils;
import ist.meic.pa.exceptions.WrongTypeException;

import java.lang.reflect.Field;

public class MCommand extends Command {

	public MCommand(InspectionState state, String[] args) {
		super(state, args);
	}

	@Override
	public InspectionState execute() throws NoSuchFieldException, WrongTypeException {

		if (this.args.length == 2) {
			Class<?> clazz = this.state.getCurrentObject().getClass();
			String attr = this.args[0];
			String value = this.args[1];

			try {
				Field field = Utils.getField(clazz, attr);
				updateField(field, value);
			} catch (IllegalArgumentException e) {
				System.err
						.println("The value does not match the type of the field.");
			} catch (IllegalAccessException e) {
				System.err
						.println("Cannot access the specified field.");
			}

		}

		return this.state;

	}

	private void updateField(Field field, String value)
			throws IllegalArgumentException, IllegalAccessException, WrongTypeException {
		Object obj = this.state.getCurrentObject();
		field.setAccessible(true);
		Class<?> c = field.get(obj).getClass();
		
			Object o = Utils.processType(this.state, c, value);
			
			if (o != null) {
				field.set(obj, o);
				this.state.updateState(obj);
			} else {
				throw new WrongTypeException();
			}
			
			Utils.dumpObject(obj);
	}

	@Override
	public String usage() {
		return "Usage: m <field> <value>";
	}
}
