package ist.meic.pa;

import java.util.HashMap;
import java.util.Map;


public class InspectionState {

	private Node currentNode;
	private Map<String, Object> savedObjects;
	
	public InspectionState(Object object) {
		this.currentNode = new Node(object);
		this.savedObjects = new HashMap<String, Object>();

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
		removeNext();
		this.currentNode.setObject(updatedObject);
	}
	
	public Node getCurrentNode() {
		return this.currentNode;
	}
	
	public void setCurrentNode(Node node) {
		this.currentNode = node;
	}
	
	public Map<String, Object> getSavedObjects() {
		return savedObjects;
	}
	
	public void removeNext() {
		this.currentNode.setNext(null);
	}
}
