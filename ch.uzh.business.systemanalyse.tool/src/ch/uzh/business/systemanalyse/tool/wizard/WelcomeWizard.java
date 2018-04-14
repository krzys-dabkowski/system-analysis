/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.wizard;

import java.beans.PropertyChangeSupport;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

import ch.uzh.business.systemanalyse.tool.commands.SaveAsHandler;
import ch.uzh.business.systemanalyse.tool.model.LoadModelHandler;
import ch.uzh.business.systemanalyse.tool.model.ModelProvider;

/**
 * The welcome wizard.
 * 
 * @author Krzysztof Dabkowski
 *
 */
public class WelcomeWizard extends Wizard {

	private boolean newModel = false;
	private int newModelSize;
	WizardPage page1;
	WizardPage page2;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		page1 = new WelcomeWizardPage1();
		addPage(page1);
		page2 = new WelcomeWizardPage2();
		propertyChangeSupport.addPropertyChangeListener(((WelcomeWizardPage2)page2).propertyListener);
		addPage(page2);
		super.addPages();
	}
	
	

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			.getActivePage().getActivePart().getSite().getService(IHandlerService.class);
		if(!newModel) {
			try {
				handlerService.executeCommand(LoadModelHandler.ID, null);
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (NotDefinedException e) {
				e.printStackTrace();
			} catch (NotEnabledException e) {
				e.printStackTrace();
			} catch (NotHandledException e) {
				e.printStackTrace();
			}
		} else {
			if(ModelProvider.INSTANCE.getVariables().size() != 0) {
				boolean save = MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						"Save Current Model?", "Do you want to save current model in a file?");
				if(save) {
					try {
						handlerService.executeCommand(SaveAsHandler.ID, null);
					} catch (NotDefinedException e) {
						e.printStackTrace();
					} catch (NotEnabledException e) {
						e.printStackTrace();
					} catch (NotHandledException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
				}
			}
			
			ModelProvider.INSTANCE.clear();
			for(int i = 0; i < newModelSize; i++) {
				/*try {
					handlerService.executeCommand(AddVariableHandler.ID, null);
				} catch (ExecutionException e) {
					e.printStackTrace();
				} catch (NotDefinedException e) {
					e.printStackTrace();
				} catch (NotEnabledException e) {
					e.printStackTrace();
				} catch (NotHandledException e) {
					e.printStackTrace();
				}*/
				ModelProvider.INSTANCE.addVariable();
			}
			ModelProvider.INSTANCE.modelChanged(true);
		}
		
		return true;
	}

	public void setNewModel(boolean newModel) {
		propertyChangeSupport.firePropertyChange("newModel", this.newModel, newModel);
		this.newModel = newModel;
	}

	public boolean isNewModel() {
		return newModel;
	}

	public void setNewModelSize(int newModelSize) {
		this.newModelSize = newModelSize;
	}
}
