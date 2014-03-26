package ist.meic.pa.commands;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Scanner;

import ist.meic.pa.InspectionState;
import ist.meic.pa.Utils;
import ist.meic.pa.exceptions.InvalidArgumentException;

public class ICommand extends Command {

	public ICommand(InspectionState state, String[] args) {
		super(state, args);
	}

	@Override
	public InspectionState execute() throws IllegalArgumentException, IllegalAccessException, InvalidArgumentException {
		if (this.args.length == 1) {
			Object obj = this.state.getCurrentObject();
			String attr = this.args[0];
			ArrayList<Field> fields = Utils.getField(obj, attr);

			if (fields.size() == 0) {
				System.err.println("The field " + attr + " does not exist.");
			} else if (fields.size() == 1) {
				getField(fields.get(0));
			} else {
				System.err.println("There are more than one field with the name " + attr + ". Please enter the number of the field you want to choose.");
				Utils.dumpChoiceList(fields);

				handleChoice(fields);
			}
		} else {
			throw new InvalidArgumentException();
		}
		
		return this.state;
	}

	private void handleChoice(ArrayList<Field> fields) throws IllegalArgumentException, IllegalAccessException {
		Scanner scanner = new Scanner(System.in);
		boolean isActive = true;
		while (isActive) {
			System.err.print("Choice: ");
			String[] command = scanner.nextLine().split(" ");

			try {
				int index = Integer.parseInt(command[0]);
				if (index >= 0 && index < fields.size()) {
					isActive = false;
					getField(fields.get(index));
					
				} else {
					System.err.println("Enter a valid number between 0 and " + (fields.size() - 1) + ".");
				}
			} catch (NumberFormatException e) {
				System.err.println("Enter a valid number between 0 and " + (fields.size() - 1) + ".");
			}
		}
		scanner.close();
	}
	
	private void getField(Field f) throws IllegalArgumentException, IllegalAccessException {
		Object obj = this.state.getCurrentObject();
		f.setAccessible(true);
		obj = f.get(obj);
		this.state.setCurrentObject(obj);
	}

	@Override
	public String usage() {
		return "Usage: i <field>";
	}

}
