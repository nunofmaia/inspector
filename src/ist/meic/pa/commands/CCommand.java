package ist.meic.pa.commands;

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
	
	private Object handleMethodWithParams() throws IllegalAccessException, IllegalArgumentException {
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
	
	private Object handleMethod() throws IllegalAccessException, IllegalArgumentException {
		try {
			Object result = obj.getClass().getDeclaredMethod(args[0]).invoke(obj);
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

	private Object[] getMethodParameters(Method m) throws NoSuchMethodException {
		ArrayList<Object> methodPar = new ArrayList<Object>();
		for (int i = 0; i < m.getParameterTypes().length; i++) {
			Class<?> par = m.getParameterTypes()[i]; 
			/**
			 * A melhorar: 
			 * 	- suporte para mais tipos de argumentos alem de ints;
			 * 	- Ate agora suporta tipos primitivos: int, double, String, boolean, long e char
			 * 	- fazer com que isto nao seja um comboio de if's
			 */
			String parValue = args[i+1];
			if (int.class.equals(par) || int.class.equals(par)) {
				methodPar.add(new Integer(parValue));
			}else if(Double.class.equals(par) || double.class.equals(par)) {
				methodPar.add(new Double(parValue));
			} else if(String.class.equals(par)) {
				methodPar.add(parValue);
			} else if (Boolean.class.equals(par) || boolean.class.equals(par)) {
				methodPar.add(new Boolean(parValue));
			} else if (Long.class.equals(par) || long.class.equals(par)){
				methodPar.add(new Long(parValue));
			} else if ((Character.class.equals(par) || char.class.equals(par)) && parValue.toCharArray().length == 1) {
				methodPar.add(new Character(parValue.toCharArray()[0]));
			} else {
				System.err.println("Error: Unable to convert arguments");
				throw new NoSuchMethodException();
			}
		}
		return methodPar.toArray();
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
