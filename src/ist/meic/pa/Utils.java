package ist.meic.pa;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

	public static ArrayList<Field> getAllFields(Object object) {
		Class<?> c = object.getClass();
		ArrayList<Field> allFields = new ArrayList<Field>();
		while (c != null) {
			for (Field f : c.getDeclaredFields()) {
				int mod = f.getModifiers();
				if (!Modifier.isStatic(mod)) {
					allFields.add(f);					
				}
			}
			c = c.getSuperclass();
		}
		return allFields;
	}
	
	public static ArrayList<Method> getAllMethods(Object object) {
		Class<?> c = object.getClass();
		ArrayList<Method> allMethods = new ArrayList<Method>();
		while (c != null) {
			for (Method f : c.getDeclaredMethods()) {
				allMethods.add(f);
			}
			c = c.getSuperclass();
		}
		return allMethods;
	}
	
	public static ArrayList<Method> getAllMethods(Object object, String methodName, int argsLength) throws NoSuchMethodException {
		Class<?> c = object.getClass();
		ArrayList<Method> allMethods = new ArrayList<Method>();
		while (c != null) {
			for (Method f : c.getDeclaredMethods()) {
				String name = f.getName();
				int size = f.getParameterTypes().length;
				if (methodName.equals(name) && size == argsLength) {
					allMethods.add(f);				
				}
			}
			
			if (allMethods.size() > 0) {
				return allMethods;
			}
			
			c = c.getSuperclass();
		}
		
		if (allMethods.isEmpty()) {
			throw new NoSuchMethodException();
		}
		
		return allMethods;
	}
	
	public static Field getField(Class<?> clazz, String fieldName)
			throws NoSuchFieldException, IllegalAccessException {

		String className = getClassName(fieldName);
		String attr = getAttributeName(fieldName);
		
		try {
			Field f = null;
			int modifier;
			
			if (className.isEmpty()) {
				f = clazz.getDeclaredField(fieldName);
				modifier = f.getModifiers();
			} else {
				Class<?> newClazz = Class.forName(className);
				f = newClazz.getDeclaredField(attr);
				modifier = f.getModifiers();
			}
			
			if (!Modifier.isStatic(modifier)) {
				return f;
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

		throw new IllegalAccessException();

	}
	
	public static Method getMethod(Class<?> clazz, String methodName)
			throws NoSuchMethodException {
		
		try {
			Method m = clazz.getDeclaredMethod(methodName);
			int modifier = m.getModifiers();
			
			if (!Modifier.isStatic(modifier)) {
				return m;
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			Class<?> superClazz = clazz.getSuperclass();
			if (superClazz != null) {
				return getMethod(superClazz, methodName);
			} else {
				throw new NoSuchMethodException();
			}
		}

		throw new NoSuchMethodException();

	}

	
	public static void dumpChoiceList(ArrayList<?> list) {
		for (int i = 0; i < list.size(); i++) {
			System.err.println("[" + i + "] " + list.get(i));
		}
	}
	

	public static void dumpObject(Object object) throws IllegalAccessException {

		System.err.println(object + " is an instance of class "
				+ object.getClass());
		System.err.println("--------------");

		for (Field f : Utils.getAllFields(object)) {
			f.setAccessible(true);
			System.err.println(f + " = " + f.get(object));
		}

	}
	
	public static String getClassName(String fieldName) {
		String[] arr = fieldName.split("\\.");
		
		if (arr.length == 1) {
			return "";
		} else {
			return fieldName.substring(0, fieldName.length() - arr[arr.length - 1].length() - 1);
		}
	}
	
	public static String getAttributeName(String fieldName) {
		String[] arr = fieldName.split("\\.");
		
		return arr[arr.length - 1];
	}
	
	public static boolean isSameType(Class<?> t1, Class<?> t2) {
		if (t1.isPrimitive()) {
			Field f;
			try {
				f = t2.getField("TYPE");
				Object t = f.get(null);
				return t1.getSimpleName().equals(t.toString());
			} catch (SecurityException e) {
				return false;
			} catch (NoSuchFieldException e) {
				return false;
			} catch (IllegalArgumentException e) {
				return false;
			} catch (IllegalAccessException e) {
				return false;
			}
		}
			
		return t2.equals(t1);

	}
	
	public static boolean isArray(String value) {
		return (value.charAt(0) == '[' && value.charAt(value.length() - 1) == ']');
	}
	
	public static String[] splitInput(String input) {
		List<String> list = new ArrayList<String>();
		String pattern = "(\\\"[^\\\"\\\\\\\\]*(?:\\\\.[^\\\"\\\\\\\\]*)*\\\"|\\[.*\\])";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(input);
		
		String[] rep = input.replaceAll(pattern, "?").split(" ");
		String[] result = new String[rep.length];
		
		for (String r : rep) {
			if (r.equals("?")) {
				m.find();
				list.add(m.group(1));
			} else {
				list.add(r);
			}
		}
		
		
		return list.toArray(result);
	}
	
}
