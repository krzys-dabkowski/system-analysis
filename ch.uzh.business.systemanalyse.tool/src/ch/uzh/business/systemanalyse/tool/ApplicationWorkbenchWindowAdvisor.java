package ch.uzh.business.systemanalyse.tool;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	@Override
	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	@Override
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(700, 500));
		configurer.setShowCoolBar(false);
		configurer.setShowStatusLine(false);
		configurer.setTitle("Executive System Analysis");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#openIntro()
	 */
	@Override
	public void openIntro() {

		super.openIntro();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#postWindowOpen()
	 */
	@Override
	public void postWindowOpen() {
		// IHandlerService handlerService =
		// (IHandlerService)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart().getSite().getService(IHandlerService.class);
		// try {
		// handlerService.executeCommand(ShowWelcomeWizard.ID, null);
		// } catch (ExecutionException e) {
		// e.printStackTrace();
		// } catch (NotDefinedException e) {
		// e.printStackTrace();
		// } catch (NotEnabledException e) {
		// e.printStackTrace();
		// } catch (NotHandledException e) {
		// e.printStackTrace();
		// }
		super.postWindowOpen();
	}

	@Override
	public void postWindowCreate() {

		super.postWindowCreate();
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		try {
			configurer.getWindow().getShell().setMaximized(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean preWindowShellClose() {
		IWorkbenchPage page = (IWorkbenchPage) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();

		page.resetPerspective();
		return super.preWindowShellClose();
	}
}
