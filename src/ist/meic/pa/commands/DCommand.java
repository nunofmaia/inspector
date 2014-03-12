package ist.meic.pa.commands;

import ist.meic.pa.Utils;

public class DCommand extends Command {

	public DCommand(Object obj, String[] args) {
		super(obj, args);
	}

	@Override
	public Object execute() throws IllegalArgumentException, IllegalAccessException {
		
		Utils.dumpObject(this.obj);
		
		return this.obj;
	}
}
