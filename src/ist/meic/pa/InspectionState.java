package ist.meic.pa;

import java.util.Stack;

public class InspectionState {
	private Object currentObject;
	private Stack<Object> history;
	
	public InspectionState(Object object) {
		this.currentObject = object;
		this.history = new Stack<Object>();
	}

	public Object getCurrentObject() {
		return currentObject;
	}

	public void setCurrentObject(Object currentObject) {
		Object previous = this.currentObject;
		this.history.push(previous);
		
		this.currentObject = currentObject;
	}
	
	public void updateState(Object updatedObject) {
		this.currentObject = updatedObject;
	}

	public Stack<Object> getHistory() {
		return history;
	}

	public void setHistory(Stack<Object> history) {
		this.history = history;
	}
}
