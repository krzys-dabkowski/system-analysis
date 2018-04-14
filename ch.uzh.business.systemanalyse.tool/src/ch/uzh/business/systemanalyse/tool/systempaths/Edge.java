/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.systempaths;

/**
 * Tarjan element.
 * 
 * @author krzysztof.dabkowski
 *
 */
public class Edge {//implements Comparable<Edge> {

	final private Node from;
	final private Node to;
	//final private int weight;
	
	public Edge(Node argFrom, Node argTo) {
		this.from = argFrom;
		this.to = argTo;
		//this.weight = argWeight;
	}

	public Node getFrom() {
		return from;
	}

	public Node getTo() {
		return to;
	}

	@Override
	public String toString() {
		return "edge from " + from + " to " + to;
	}

	/*public int getWeight() {
		return weight;
	}*/

	/*@Override
	public int compareTo(Edge argEdge) {
		return weight - argEdge.getWeight();
	}*/
	
	
}
