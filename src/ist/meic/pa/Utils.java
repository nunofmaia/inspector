package ist.meic.pa;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Utils {
	
	private final static Map<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>();
	static {
	    map.put(boolean.class, Boolean.class);
	    map.put(byte.class, Byte.class);
	    map.put(short.class, Short.class);
	    map.put(char.class, Character.class);
	    map.put(int.class, Integer.class);
	    map.put(long.class, Long.class);
	    map.put(float.class, Float.class);
	    map.put(double.class, Double.class);
	}

	public static ArrayList<Field> getAllFields(Object object) {
		Class<?> c = object.getClass();
		ArrayList<Field> allFields = new ArrayList<Field>();
		while (c != null) {
			for (Field f : c.getDeclaredFields()) {
				allFields.add(f);
			}
			c = c.getSuperclass();
		}
		return allFields;
	}

	public static void dumpObject(Object object) throws IllegalAccessException {
		Class<?> c = object.getClass();
		if (map.containsValue(c)) {
			System.err.println(object);
		} else {
			System.err.println(object + " is an instance of class "
					+ object.getClass());
			System.err.println("--------------");

			for (Field f : Utils.getAllFields(object)) {
				f.setAccessible(true);
				System.err.println(f + " = " + f.get(object));
			}
		}
	}
	
	public static Class<?> getWrapper(Class<?> primitive) {
		return map.get(primitive);
	}
	
}
