package ist.meic.pa;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class InspectionState {
	private Object currentObject;
	private Stack<Object> history;
	private Map<String, Object> savedObjects;
	
	public InspectionState(Object object) {
		this.currentObject = object;
		this.history = new Stack<Object>();
		this.savedObjects = new HashMap<String, Object>();
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
	
	public Map<String, Object> getSavedObjects() {
		return savedObjects;
	}
}
