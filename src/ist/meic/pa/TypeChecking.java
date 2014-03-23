package ist.meic.pa;

import ist.meic.pa.annotations.Type;

public class TypeChecking {

	@Type({ "int", "Integer" })
	public static Object processInt(String value) {
		return Integer.parseInt(value);
	}
	
	@Type({ "float", "Float" })
	public static Object processFloat(String value) {
		return Float.parseFloat(value);
	}
	
	@Type({ "long", "Long" })
	public static Object processLong(String value) {
		return Long.parseLong(value);
	}
	
	@Type({ "double", "Double" })
	public static Object processDouble(String value) {
		return Double.parseDouble(value);
	}
	
	@Type({ "boolean", "Boolean" })
	public static Object processBoolean(String value) {
		return Boolean.parseBoolean(value);
	}
	
	@Type({ "char", "Character" })
	public static Object processChar(String value) {
		String stripped = value.substring(1, value.length() - 1);
		return new Character(stripped.toCharArray()[0]);
	}
	
	@Type("String")
	public static Object processString(String value) {
		return value.substring(1, value.length() - 1);
	}
}
