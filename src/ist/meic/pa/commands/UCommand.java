package ist.meic.pa.commands;

import ist.meic.pa.InspectionState;
import ist.meic.pa.Node;

public class UCommand extends Command {

	public UCommand(InspectionState state, String[] args) {
		super(state, args);
		// TODO Auto-generated constructor stub
	}

	@Override
	public InspectionState execute() throws Exception {
		// TODO Auto-generated method stub
		Node currentNode = this.state.getCurrentNode();
		Node previousNode = currentNode.getPrevious();
		
		if (previousNode != null) {
			this.state.setCurrentNode(previousNode);			
		}
		
		return this.state;
	}

}
