/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ch.uzh.business.systemanalyse.tool.Activator;

/**
 * An abstract preference page for setting the allowed values of
 * impact and time.
 * 
 * @author Krzysztof Dabkowski
 *
 */
public abstract class AbstractValuesPreferencePage extends PreferencePage implements
IWorkbenchPreferencePage {

	
	private List delayValuesList;

	private Text newEntryText;
	/**
	 * 
	 */


	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {

		Composite entryTable = new Composite(parent, SWT.NULL);

		//Create a data that takes up the extra space in the dialog .
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.grabExcessHorizontalSpace = true;
		entryTable.setLayoutData(data);

		GridLayout layout = new GridLayout();
		entryTable.setLayout(layout);			

		//Add in a dummy label for spacing
		new Label(entryTable,SWT.NONE);

		delayValuesList = new List(entryTable, SWT.BORDER);

		delayValuesList.setItems(convert(getPreferenceStore().getString(getPreferenceName())));

		//Create a data that takes up the extra space in the dialog and spans both columns.
		data = new GridData(GridData.FILL_BOTH);
		delayValuesList.setLayoutData(data);

		Composite buttonComposite = new Composite(entryTable,SWT.NULL);

		GridLayout buttonLayout = new GridLayout();
		buttonLayout.numColumns = 2;
		buttonComposite.setLayout(buttonLayout);

		//Create a data that takes up the extra space in the dialog and spans both columns.
		data = new GridData(GridData.FILL_BOTH | GridData.VERTICAL_ALIGN_BEGINNING);
		buttonComposite.setLayoutData(data);		

		Button addButton = new Button(buttonComposite, SWT.PUSH | SWT.CENTER);

		addButton.setText("Add to List"); //$NON-NLS-1$
		addButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if(newEntryText.getText() != null && newEntryText.getText() != "" && !newEntryText.getText().trim().isEmpty())
				delayValuesList.add(newEntryText.getText(), delayValuesList.getItemCount());
			}
		});

		newEntryText = new Text(buttonComposite, SWT.BORDER);
		//Create a data that takes up the extra space in the dialog .
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.grabExcessHorizontalSpace = true;
		newEntryText.setLayoutData(data);


		Button removeButton = new Button(buttonComposite, SWT.PUSH | SWT.CENTER);

		removeButton.setText("Remove Selection"); //$NON-NLS-1$
		removeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				delayValuesList.remove(delayValuesList.getSelectionIndex());
			}
		});

		data = new GridData();
		data.horizontalSpan = 2;
		removeButton.setLayoutData(data);

		return entryTable;
	}
	
	abstract protected String getPreferenceName();

	/**
	 * Performs special processing when this page's Restore Defaults button has been pressed.
	 * Sets the contents of the nameEntry field to
	 * be the default 
	 */
	protected void performDefaults() {
		delayValuesList.setItems(getDefaults());
	}
	/** 
	 * Method declared on IPreferencePage. Save the
	 * author name to the preference store.
	 */
	public boolean performOk() {
		Activator.getDefault().getPreferenceStore().setValue(getPreferenceName(), convert(delayValuesList.getItems()));
		return super.performOk();
	}

	abstract protected String[] getDefaults();

	private String convert(String [] list) {
		String string = null;
		if(list.length > 0){
			string = "";
			for(int i = 0; i < list.length; i++) {
				if(i == 0) {
					string = string + list[i];
				} else {
					string = string + "v" + list[i];
				}
			}
		}
		return string;
	}

	private String [] convert(String string) {
		return string.split("v");
	}

}
