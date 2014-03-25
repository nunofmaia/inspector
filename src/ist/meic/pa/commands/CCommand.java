package ist.meic.pa.commands;

import ist.meic.pa.InspectionState;
import ist.meic.pa.TypeChecking;
import ist.meic.pa.Utils;
import ist.meic.pa.annotations.Type;
import ist.meic.pa.exceptions.WrongTypeException;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;

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

	private InspectionState handleMethodWithParams()
			throws IllegalAccessException, IllegalArgumentException {
		try {
			ArrayList<Method> methods = getMethodsByName();
			System.err.println("There are more than one method with the name " + args[0] + ". Please enter the number of the method you want to choose.");
			Utils.dumpChoiceList(methods);
			Method m = handleChoice(methods);
			Object[] methodParameters = getMethodParameters(m);
			Object result = m.invoke(this.state.getCurrentObject(),
					methodParameters);
			this.state.setCurrentObject(result);

			return this.state;
		} catch (WrongTypeException e) {
			System.err.println("The arguments do not match the method signature.");
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
			Object result = current.getClass().getDeclaredMethod(args[0]).invoke(current);

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

	private Object[] getMethodParameters(Method m) throws WrongTypeException {
		ArrayList<Object> methodPar = new ArrayList<Object>();
		for (int i = 0; i < m.getParameterTypes().length; i++) {
			Class<?> par = m.getParameterTypes()[i];
			String parValue = args[i + 1];

			methodPar.add(processType(par, parValue));

		}

		return methodPar.toArray();
	}

//	public void addParameter(ArrayList<Object> params, String value,
//			Class<?> clazz) {
//
//		try {
//			Constructor<?> con = clazz.getConstructor(String.class);
//			Object[] sdoid = new Object[] { value };
//			params.add(con.newInstance(sdoid));
//		} catch (Exception e) {
//			System.err.println("error");
//		}
//	}

	private ArrayList<Method> getMethodsByName() throws NoSuchMethodException {
		ArrayList<Method> methods = new ArrayList<Method>();
		for (Method m : Utils.getAllMethods(this.state.getCurrentObject())) {
			if (m.getName().equals(args[0])
					&& m.getParameterTypes().length == (args.length - 1)) {
				methods.add(m);
			}
		}
		
		if (methods.size() == 0) {
			throw new NoSuchMethodException();			
		}
		
		return methods;

	}

	private Object processType(Class<?> type, String value) throws WrongTypeException {
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
								throw new WrongTypeException(e);
							}
						}
					}					
				}
			}
		}

		return null;
	}
	
	private Method handleChoice(ArrayList<Method> methods) throws IllegalArgumentException, IllegalAccessException {
		Scanner scanner = new Scanner(System.in);
		boolean isActive = true;
		while (isActive) {
			System.err.print("Choice: ");
			String[] command = scanner.nextLine().split(" ");

			try {
				int index = Integer.parseInt(command[0]);
				if (index >= 0 && index < methods.size()) {
					isActive = false;
					Method m = methods.get(index);
					return m;
					
				} else {
					System.err.println("Enter a valid number between 0 and " + (methods.size() - 1) + ".");
				}
			} catch (NumberFormatException e) {
				System.err.println("Enter a valid number between 0 and " + (methods.size() - 1) + ".");
			}
		}
		
		return null;
	}

}
