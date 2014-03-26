package ist.meic.pa;


import ist.meic.pa.commands.Command;
import ist.meic.pa.exceptions.InvalidArgumentException;
import ist.meic.pa.exceptions.QuitException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Scanner;

public class Inspector {
	
	private InspectionState state;
	
	public void inspect(Object object) throws IllegalArgumentException, IllegalAccessException {
		
		if (object != null) {
			this.state = new InspectionState(object);
			
			Utils.dumpObject(object);
			
			prompt();
		} else {
			System.err.println("The object you want to inspect is null.");
		}
		
	}
	
	private void prompt() {
		
		Scanner scanner = new Scanner(System.in);
		
		while (true) {
			System.err.print("> ");
			String[] command = scanner.nextLine().split(" +");
			
			try {
				executeCommand(command);
			} catch (ClassNotFoundException e) {
				System.err.println("ERROR: Command not found.");
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				System.err.println("ERROR: Invalid syntax.");
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (QuitException e) {
				break;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		Utils.dumpObject(this.state.getCurrentObject());
	}

	public static void main(String[] args) {
		Test s = new Test();
		
		try {
			new Inspector().inspect(s);
			System.out.println("Finished inspection and program");
			System.out.println(1 + 1);
		} catch (IllegalArgumentException e1) {
			
		} catch (IllegalAccessException e1) {
			
		}
	}

}
