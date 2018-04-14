package ch.uzh.business.systemanalyse.tool.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import ch.uzh.business.systemanalyse.tool.wizard.WelcomeWizard;

/**
 * 
 */

/**
 * Command opening the welcome wizard.
 * 
 * @author Krzysztof Dabkowski
 * 
 */
public class ShowWelcomeWizard extends AbstractHandler {

	public final static String ID = "ch.uzh.business.systemanalyse.tool.commands.showwelcomewizard";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		WelcomeWizard wizard = new WelcomeWizard();
		WizardDialog dialog = new WizardDialog(
				HandlerUtil.getActiveShell(event), wizard);
		dialog.open();
		return null;
	}

}
