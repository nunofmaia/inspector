package ist.meic.pa.commands;

import ist.meic.pa.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class CCommand extends Command {

	public CCommand(Object obj, String[] args) {
		super(obj, args);
	}

	@Override
	public Object execute() throws IllegalArgumentException,
			IllegalAccessException {
		if (args.length != 1) {
			return handleMethodWithParams();
		} else {
			return handleMethod();

		}
	}

	private Object handleMethodWithParams() throws IllegalAccessException,
			IllegalArgumentException {
		try {
			Method m = getMethodByName();
			Object[] methodParameters = getMethodParameters(m);
			return m.invoke(obj, methodParameters);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return obj;
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return obj;
		}

	}

	private Object handleMethod() throws IllegalAccessException,
			IllegalArgumentException {
		try {
			Object result = obj.getClass().getDeclaredMethod(args[0])
					.invoke(obj);
			return result;
		} catch (NoSuchMethodException e) {
			System.err.println("Unable to fulfill request");
			return obj;
		} catch (SecurityException e) {
			System.err.println("Unable to fulfill request");
			return obj;
		} catch (InvocationTargetException e) {
			System.err.println("Unable to fulfill request");
			return obj;
		}

	}

	private Object[] getMethodParameters(Method m) {
		ArrayList<Object> methodPar = new ArrayList<Object>();
		for (int i = 0; i < m.getParameterTypes().length; i++) {
			Class<?> par = m.getParameterTypes()[i];
			String parValue = args[i + 1];
			
			if (par.isPrimitive()) {
				Class<?> wrapper = Utils.getWrapper(par);
				if ((Character.class.equals(par) || char.class.equals(par))
						&& parValue.toCharArray().length == 1) {
					methodPar.add(new Character(parValue.toCharArray()[0]));
				} else {
					addParameter(methodPar, parValue, wrapper);
				}
			} else {
				addParameter(methodPar, parValue, par);
			}
		}
		return methodPar.toArray();
	}

	public void addParameter(ArrayList<Object> params, String value,
			Class<?> clazz) {
		
		try {
			Constructor<?> con = clazz.getConstructor(String.class);
			Object[] sdoid = new Object[] { value };
			params.add(con.newInstance(sdoid));
		} catch (Exception e) {
			System.err.println("error");
		}
	}

	private Method getMethodByName() throws NoSuchMethodException {
		for (Method m : obj.getClass().getDeclaredMethods()) {
			if (m.getName().equals(args[0])
					&& m.getParameterTypes().length == (args.length - 1)) {
				return m;

			}
		}
		throw new NoSuchMethodException();

	}

}
