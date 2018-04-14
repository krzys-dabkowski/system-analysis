/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.model;


/**
 * Represents a value between two {@link Variable}s.
 * 
 * A value's validated can be set to false in order to
 * indicate validation problems.
 * 
 * @author Krzysztof Dabkowski
 *
 */
public class VariableValue {
	private boolean changesModel;
	private int variableIndex;
	private float variableValue;
	private boolean validated = true; // true if it's 0 but should not be..

	public VariableValue(int variableIndex, float impactValue, boolean changesModel) {
		super();
		this.variableIndex = variableIndex;
		this.setVariableValue(impactValue);
		this.changesModel = changesModel;
	}

	public void setVariableIndex(int variableIndex) {
		this.variableIndex = variableIndex;
	}
	public int getVariableIndex() {
		return variableIndex;
	}

	public void setVariableValue(float value) {
		this.variableValue = value;
		if(changesModel && ModelProvider.INSTANCE != null) {
			ModelProvider.INSTANCE.modelChanged(false);
		}
	}

	public float getVariableValue() {
		return variableValue;
	}

	public boolean isValidated() {
		return validated;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}
}
