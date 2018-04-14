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
public class Node implements Comparable<Node> {

	final int name;
	int lowLink = -1;
	int index = -1;
	
	public Node(final int name) {
		this.name = name;
	}
	
	@Override
	public int compareTo(Node other) {
		return other == this ? 0 : -1;
	}

	@Override
	public String toString() {
		return String.valueOf(name);
	}

	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Node)) {
			return false;
		}
		Node other = (Node) obj;
		if (name != other.name) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + name;
		return result;
	}
}
