package ist.meic.pa.commands;

import ist.meic.pa.InspectionState;
import ist.meic.pa.TypeChecking;
import ist.meic.pa.Utils;
import ist.meic.pa.annotations.Type;
import ist.meic.pa.exceptions.InvalidArgumentException;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;

public class MCommand extends Command {

	public MCommand(InspectionState state, String[] args) {
		super(state, args);
	}

	@Override
	public InspectionState execute() throws IllegalArgumentException, IllegalAccessException, InvalidArgumentException {
		if (this.args.length == 2) {
			Object obj = this.state.getCurrentObject();
			String attr = this.args[0];
			String value = this.args[1];
			ArrayList<Field> fields = Utils.getField(obj, attr);
			
			if (fields.size() == 0) {
				System.err.println("The field " + attr + " does not exist.");
			} else if (fields.size() == 1) {
				updateField(fields.get(0), value);
			} else {
				System.err.println("There are more than one field with the name " + attr + ". Please enter the number of the field you want to choose.");
				Utils.dumpChoiceList(fields);

				handleChoice(fields, value);
			}
		} else {
			throw new InvalidArgumentException();
		}
		
		return this.state;

	}
	
	private void updateField(Field field, String value) throws IllegalArgumentException, IllegalAccessException {
		Object obj = this.state.getCurrentObject();
		field.setAccessible(true);
		Class<?> c = field.get(obj).getClass();
		field.set(obj, processType(c, value));
		this.state.updateState(obj);
	}

	private void handleChoice(ArrayList<Field> fields, String value) throws IllegalArgumentException, IllegalAccessException {
		Scanner scanner = new Scanner(System.in);
		boolean isActive = true;
		while (isActive) {
			System.err.print("Choice: ");
			String[] command = scanner.nextLine().split(" ");

			try {
				int index = Integer.parseInt(command[0]);
				if (index >= 0 && index < fields.size()) {
					isActive = false;
					updateField(fields.get(index), value);
					
				} else {
					System.err.println("Enter a valid number between 0 and " + (fields.size() - 1) + ".");
				}
			} catch (NumberFormatException e) {
				System.err.println("Enter a valid number between 0 and " + (fields.size() - 1) + ".");
			}
		}
	}
	
	private Object processType(Class<?> type, String value) {
		String typeName = type.getSimpleName();
		Class<?> checker = TypeChecking.class;
		for (Method m : checker.getDeclaredMethods()) {
			Type t = m.getAnnotation(Type.class);
			if (t != null) {
				if (type.isArray()) {
					Class<?> arrayType = type.getComponentType();
					String[] values = value.substring(1, value.length() - 1).split(",");
					Object arr = Array.newInstance(arrayType, values.length);
					int index = 0;
					for (String v : values) {
						Array.set(arr, index, processType(arrayType, v));
						index++;
					}
					return arr;
				} else {
					for (String v : t.value()) {
						if (v.equals(typeName)) {
							Object[] args = new Object[] { value };
							try {
								return m.invoke(null, args);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}					
				}
			}
		}

		return null;
	}

	@Override
	public String usage() {
		return "Usage: m <field> <value>";
	}
}
