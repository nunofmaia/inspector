package ist.meic.pa;

import ist.meic.pa.annotations.Type;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
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

	/**
	 * Get all methods
	 */
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

	/**
	 * Get all methods with a certain name and a specific args length
	 */
	public static ArrayList<Method> getAllMethods(Object object,
			String methodName, int argsLength) throws NoSuchMethodException {
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
			throw new NoSuchFieldException();
		} catch (NoSuchFieldException e) {
			Class<?> superClazz = clazz.getSuperclass();
			if (superClazz != null) {
				return getField(superClazz, fieldName);
			} else {
				throw new NoSuchFieldException();
			}
		} catch (ClassNotFoundException e) {
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

	public static void dumpObject(Object object) throws IllegalAccessException {

		System.err.println(object + " is an instance of " + object.getClass());
		System.err.println("--------------");

		for (Field f : Utils.getAllFields(object)) {
			f.setAccessible(true);
			Class<?> clazz = f.getType();
			Object o = f.get(object);
			if (clazz.equals(String.class)) {
				System.err.println(f + " = " + dumpString(o));
			} else if (clazz.isArray()) {
				System.err.println(f + " = " + dumpArray(o));
			} else {
				System.err.println(f + " = " + f.get(object));
			}
		}

	}

	private static String dumpArray(Object o) {
		Class<?> type = o.getClass().getComponentType();
		int len = Array.getLength(o);
		String result = "[ ";

		for (int i = 0; i < len; i++) {
			if (type.equals(String.class)) {
				Object s = Array.get(o, i);
				result += dumpString(s) + " ";
			} else {
				result += o;
			}
		}

		result += "]";

		return result;

	}

	private static String dumpString(Object s) {
		return "\"" + s + "\"";
	}

	public static String getClassName(String fieldName) {
		String[] arr = fieldName.split("\\.");

		if (arr.length == 1) {
			return "";
		} else {
			return fieldName.substring(0, fieldName.length()
					- arr[arr.length - 1].length() - 1);
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
			} catch (Exception e) {
				return false;
			}
		}

		return t2.equals(t1);

	}

	public static String[] splitInput(String input) {
		List<String> list = new ArrayList<String>();
		String pattern = "(\\\"[^\\\"\\\\\\\\]*(?:\\\\.[^\\\"\\\\\\\\]*)*\\\"|\\[.*\\])";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(input);

		String[] rep = input.replaceAll(pattern, "?").split("\\s+");
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

	public static String[] splitArrayStrings(String input) {
		List<String> list = new ArrayList<String>();
		String pattern = "(\\\"[^\\\"\\\\]*(?:\\\\.[^\\\"\\\\,]*)*\\\")";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(input);

		String[] rep = input.replaceAll(pattern, "?").split(",\\s*");
		String[] result = new String[rep.length];

		while (m.find()) {
			list.add(m.group(1));
		}

		return list.toArray(result);
	}

	public static Object processType(InspectionState state, Class<?> type,
			String value) {
		String typeName = type.getSimpleName();
		Class<?> checker = TypeChecking.class;
		boolean hasMatch = false;
		for (Method m : checker.getDeclaredMethods()) {
			Type t = m.getAnnotation(Type.class);
			if (t != null) {
				if (type.isArray()) {
					return processArrayType(state, type, value);

				} else {
					for (String v : t.value()) {
						if (v.equals(typeName)) {
							hasMatch = true;
							Object[] args = new Object[] { value };
							try {
								return m.invoke(null, args);
							} catch (Exception e) {
								if (state.getSavedObjects().containsKey(value)) {
									Object o = state.getSavedObjects().get(
											value);
									if (o.getClass().equals(type)) {
										return o;
									}
								} else {
									return null;
								}
							}
						}
					}
				}
			}
		}

		if (!hasMatch && state.getSavedObjects().containsKey(value)) {
			Object o = state.getSavedObjects().get(value);
			if (o.getClass().equals(type)) {
				return o;
			}
		}

		return null;
	}

	private static Object processArrayType(InspectionState state,
			Class<?> type, String value) {
		Class<?> arrayType = type.getComponentType();
		String[] values;
		Object arr = null;
		if (value.charAt(0) == '[' && value.charAt(value.length() - 1) == ']') {
			value = value.substring(1, value.length() - 1);
			if (arrayType.equals(String.class)) {
				values = Utils.splitArrayStrings(value);
			} else {
				values = value.split(",\\s*");
			}
			arr = Array.newInstance(arrayType, values.length);
		} else {
			return null;
		}

		int index = 0;
		for (String v : values) {
			Object o = processType(state, arrayType, v);

			if (o != null) {
				Array.set(arr, index, o);
			} else {
				return null;
			}
			index++;
		}

		return arr;
	}
}
