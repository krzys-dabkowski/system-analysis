package ch.uzh.business.systemanalyse.tool;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.handlers.IHandlerService;

import ch.uzh.business.systemanalyse.tool.commands.ShowWelcomeWizard;

/**
 * This class manages the Workbench.
 * 
 * @author krzysztof.dabkowski
 * 
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String PERSPECTIVE_ID = "ch.uzh.business.systemanalyse.tool.perspectiveTree";

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
//		IHandlerService handlerService = (IHandlerService) configurer
//				.getWindow().getService(IHandlerService.class);
//		try {
//			handlerService.executeCommand(ShowWelcomeWizard.ID, null);
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		} catch (NotDefinedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NotEnabledException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NotHandledException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}

}
