package ist.meic.pa;

import ist.meic.pa.annotations.Type;
import ist.meic.pa.exceptions.WrongTypeException;

public class TypeChecking {

	@Type({ "int", "Integer" })
	public static Object processInt(String value) throws WrongTypeException {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new WrongTypeException();
		}
	}
	
	@Type({ "float", "Float" })
	public static Object processFloat(String value) throws WrongTypeException {
		try {
			return Float.parseFloat(value);
		} catch (NumberFormatException e) {
			throw new WrongTypeException();
		}
	}
	
	@Type({ "long", "Long" })
	public static Object processLong(String value) throws WrongTypeException {
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			throw new WrongTypeException();
		}
	}
	
	@Type({ "double", "Double" })
	public static Object processDouble(String value) throws WrongTypeException {
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			throw new WrongTypeException();
		}
	}
	
	@Type({ "boolean", "Boolean" })
	public static Object processBoolean(String value) throws WrongTypeException {
		if (value.toLowerCase().equals("true") || value.toLowerCase().equals("false") ) {
			return Boolean.parseBoolean(value);
		}
		
		throw new WrongTypeException();
	}
	
	@Type({ "char", "Character" })
	public static Object processChar(String value) throws WrongTypeException {
		if (value.charAt(0) == '\'' && value.charAt(value.length() - 1) == '\'') {
			String stripped = value.substring(1, value.length() - 1);
			return new Character(stripped.toCharArray()[0]);
		}
		
		throw new WrongTypeException();
	}
	
	@Type("String")
	public static Object processString(String value) throws WrongTypeException {
		if (value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
			value = value.replaceAll("\\\\\"", "\"");
			return value.substring(1, value.length() - 1);			
		}
		
		throw new WrongTypeException();
	}
}
