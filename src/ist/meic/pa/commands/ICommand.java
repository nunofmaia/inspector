package ist.meic.pa.commands;

import java.lang.reflect.Field;

import ist.meic.pa.Utils;

public class ICommand extends Command {

	public ICommand(Object obj, String[] args) {
		super(obj, args);
	}

	@Override
	public Object execute() throws IllegalArgumentException, IllegalAccessException {
		if (this.args.length > 0) {
			 String attr = this.args[0];
			 for (Field f : Utils.getAllFields(this.obj)) {
				 if (f.getName().equals(attr)) {
					 f.setAccessible(true);
					 this.obj = f.get(this.obj);
					 System.err.println(this.obj);
					 break;
				 }
			 }
		}
		return this.obj;
	}

}
