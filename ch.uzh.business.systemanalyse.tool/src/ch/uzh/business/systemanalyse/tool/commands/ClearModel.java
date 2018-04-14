package ch.uzh.business.systemanalyse.tool.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

import ch.uzh.business.systemanalyse.tool.IProjectSettingsConstants;
import ch.uzh.business.systemanalyse.tool.model.ModelProvider;

/**
 * 
 */

/**
 * Clears the model calls the delete method on ModelProvider.
 * 
 * @author Krzysztof Dabkowski
 * 
 */
public class ClearModel extends AbstractHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();// HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		if (ModelProvider.INSTANCE.isLicenced()) {

			if (ModelProvider.INSTANCE.getVariables().size() != 0) {
				boolean save = MessageDialog.openQuestion(shell,
						"Save Current Model?",
						"Do you want to save current model in a file?");
				if (save) {
					IHandlerService handlerService = (IHandlerService) PlatformUI
							.getWorkbench().getActiveWorkbenchWindow()
							.getService(IHandlerService.class);
					try {
						handlerService.executeCommand(SaveAsHandler.ID, null);
					} catch (NotDefinedException e) {
						e.printStackTrace();
					} catch (NotEnabledException e) {
						e.printStackTrace();
					} catch (NotHandledException e) {
						e.printStackTrace();
					}
				}
			}

			ModelProvider.INSTANCE.clear();

		} else {
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					IProjectSettingsConstants.LICENCE_INVALID_TITLE,
					IProjectSettingsConstants.LICENCE_INVALID_TEXT);
		}
		return null;
	}

}
