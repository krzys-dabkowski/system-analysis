/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.preferences;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ch.uzh.business.systemanalyse.tool.Activator;
import ch.uzh.business.systemanalyse.tool.licence.tool.filegenerator.LicenceException;
import ch.uzh.business.systemanalyse.tool.licence.tool.parts.LicenceFileConsumer;

/**
 * @author Krzysztof Dabkowski (kdabko@yahoo.com)
 * 
 */
public class LicencePreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	FileFieldEditor licenceFieldEditor;

	@Override
	protected void createFieldEditors() {
		licenceFieldEditor = new FileFieldEditor("LICENCE_FILE",
				"Licence file", getFieldEditorParent());
		addField(licenceFieldEditor);

	}

	@Override
	protected Control createContents(Composite parent) {

		super.createContents(parent);
		Button button = new Button(parent, SWT.PUSH);
		button.setText("Validate");
		GridData gd = new GridData(SWT.RIGHT, SWT.TOP, true, true);
		button.setLayoutData(gd);
		button.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				LicenceFileConsumer consumer = new LicenceFileConsumer();
				try {
					String message;
					if (consumer.consumeLicence(licenceFieldEditor
							.getStringValue())) {
						message = "The licence is valid";
					} else {
						message = "the licence is invalid. Choose another file.";
					}
					MessageDialog.openInformation(Display.getDefault()
							.getActiveShell(), "Licence validation", message);
				} catch (LicenceException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		return parent;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Select a valid licence file.");
	}

}
