package ch.uzh.business.systemanalyse.tool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class ConsoleView extends ViewPart {

	public static final String ID = "ch.uzh.business.systemanalyse.tool.console";

	private Text text;

	@Override
	public void createPartControl(Composite parent) {
		text = new Text(parent, SWT.READ_ONLY | SWT.MULTI);
		// OutputStream out = new OutputStream() {
		// @Override
		// public void write(int b) throws IOException {
		// if (text.isDisposed())
		// return;
		// text.append(String.valueOf((char) b));
		// }
		// };
		// final PrintStream oldOut = System.out;
		// System.setOut(new PrintStream(out));
		// text.addDisposeListener(new DisposeListener() {
		// @Override
		// public void widgetDisposed(DisposeEvent e) {
		// System.setOut(oldOut);
		// }
		// });
	}

	@Override
	public void setFocus() {
		text.setFocus();
	}
}