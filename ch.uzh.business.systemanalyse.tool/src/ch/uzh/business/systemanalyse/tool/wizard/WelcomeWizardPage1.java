/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class WelcomeWizardPage1 extends WizardPage {

	private Composite container;
	WelcomeWizard wizard;
	
	public WelcomeWizardPage1() {
		super("First Page");
		setTitle("System Analysis model wizard");
		setDescription("Select if you want to create a complete new model or load one from a file.");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		wizard = (WelcomeWizard)getWizard();
		
		container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(new RowLayout(SWT.VERTICAL));
		layout.numColumns = 1;
		Button radio2 = new Button(container, SWT.RADIO);
		radio2.setText("Load system from file");
		radio2.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button button = (Button)e.widget;
				if(button.getSelection()) {
					wizard.setNewModel(false);
					setPageComplete(true);
					wizard.page2.setPageComplete(true);
					wizard.getContainer().updateButtons();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		Button radio1 = new Button(container, SWT.RADIO);
		radio1.setText("Create a new system");
		radio1.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				wizard.setNewModel(true);
				wizard.page2.setPageComplete(false);
				wizard.getContainer().updateButtons();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		setControl(radio2);
		setControl(container);
		setPageComplete(true);

	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
	 */
	@Override
	public boolean canFlipToNextPage() {
		return super.canFlipToNextPage() && wizard.isNewModel();
	}

}
