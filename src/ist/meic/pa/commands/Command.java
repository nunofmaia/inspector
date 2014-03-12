package ist.meic.pa.commands;

public abstract class Command {
	
	protected Object obj;
	protected String[] args;
	
	public Command(Object obj, String[] args) {
		this.obj = obj;
		this.args = args;
	}
	
	public abstract Object execute() throws IllegalArgumentException, IllegalAccessException; 
}
