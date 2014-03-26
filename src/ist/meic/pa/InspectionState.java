package ist.meic.pa;

public class InspectionState {

	private Node currentNode;
	
	public InspectionState(Object object) {
		this.currentNode = new Node(object);
	}

	public Object getCurrentObject() {
		return currentNode.getObject();
	}

	public void setCurrentObject(Object currentObject) {
		Node newNode = new Node(currentObject);
		this.currentNode.setNext(newNode);
		newNode.setPrevious(this.currentNode);
		this.currentNode = newNode;
	}
	
	public void updateState(Object updatedObject) {
		this.currentNode.setObject(updatedObject);
	}
	
	public Node getCurrentNode() {
		return this.currentNode;
	}
	
	public void setCurrentNode(Node node) {
		this.currentNode = node;
	}
}
