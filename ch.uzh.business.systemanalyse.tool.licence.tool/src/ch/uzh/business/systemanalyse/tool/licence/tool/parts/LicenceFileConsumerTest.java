/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.licence.tool.parts;

import ch.uzh.business.systemanalyse.tool.licence.tool.filegenerator.LicenceException;

/**
 * @author Krzysztof Dabkowski (kdabko@yahoo.com)
 * 
 */
public class LicenceFileConsumerTest {

	/**
	 * @param args
	 * @throws LicenceException
	 */
	public static void main(String[] args) throws LicenceException {
		LicenceFileConsumer consumer = new LicenceFileConsumer();
		System.out
				.println(consumer
						.consumeLicence("C:\\Users\\krzysztof.dabkowski\\Desktop\\licence.dat"));
	}

}
