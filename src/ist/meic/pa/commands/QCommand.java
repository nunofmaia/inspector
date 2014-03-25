package ist.meic.pa.commands;

import ist.meic.pa.InspectionState;
import ist.meic.pa.exceptions.QuitException;

public class QCommand extends Command {

	public QCommand(InspectionState state, String[] args) {
		super(state, args);
		// TODO Auto-generated constructor stub
	}

	@Override
	public InspectionState execute() throws QuitException {
		
		throw new QuitException();
	}

}
