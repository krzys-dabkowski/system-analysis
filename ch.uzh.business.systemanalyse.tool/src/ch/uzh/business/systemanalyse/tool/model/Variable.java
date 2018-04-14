/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.model;

import java.util.ArrayList;


/**
 * An element of the application model storing information on
 * a simple model element. It contains an index, a name and an
 * array of
 * impact values to other variables as well as an array of
 * delay values to other variables. Additionally, arrays
 * shortestPathsDelayArray and shortestPathsImpactArray store
 * the values of the shortest paths from this variable to others.
 * 
 * The static highestUsedIndex variable informs, which index
 * is the highest currently used, so a new variable will get
 * the highestUsedIndex + 1 'th index.
 * 
 * @author Krzysztof Dabkowski
 *
 */
public class Variable {

	//private static int counter = 1;
	private static int highestUsedIndex = 0;

	private int index;
	private String name;
	
	private ArrayList<VariableValue> impactArray;
	private ArrayList<VariableValue> delayArray;
	private ArrayList<VariableValue> shortestPathsDelayArray;
	private ArrayList<VariableValue> shortestPathsImpactArray;

	/**
	 * The simplest constructor. The index is "generated" from the
	 * the highestUsedIndex static variable.
	 * @param name
	 */
	Variable(String name) {
		//this.index = counter++;
		this.index = ++highestUsedIndex;
		this.name = name;
		this.impactArray = new ArrayList<VariableValue>();
		this.delayArray = new ArrayList<VariableValue>();
		this.shortestPathsDelayArray = new ArrayList<VariableValue>();
		this.shortestPathsImpactArray = new ArrayList<VariableValue>();
	}

	Variable(String name, int index) {
		//Variable.counter = index + 1;
		this.index = ++highestUsedIndex;
		this.name = name;
		this.index = index;
		this.impactArray = new ArrayList<VariableValue>();
		this.delayArray = new ArrayList<VariableValue>();
		this.shortestPathsDelayArray = new ArrayList<VariableValue>();
		this.shortestPathsImpactArray = new ArrayList<VariableValue>();
	}
	
	void delete() {
		if(index == highestUsedIndex) {
			highestUsedIndex--;
		}
	}
	
	void setIndex(int newIndex) {
		this.index = newIndex;
	}

	public void setName(String name) {
		this.name = name;
		ModelProvider.INSTANCE.modelChanged(false);
	}

	public String getName() {
		return name;
	}

	public int getIndex() {
		return index;
	}

	public ArrayList<VariableValue> getImpactArray() {
		return impactArray;
	}

	public ArrayList<VariableValue> getDelayArray() {
		return delayArray;
	}
	
	public ArrayList<VariableValue> getShortestPathsArray() {
		return shortestPathsDelayArray;
	}
	
	public ArrayList<VariableValue> getStrongestEffectsArray() {
		return shortestPathsImpactArray;
	}

	/**
	 * 
	 * 
	 * @param variableIndex
	 * @param impactValue
	 * @return
	 */
	int addImpactValue(int variableIndex, float impactValue) {

		// Set impactValue for the wished variable
		for(VariableValue tempImpactValue : impactArray) {
			if(tempImpactValue.getVariableIndex() == variableIndex) {
				tempImpactValue.setVariableValue(impactValue);
				
				return 1;
			}
		}

		// Add a new variable impact value if it does not exist yet
		// Do not set any impact for itself (set 0)
		if(variableIndex == index) {
			impactArray.add(new VariableValue(variableIndex, 0, true));
		} else {
			impactArray.add(new VariableValue(variableIndex, impactValue, true));
		}
		
		return 2;
	}

	public int addDelayValue(int variableIndex, float delayValue) {

		// Set delayValue for the wished variable
		for(VariableValue tempImpactValue : delayArray) {
			if(tempImpactValue.getVariableIndex() == variableIndex) {
				tempImpactValue.setVariableValue(delayValue);
				return 1;
			}
		}

		// Add a new variable delay value if it does not exist yet
		// Do not set any delay for itself (set 0)
		if(variableIndex == index) {
			delayArray.add(new VariableValue(variableIndex, 0, true));
		} else {
			delayArray.add(new VariableValue(variableIndex, delayValue, true));
		}
		return 2;
	}
	
