package ist.meic.pa.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class CCommand extends Command {
	private Object obj;
	private String[] args;

	public CCommand(Object obj, String[] args) {
		super(obj, args);
		this.obj = obj;
		this.args = args;
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
	
	private Object handleMethodWithParams() throws IllegalAccessException, IllegalArgumentException{
		try {
			Method m = getMethodByName();
			Object[] methodParameters = getMethodParameters(m);
			return m.invoke(obj, methodParameters);
		} catch (NoSuchMethodException | SecurityException
				| InvocationTargetException e) {
			e.printStackTrace();
			return obj;
		}
		
	}
	
	private Object handleMethod() throws IllegalAccessException, IllegalArgumentException{
		try {
			Object result = obj.getClass().getDeclaredMethod(args[0])
					.invoke(obj);
			return result;
		} catch (NoSuchMethodException | SecurityException|InvocationTargetException e) {
			System.err.println("Unable to fulfill request");
			return obj;
		} 
		
	}

	private Object[] getMethodParameters(Method m){
		ArrayList<Object> methodPar = new ArrayList<Object>();
		for (int i = 0; i < m.getParameterTypes().length; i++) {
			/**
			 * A melhorar: 
			 * 	- suporte para mais tipos de argumentos além de ints;
			 * 	- fazer com que isto não seja um comboio de if's
			 */
			if (int.class.equals(m.getParameterTypes()[i])) {
				methodPar.add(new Integer(args[i+1]));
			} else {
				//...
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
