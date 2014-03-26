package ist.meic.pa.commands;

import ist.meic.pa.InspectionState;
import ist.meic.pa.TypeChecking;
import ist.meic.pa.annotations.Type;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class CCommand extends Command {

	public CCommand(InspectionState state, String[] args) {
		super(state, args);
	}

	@Override
	public InspectionState execute() throws IllegalArgumentException,
			IllegalAccessException {
		if (args.length != 1) {
			return handleMethodWithParams();
		} else {
			return handleMethod();

		}
	}

	/*
	 * If the method to call has parameters, they must be handled (check types and create an array of Object)
	 */
	private InspectionState handleMethodWithParams()
			throws IllegalAccessException, IllegalArgumentException {
		try {
			Method m = getMethodByName();
			Object[] methodParameters = getMethodParameters(m);
			Object result = m.invoke(this.state.getCurrentObject(),
					methodParameters);
			this.state.setCurrentObject(result);

			return this.state;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return this.state;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return this.state;
		}

	}

	private InspectionState handleMethod() throws IllegalAccessException,
			IllegalArgumentException {
		try {
			Object current = this.state.getCurrentObject();
			Object result = current.getClass().getDeclaredMethod(args[0])
					.invoke(current);

			this.state.setCurrentObject(result);

			return this.state;
		} catch (NoSuchMethodException e) {
			System.err.println("Unable to fulfill request");
			return this.state;
		} catch (SecurityException e) {
			System.err.println("Unable to fulfill request");
			return this.state;
		} catch (InvocationTargetException e) {
			System.err.println("Unable to fulfill request");
			return this.state;
		}

	}

	private Object[] getMethodParameters(Method m) {
		ArrayList<Object> methodPar = new ArrayList<Object>();
		for (int i = 0; i < m.getParameterTypes().length; i++) {
			Class<?> par = m.getParameterTypes()[i];
			String parValue = args[i + 1];

			methodPar.add(processType(par, parValue));

		}

		return methodPar.toArray();
	}

	private Method getMethodByName() throws NoSuchMethodException {
		for (Method m : this.state.getCurrentObject().getClass()
				.getDeclaredMethods()) {
			if (m.getName().equals(args[0])
					&& m.getParameterTypes().length == (args.length - 1)) {
				return m;

			}
		}
		throw new NoSuchMethodException();

	}

	private Object processType(Class<?> type, String value) {
		String typeName = type.getSimpleName();
		Class<?> checker = TypeChecking.class;
		boolean hasMatch = false;
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
							hasMatch = true;
							Object[] args = new Object[] { value };
							try {
								return m.invoke(null, args);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}					
				}
			}
		}
		if(!hasMatch && state.getSavedObjects().containsKey(value)){
			Object o  = state.getSavedObjects().get(value);
			if(o.getClass().equals(type)){
				return o;
			}
		}

		return null;
	}

}
