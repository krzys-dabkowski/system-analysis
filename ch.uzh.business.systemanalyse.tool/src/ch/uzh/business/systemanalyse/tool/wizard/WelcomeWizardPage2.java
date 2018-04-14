/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

import ch.uzh.business.systemanalyse.tool.IProjectSettingsConstants;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class WelcomeWizardPage2 extends WizardPage {

	private Composite container;
	private WelcomeWizard wizard;
	private Spinner spinner;
	PropertyChangeListener propertyListener = new PropertyChangeListener() {
		
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if(evt.getPropertyName().equals("newModel")) {
				if((Boolean)evt.getNewValue() == true) {
					spinner.setEnabled(true);
				} else {
					spinner.setEnabled(false);
				}
			}
		}
	};
	
	public WelcomeWizardPage2() {
		super("Second Page");
		setTitle("Create new model");
		setDescription("Select number of variables");
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		wizard = (WelcomeWizard)getWizard();
		container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;
		Label label = new Label(container, SWT.NULL);
		label.setText("Number of variables");
		Composite spinnerComposite = new Composite(container, SWT.NULL);
		GridLayout spinnerLayout = new GridLayout();
		spinnerLayout.numColumns = 2;
		spinnerComposite.setLayout(spinnerLayout);
		spinner = new Spinner(spinnerComposite, SWT.BORDER);
		spinner.setMinimum(0);
		spinner.setMaximum(IProjectSettingsConstants.MAX_VALUE_NEW_MODEL);
		spinner.setSelection(0);
		spinner.setIncrement(1);
		spinner.setPageIncrement(5);
		spinner.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				setPageComplete(true);
				wizard.setNewModelSize(((Spinner)e.getSource()).getSelection());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		Label spinnerComment = new Label(spinnerComposite, SWT.NULL);
		spinnerComment.setText("NOTE: maximum number of variables is " + IProjectSettingsConstants.MAX_VALUE_NEW_MODEL);
		setControl(container);
		setPageComplete(false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		if(visible) {
			setPageComplete(false);
		} else {
			setPageComplete(true);
		}
		super.setVisible(visible);
	}
	
}
