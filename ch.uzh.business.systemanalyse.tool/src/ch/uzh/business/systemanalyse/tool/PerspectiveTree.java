package ch.uzh.business.systemanalyse.tool;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import ch.uzh.business.systemanalyse.tool.navigation.TreeOverView;
import ch.uzh.business.systemanalyse.tool.view.welcome.WelcomeView;

/**
 * This perspective shows a navigation tree on the lefthand side
 * 
 * @author alexander.schmid
 * 
 */
public class PerspectiveTree implements IPerspectiveFactory {

	public static final String ID = "ch.uzh.business.systemanalyse.tool.perspectiveTree";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.addView(TreeOverView.ID, IPageLayout.LEFT, 0.15f,
				layout.getEditorArea());
		layout.addView(WelcomeView.ID, IPageLayout.LEFT, 1f,
				layout.getEditorArea());
		// layout.addView(ConsoleView.ID, IPageLayout.BOTTOM, 0.15f,
		// layout.getEditorArea());
	}

}
