package ist.meic.pa.commands;

import ist.meic.pa.InspectionState;
import ist.meic.pa.Node;
import ist.meic.pa.Utils;

public class UCommand extends Command {

	public UCommand(InspectionState state, String[] args) {
		super(state, args);
	}

	@Override
	public InspectionState execute() throws Exception {
		
		Node currentNode = this.state.getCurrentNode();
		Node previousNode = currentNode.getPrevious();
		
		if (previousNode != null) {
			this.state.setCurrentNode(previousNode);			
		}
		
		Utils.dumpObject(this.state.getCurrentObject());
		return this.state;
	}

	@Override
	public String usage() {
		return "Usage: u";
	}

}
