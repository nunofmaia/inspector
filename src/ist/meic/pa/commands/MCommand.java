package ist.meic.pa.commands;

import ist.meic.pa.InspectionState;
import ist.meic.pa.TypeChecking;
import ist.meic.pa.Utils;
import ist.meic.pa.annotations.Type;
import ist.meic.pa.exceptions.InvalidArgumentException;
import ist.meic.pa.exceptions.WrongTypeException;

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
	public InspectionState execute() throws NoSuchFieldException {

		if (this.args.length == 2) {
			Class<?> clazz = this.state.getCurrentObject().getClass();
			String attr = this.args[0];
			String value = this.args[1];

			Field field = getField(clazz, attr);
			try {
				updateField(field, value);
			} catch (IllegalArgumentException e) {
				System.err
						.println("The value does not match the type of the field.");
			} catch (IllegalAccessException e) {
				System.err
						.println("The value does not match the type of the field.");
			}

		}

		return this.state;

	}

	private Field getField(Class<?> clazz, String fieldName)
			throws NoSuchFieldException {

		String className = Utils.getClassName(fieldName);
		String attr = Utils.getAttributeName(fieldName);
		
		try {
			if (className.isEmpty()) {
				return clazz.getDeclaredField(fieldName);
			} else {
				Class<?> newClazz = Class.forName(className);
				return newClazz.getDeclaredField(attr);
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			Class<?> superClazz = clazz.getSuperclass();
			if (superClazz != null) {
				return getField(superClazz, fieldName);
			} else {
				throw new NoSuchFieldException();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			throw new NoSuchFieldException();
		}

		return null;

	}

	private void updateField(Field field, String value)
			throws IllegalArgumentException, IllegalAccessException {
		Object obj = this.state.getCurrentObject();
		field.setAccessible(true);
		Class<?> c = field.get(obj).getClass();
		try {
			field.set(obj, processType(c, value));
			this.state.updateState(obj);
		} catch (WrongTypeException e) {
			System.err.println("The field and value types are different.");
		}
	}

	private Object processType(Class<?> type, String value) throws WrongTypeException {
		String typeName = type.getSimpleName();
		Class<?> checker = TypeChecking.class;
		for (Method m : checker.getDeclaredMethods()) {
			Type t = m.getAnnotation(Type.class);
			if (t != null) {
				if (type.isArray()) {
					Class<?> arrayType = type.getComponentType();
					String[] values = value.substring(1, value.length() - 1)
							.split(",");
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
								throw new WrongTypeException();
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
