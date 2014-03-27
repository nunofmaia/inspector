 package ist.meic.pa.commands;

import ist.meic.pa.InspectionState;
import ist.meic.pa.TypeChecking;
import ist.meic.pa.Utils;
import ist.meic.pa.annotations.Type;
import ist.meic.pa.exceptions.InvalidArgumentException;
import ist.meic.pa.exceptions.TooManyMethodsException;
import ist.meic.pa.exceptions.WrongTypeException;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CCommand extends Command {

	public CCommand(InspectionState state, String[] args) {
		super(state, args);
	}

	@Override
	public InspectionState execute() throws IllegalArgumentException,
			IllegalAccessException, InvalidArgumentException {
		
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
//			Object[] methodParameters = getMethodParameters(m);
//			Object result = m.invoke(this.state.getCurrentObject(),
//					methodParameters);
			if (result != null) {
				this.state.setCurrentObject(result);				
			}

			return this.state;
		} catch (WrongTypeException e) {
			System.err.println("The arguments do not match the method signature.");
			return this.state;
		} catch (TooManyMethodsException e) {
			System.err.println("There are too many methods to make a decision.");
			return this.state;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return this.state;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return this.state;
		}

	}

	private InspectionState handleMethod() throws IllegalAccessException,
			IllegalArgumentException {
		try {
			Object current = this.state.getCurrentObject();
			Method method = Utils.getMethod(current.getClass(), args[0]);
			Object result = method.invoke(current);
			
			
			if (method.getReturnType().isPrimitive()) {
				System.err.println(result);
				result = null;
			} else {
				Utils.dumpObject(result);
			}

			if (result != null) {
				this.state.setCurrentObject(result);			
			}

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

//	private Object[] getMethodParameters(Method m) throws WrongTypeException {
//		ArrayList<Object> methodPar = new ArrayList<Object>();
//		Class<?>[] params = m.getParameterTypes();
//		for (int i = 0; i < params.length; i++) {
//			Class<?> par = params[i];
//			String parValue = args[i + 1];
//
//			methodPar.add(processType(par, parValue));
//
//		}
//
//		return methodPar.toArray();
//	}
//
//	private Object processType(Class<?> type, String value) throws WrongTypeException {
//		String typeName = type.getSimpleName();
//		Class<?> checker = TypeChecking.class;
//		boolean hasMatch = false;
//		for (Method m : checker.getDeclaredMethods()) {
//			Type t = m.getAnnotation(Type.class);
//			if (t != null) {
//				if (type.isArray()) {
//					Class<?> arrayType = type.getComponentType();
//					String[] values = value.substring(1, value.length() - 1).split(",");
//					Object arr = Array.newInstance(arrayType, values.length);
//					int index = 0;
//					
//					
//						for (String v : values) {
//							Array.set(arr, index, processType(arrayType, v));
//							index++;
//						}
//						return arr;
//					
//				} else {
//					for (String v : t.value()) {
//						if (v.equals(typeName)) {
//							hasMatch = true;
//							Object[] args = new Object[] { value };
//							try {
//								return m.invoke(null, args);
//							} catch (Exception e) {
//								if(state.getSavedObjects().containsKey(value)){
//									Object o  = state.getSavedObjects().get(value);
//									if(o.getClass().equals(type)){
//										return o;
//									}
//								}else{
//									throw new WrongTypeException(e);
//								}
//							}
//						}
//					}					
//				}
//			}
//		}
//		if(!hasMatch && state.getSavedObjects().containsKey(value)){
//			Object o  = state.getSavedObjects().get(value);
//			if(o.getClass().equals(type)){
//				return o;
//			}
//		}
//
//		return null;
//	}
	
	private ArrayList<Object> checkPossibleTypes(String value) {
		Class<?> checker = TypeChecking.class;
		ArrayList<Object> types = new ArrayList<Object>();
	
		for (Method f : checker.getDeclaredMethods()) {
			try {
				Object[] args = new Object[] { value };
				Object o = f.invoke(null, args);
				types.add(o);
			} catch (Exception e) {
				// Catch exception and continue loop
			}
		}
		
		if (types.isEmpty()) {
			Map<String, Object> map = this.state.getSavedObjects();
			if (map.containsKey(value)) {
				Object o = map.get(value);
				types.add(o);
			}
		}
		
		return types;
	}
	
	private Object handleChoice(ArrayList<Method> methods) throws Exception {
		
		Map<Integer, ArrayList<Object>> possibleTypes = new HashMap<Integer, ArrayList<Object>>();
		Map<Integer, ArrayList<Object>> possibleMethods = new HashMap<Integer, ArrayList<Object>>();
		
		for (int i = 1; i < args.length; i++) {
			ArrayList<Object> types = checkPossibleTypes(args[i]);
			if (types.isEmpty()) {
				throw new InvalidArgumentException();
			} else {
				possibleTypes.put(i, types);
			}
		}
		
		for (int m = 0; m < methods.size(); m++) {
			Method mth = methods.get(m);
			Class<?>[] paramsTypes = mth.getParameterTypes();
			ArrayList<Object> paramsValues = new ArrayList<Object>();
			for (int i = 0; i < paramsTypes.length; i++) {
				Class<?> type = paramsTypes[i];
				ArrayList<Object> possibilities = possibleTypes.get(i + 1);
				
				for (Object o : possibilities) {
					if (Utils.isSameType(type, o.getClass())) {
						paramsValues.add(o);
					}
					
				}
			}
			
			if (paramsValues.size() == paramsTypes.length) {
				possibleMethods.put(m, paramsValues);
			}
		}
		
		if (possibleMethods.size() == 1) {
			int v = (Integer)possibleMethods.keySet().toArray()[0];
			Method mth = methods.get(v);
			Object[] params = possibleMethods.get(v).toArray();
			Object result = mth.invoke(this.state.getCurrentObject(), params);
			if (mth.getReturnType().isPrimitive()) {
				System.err.println(result);
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
