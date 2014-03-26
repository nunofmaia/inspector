package ist.meic.pa.commands;

import ist.meic.pa.InspectionState;
import ist.meic.pa.exceptions.InvalidArgumentException;
import ist.meic.pa.exceptions.QuitException;

public class QCommand extends Command {

	public QCommand(InspectionState state, String[] args) {
		super(state, args);
	}

	@Override
	public InspectionState execute() throws QuitException, InvalidArgumentException {
		if (args.length == 0) {
			throw new QuitException();			
		} else {
			throw new InvalidArgumentException();
		}
	}
	
	@Override
	public String usage() {
		return "Usage: q";
	}

}
