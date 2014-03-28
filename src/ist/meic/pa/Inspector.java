package ist.meic.pa;


import ist.meic.pa.commands.Command;
import ist.meic.pa.exceptions.InvalidArgumentException;
import ist.meic.pa.exceptions.QuitException;
import ist.meic.pa.exceptions.TooManyMethodsException;
import ist.meic.pa.exceptions.WrongTypeException;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Scanner;

public class Inspector {
	
	private InspectionState state;
	
	public void inspect(Object object) {

		try {
			if (object != null) {
				this.state = new InspectionState(object);

				Utils.dumpObject(object);
				prompt();

			} else {
				System.err.println("The object you want to inspect is null.");
			}
		} catch (IllegalAccessException e) {
			
		}

	}
	
	private void prompt() {
		
		Scanner scanner = new Scanner(System.in);
		
		while (true) {
			System.err.print("> ");
			String[] command = Utils.splitInput(scanner.nextLine());
			
			try {
				executeCommand(command);
			} catch (ClassNotFoundException e) {
				System.err.println("Command not found.");
			} catch (QuitException e) {
				break;
			} catch (Exception e) {
				System.err.println("Something went wrong...");
			}			
		}
		
		scanner.close();
	}

	private void executeCommand(String[] command) throws Exception {
		String className = "ist.meic.pa.commands." + command[0].toUpperCase() + "Command";
		
		Class<?> c = Class.forName(className);
		Constructor<?> constructor = c.getConstructors()[0];
		Object[] args = new Object[] { this.state, Arrays.copyOfRange(command, 1, command.length) };
		Command cmd = (Command) constructor.newInstance(args);
		
		try {
			this.state = cmd.execute();
			
		} catch (InvalidArgumentException e) {
			System.err.println(cmd.usage());
		} catch (NoSuchFieldException e) {
			System.err.println("The inspected object does not have the specified field.");
		} catch (NoSuchMethodException e) {
			System.err.println("The inspected object does not have the specified method.");
		} catch (TooManyMethodsException e) {
			System.err.println("There are too many methods to make a decision.");
		} catch (WrongTypeException e) {
			System.err.println("The value does not match the type of the field.");
		}

	}

}
