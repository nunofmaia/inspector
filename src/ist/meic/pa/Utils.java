package ist.meic.pa;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Utils {
	
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
		System.err.println(object + " is an instance of class " + object.getClass());
		System.err.println("--------------");
		
		for(Field f : Utils.getAllFields(object)) {
			f.setAccessible(true);
			System.err.println(f + " = " + f.get(object));
		}
	}
}