	public int addShortestPathValue(int variableIndex, float shortestPathValue) {
		// Set shortestPathValue for the wished variable
		for(VariableValue tempImpactValue : shortestPathsDelayArray) {
			if(tempImpactValue.getVariableIndex() == variableIndex) {
				tempImpactValue.setVariableValue(shortestPathValue);
				return 1;
			}
		}

		// Add a new variable shortest path value if it does not exist yet
		shortestPathsDelayArray.add(new VariableValue(variableIndex, shortestPathValue, false));
		return 2;
	}
	
	public int addStrongestEffectValue(int variableIndex, float strongestEffectValue) {
		// Set shortestPathValue for the wished variable
		for(VariableValue tempImpactValue : shortestPathsImpactArray) {
			if(tempImpactValue.getVariableIndex() == variableIndex) {
				tempImpactValue.setVariableValue(strongestEffectValue);
				return 1;
			}
		}

		// Add a new variable shortest path value if it does not exist yet
		shortestPathsImpactArray.add(new VariableValue(variableIndex, strongestEffectValue, false));
		return 2;
	}
	
	/**
	 * looks up the delay value from the receiver
	 * to the variable specified by the index
	 * passed by the argument.
	 * @param variableIndex
	 * @return
	 */
	public float getDelayValueToVariable(int variableIndex) {
		for(VariableValue value : getDelayArray()) {
			if (value.getVariableIndex() == variableIndex) {
				return value.getVariableValue();
			}
		}
		return Float.NaN;
	}
	
	/**
	 * looks up the impact value from the receiver
	 * to the variable specified by the index
	 * passed by the argument.
	 * @param variableIndex
	 * @return
	 */
	public float getImpactValueToVariable(int variableIndex) {
		for(VariableValue value : getImpactArray()) {
			if (value.getVariableIndex() == variableIndex) {
				return value.getVariableValue();
			}
		}
		return Float.NaN;
	}

	public float calculateActiveSum() {
		float activeSum = 0.0f;
		for(VariableValue iv : impactArray) {
			activeSum += Math.abs(iv.getVariableValue());
		}
		return activeSum;
	}

	public float calculatePassiveSum() {
		float passiveSum = 0.0f;
		for(Variable var : ModelProvider.INSTANCE.getVariables()) {
			for(VariableValue iv : var.getImpactArray()) {
				if(iv.getVariableIndex() == getIndex()) {
					passiveSum += Math.abs(iv.getVariableValue());
				}
				
			}
		}
		return passiveSum;
	}

	public float calculateProducedDelay() {
		float producedDelay = 0.0f;
		float notZeroValues = 0.0f;
		for(VariableValue value : delayArray) {
			producedDelay += Math.abs(value.getVariableValue());
			if(value.getVariableValue() != 0) {
				notZeroValues++;
			}
		}
		return producedDelay/notZeroValues;
	}

	public float calculateReceivedDelay() {
		float receivedDelay = 0.0f;
		float notZeroValues = 0.0f;
		for(Variable var : ModelProvider.INSTANCE.getVariables()) {
			for(VariableValue iv : var.getDelayArray()) {
				if(iv.getVariableIndex() == getIndex()) {
					receivedDelay += Math.abs(iv.getVariableValue());
					if(iv.getVariableValue() != 0) {
						notZeroValues++;
					}
				}
			}
		}
		return receivedDelay/notZeroValues;
	}

	@Override
	public String toString() {
		String impactValues = "";
		for (VariableValue value : impactArray) {
			impactValues += "\nimpact to " + value.getVariableIndex() + " value: " + value.getVariableValue() + " ";
		}
		String delayValues = "";
		for (VariableValue value : delayArray) {
			delayValues += "\ndelay to " + value.getVariableIndex() + " value: " + value.getVariableValue() + " ";
		}
		return "index: " + index + " name: " + name + "impact values:\n" + impactValues + "\ndelay values\n" + delayValues + "\n=================";
	}

	
}
