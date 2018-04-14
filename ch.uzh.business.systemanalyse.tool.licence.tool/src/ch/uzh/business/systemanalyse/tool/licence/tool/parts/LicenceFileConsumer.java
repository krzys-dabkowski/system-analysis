/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.licence.tool.parts;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import ch.uzh.business.systemanalyse.tool.licence.tool.filegenerator.Licence;
import ch.uzh.business.systemanalyse.tool.licence.tool.filegenerator.LicenceException;

/**
 * @author Krzysztof Dabkowski (kdabko@yahoo.com)
 * 
 */
public class LicenceFileConsumer {

	public LicenceFileConsumer() {

	}

	public boolean consumeLicence(String filePath) throws LicenceException {

		Licence licence = readFile(filePath);
		long serverTime = getTimeOnline();

		if (serverTime != 0 && licence.getTime() > serverTime) {
			return true;
		} else {
			return false;
		}

	}

	private long getTimeOnline() throws LicenceException {
		String TIME_SERVER = "time-a.nist.gov";
		NTPUDPClient timeClient = new NTPUDPClient();
		try {
			InetAddress inetAddress = InetAddress.getByName(TIME_SERVER);
			TimeInfo timeInfo = timeClient.getTime(inetAddress);
			long returnTime = timeInfo.getReturnTime();
			Date time = new Date(returnTime);
			return time.getTime();
		} catch (UnknownHostException e) {
			MessageDialog
					.openError(Display.getDefault().getActiveShell(),
							"Error getting server time",
							"Could not get time from the server. Check your internet connection.");
			throw new LicenceException(e);
		} catch (IOException e) {
			MessageDialog.openError(Display.getDefault().getActiveShell(),
					"Error getting server time",
					"Could not get time from the server.");
			throw new LicenceException(e);
		}
	}

	private Licence readFile(String filePath) throws LicenceException {

		try {
			FileInputStream fin = new FileInputStream(filePath);

			ObjectInputStream ois = new ObjectInputStream(fin);
			Object object = ois.readObject();
			if (object.getClass() == Licence.class) {
				return (Licence) object;
			} else {
				throw new ClassNotFoundException();
			}
		} catch (FileNotFoundException e) {
			MessageDialog.openError(Display.getDefault().getActiveShell(),
					"Error evaluating licence",
					"Cound not find specified file.");
			throw new LicenceException(e);
		} catch (IOException e) {
			MessageDialog.openError(Display.getDefault().getActiveShell(),
					"Error evaluating licence",
					"Cound not read specified file.");
			throw new LicenceException(e);
		} catch (ClassNotFoundException e) {
			MessageDialog.openError(Display.getDefault().getActiveShell(),
					"Error evaluating licence",
					"The specified file does not contain a licence.");
			throw new LicenceException(e);
		}

	}
}
