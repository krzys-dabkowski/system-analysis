/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.licence.tool.filegenerator;

/**
 * @author Krzysztof Dabkowski (kdabko@yahoo.com)
 * 
 */
public class LicenceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LicenceException(Exception e) {
		super(e);
	}

	public LicenceException() {
		super();
	}
}
