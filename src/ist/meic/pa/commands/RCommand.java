package ist.meic.pa.commands;

import ist.meic.pa.InspectionState;
import ist.meic.pa.Node;

public class RCommand extends Command {

	public RCommand(InspectionState state, String[] args) {
		super(state, args);
		// TODO Auto-generated constructor stub
	}

	@Override
	public InspectionState execute() throws Exception {
		// TODO Auto-generated method stub
		Node currentNode = this.state.getCurrentNode();
		Node nextNode = currentNode.getNext();
		
		if (nextNode != null) {
			this.state.setCurrentNode(nextNode);			
		}
		
		return this.state;
	}

}