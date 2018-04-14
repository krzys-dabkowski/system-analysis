/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.licence.tool.filegenerator;

import java.io.Serializable;

/**
 * @author Krzysztof Dabkowski (kdabko@yahoo.com)
 * 
 */
public class Licence implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final long time;

	public Licence(long time) {
		this.time = time;
	}

	public long getTime() {
		return time;
	}
}
