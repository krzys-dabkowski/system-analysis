/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.policyonoff;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Krzysztof Dabkowski (kdabko@yahoo.com)
 * 
 */
public class StopCalculationWindow extends Window {

	private final String title;

	private Control windowArea;
	private Control buttonBar;

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, 0);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label label = new Label(composite, SWT.NONE);
		label.setText("wait...");

		buttonBar = createButtonBar(composite);

		// getShell().setSize(parent.getBounds().width,
		// parent.getBounds().height);
		return composite;
	}

	protected StopCalculationWindow(Shell parentShell) {
		super(parentShell);
		title = "Calculation";
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (title != null) {
			shell.setText(title);
		}
	}

	private Control createButtonBar(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(0) // this is incremented
				// by createButton
				.equalWidth(true).applyTo(composite);

		GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).span(2, 1)
				.applyTo(composite);
		composite.setFont(parent.getFont());
		// Add the buttons to the button bar.
		createButtonsForButtonBar(composite);
		return composite;
	}

	private void createButtonsForButtonBar(Composite composite) {
		createButton(composite, "OK", true, new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				close();
			}
		});

	}

	protected Button createButton(Composite parent, String label,
			boolean defaultButton, SelectionListener selectionListener) {
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());
		button.addSelectionListener(selectionListener);
		if (defaultButton) {
			Shell shell = parent.getShell();
			if (shell != null) {
				shell.setDefaultButton(button);
			}
		}
		// setButtonLayoutData(button);
		return button;
	}
}
