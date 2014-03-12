package ist.meic.pa.commands;

import ist.meic.pa.Utils;

import java.lang.reflect.Field;

public class MCommand extends Command {

	public MCommand(Object obj, String[] args) {
		super(obj, args);
	}

	@Override
	public Object execute() throws IllegalArgumentException, IllegalAccessException {
		// TODO Auto-generated method stub
		if (this.args.length > 1) {
			String attr = this.args[0];
			String value = this.args[1];
			for (Field f : Utils.getAllFields(this.obj)) {
				if (f.getName().equals(attr)) {
					f.setAccessible(true);
					Class<?> c = f.get(this.obj).getClass();
					if (c.getSimpleName().equals("Integer")) {
							 f.set(this.obj, Integer.parseInt(value));
					}
					
					Utils.dumpObject(this.obj);
					break;
				}
			}
		}
		return this.obj;

	}

}
