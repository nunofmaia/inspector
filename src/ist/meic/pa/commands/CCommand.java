 package ist.meic.pa.commands;

import ist.meic.pa.InspectionState;
import ist.meic.pa.Utils;
import ist.meic.pa.exceptions.InvalidArgumentException;
import ist.meic.pa.exceptions.TooManyMethodsException;
import ist.meic.pa.exceptions.WrongTypeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CCommand extends Command {

	public CCommand(InspectionState state, String[] args) {
		super(state, args);
	}

	@Override
	public InspectionState execute() throws IllegalArgumentException,
			IllegalAccessException, InvalidArgumentException, NoSuchMethodException {
		
		if (args.length > 1) {
			return handleMethodWithParams();
		} else if (args.length == 1) {
			return handleMethod();
		} else {
			throw new InvalidArgumentException();
		}
	}

	/*
	 * If the method to call has parameters, they must be handled (check types and create an array of Object)
	 */
	private InspectionState handleMethodWithParams()
			throws IllegalAccessException, IllegalArgumentException {
		try {
			ArrayList<Method> methods = Utils.getAllMethods(this.state.getCurrentObject(), args[0], args.length - 1);
			Object result = handleChoice(methods);

			if (result != null) {
				this.state.setCurrentObject(result);
				Utils.dumpObject(result);
			}

			this.state.removeNext();
			return this.state;
		} catch (WrongTypeException e) {
			System.err.println("The arguments do not match the method signature.");
			return this.state;
		} catch (TooManyMethodsException e) {
			System.err.println("There are too many methods to make a decision.");
			return this.state;
		} catch (NoSuchMethodException e) {
			System.err.println("The inspected object does not have the specified method.");
			return this.state;
		} catch (Exception e) {
			return this.state;
		}

	}

	private InspectionState handleMethod() throws IllegalAccessException,
			IllegalArgumentException, NoSuchMethodException {
		try {
			Object current = this.state.getCurrentObject();
			Method method = Utils.getMethod(current.getClass(), args[0]);
			Object result = method.invoke(current);
			
			
			if (method.getReturnType().isPrimitive()) {
				if (result != null) {
					System.err.println(result);
				}
				result = null;
			} else {
				Utils.dumpObject(result);
			}

			if (result != null) {
				this.state.setCurrentObject(result);			
			}
			
			this.state.removeNext();
			return this.state;
		} catch (SecurityException e) {
			System.err.println("Unable to fulfill request");
			return this.state;
		} catch (InvocationTargetException e) {
			System.err.println("Unable to fulfill request");
			return this.state;
		}

	}
	
	private Object handleChoice(ArrayList<Method> methods) throws Exception {
		
		Map<Integer, ArrayList<Object>> possibleMethods = new HashMap<Integer, ArrayList<Object>>();
		
		for (int m = 0; m < methods.size(); m++) {
			Class<?>[] params = methods.get(m).getParameterTypes();
			ArrayList<Object> paramsValues = new ArrayList<Object>();
			for (int i = 0; i < params.length; i++) {
				Object o = Utils.processType(this.state, params[i], args[i + 1]);
				
				if (o != null) {
					paramsValues.add(o);
				}
				
			}
			
			if (paramsValues.size() == params.length) {
				possibleMethods.put(m, paramsValues);
			}
		}
		
		if (possibleMethods.isEmpty()) {
			throw new WrongTypeException();
		}
		
		if (possibleMethods.size() == 1) {
			int v = (Integer) possibleMethods.keySet().toArray()[0];
			Method mth = methods.get(v);
			Object[] params = possibleMethods.get(v).toArray();
			Object result = mth.invoke(this.state.getCurrentObject(), params);
			if (mth.getReturnType().isPrimitive()) {
				if (result != null) {
					System.err.println(result);
				}
				return null;
			} else {
				return result;
			}
		} else {
			throw new TooManyMethodsException();
		}
	}
	
	@Override
	public String usage() {
		return "Usage: c <method> <arg1> ... <argN>";
	}

}
