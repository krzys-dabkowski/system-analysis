/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.model;

import java.util.ArrayList;

/**
 * This is a representation of a path containing
 * an array of nodes ({@link Variables}) as well as
 * a delay and an effect value of the whole path (the
 * sum of all the contained edges.)
 * 
 * @author Krzysztof Dabkowski
 *
 */
public class Path {

	private int index;
	private float delay;
	private float effect;
	private ArrayList<Integer> nodes;
	/**
	 * @return the delay
	 */
	public float getDelay() {
		return delay;
	}
	
	/**
	 * @param delay the delay to set
	 */
	public void setDelay(float delay) {
		this.delay = delay;
	}
	
	/**
	 * @return the effect
	 */
	public float getEffect() {
		return effect;
	}
	/**
	 * @param effect the effect to set
	 */
	public void setEffect(float effect) {
		this.effect = effect;
	}
	
	/**
	 * @return the nodes
	 */
	public ArrayList<Integer> getNodes() {
		return nodes;
	}
	
	/**
	 * @param nodes the nodes to set
	 */
	public void setNodes(ArrayList<Integer> nodes) {
		this.nodes = nodes;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
	
	@Override
	public String toString() {
		String s = "";
		for(Integer n : nodes) {
			s = s + n + " ";
		}
		return s;
	}
	
	/*@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		
		if(o == null) {
			return false;
		}
		
		if(getClass() != o.getClass()) {
			return false;
		}
		
		Path path = (Path) o;
		if()
	}*/
}
