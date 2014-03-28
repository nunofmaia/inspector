package ist.meic.pa.commands;

import ist.meic.pa.InspectionState;
import ist.meic.pa.Node;
import ist.meic.pa.Utils;

public class RCommand extends Command {

	public RCommand(InspectionState state, String[] args) {
		super(state, args);
	}

	@Override
	public InspectionState execute() throws Exception {
		
		Node currentNode = this.state.getCurrentNode();
		Node nextNode = currentNode.getNext();
		
		if (nextNode != null) {
			this.state.setCurrentNode(nextNode);			
		}
		
		Utils.dumpObject(this.state.getCurrentObject());
		return this.state;
	}

	@Override
	public String usage() {
		return "Usage: u";
	}

}
