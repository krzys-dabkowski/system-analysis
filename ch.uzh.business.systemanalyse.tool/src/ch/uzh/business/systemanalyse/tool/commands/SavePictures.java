package ch.uzh.business.systemanalyse.tool.commands;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import ch.uzh.business.systemanalyse.tool.Activator;
import ch.uzh.business.systemanalyse.tool.view.chart.AbstractChartView;
import ch.uzh.business.systemanalyse.tool.view.paths.AbstractPathTable;
import ch.uzh.business.systemanalyse.tool.view.policyonoff.PolicyComposite;
import ch.uzh.business.systemanalyse.tool.view.policyonoff.PolicyOnOffView;
import ch.uzh.business.systemanalyse.tool.view.table.AbstractTableView;

/**
 * 
 */

/**
 * Saves analyses as pictures under the location specified
 * under Preferences/Save pictures/Directory
 * 
 * @author Krzysztof Dabkowski
 * 
 */
public class SavePictures extends AbstractHandler {

	private String path;
	private IViewPart viewPart;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		path = Activator.getDefault().getPreferenceStore()
				.getString("SAVE_PATH");
		if (path == null || path.equals("")) {
			MessageDialog
					.openWarning(
							PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow().getShell(),
							"Error saving images",
							"Before saving, you have to set the directory, "
									+ "where you want to store the images. You can do "
									+ "it selecting \"Preferences\" menu and go to \"Save pictures\" page.");
			return null;
		}

		viewPart = (IViewPart) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart();// (IViewPart)
																			// r.getPart(true);
		if (viewPart instanceof AbstractTableView) {
			AbstractTableView impactTableView = (AbstractTableView) viewPart;
			printTable(impactTableView.getViewer().getTable());

		}

		if (viewPart instanceof AbstractPathTable) {
			AbstractPathTable impactTableView = (AbstractPathTable) viewPart;
			printTable(impactTableView.getViewer().getTable());
		}

		if (viewPart instanceof AbstractChartView) {
			AbstractChartView view = (AbstractChartView) viewPart;
			JFreeChart chart = view.getChart();
			File file = new File(path + "\\" + view.getTitle() + ".png");
			try {
				ChartUtilities.saveChartAsPNG(file, chart, view.getSize().x,
						view.getSize().y);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (viewPart instanceof PolicyOnOffView) {
			PolicyOnOffView view = (PolicyOnOffView) viewPart;
			int count = 1;
			for (PolicyComposite pc : view.getPolicies()) {
				if (pc.getComposite().isVisible()) {
					GC gc = new GC(pc.getComposite());
					Image image = new Image(PlatformUI.getWorkbench()
							.getDisplay(), pc.getComposite().getSize().x, pc
							.getComposite().getSize().y);
					gc.copyArea(image, 0, 0);
					gc.dispose();

					ImageData imageData = image.getImageData();
					ImageLoader imageLoader = new ImageLoader();
					imageLoader.data = new ImageData[] { imageData };
					String filename = "policy_on_off" + count + ".png";
					imageLoader.save(path + "\\" + filename, SWT.IMAGE_PNG);
					System.out.println("saved " + path + "\\" + filename);
					count++;
				}
			}
		}

		// }

		return null;
	}

	private void printTable(Table table) {
		try {
		GC gc = new GC(table);
		Image image = new Image(PlatformUI.getWorkbench().getDisplay(),
				table.getSize().x, table.getSize().y);
		gc.copyArea(image, 0, 0);
		gc.dispose();

		ImageData imageData = image.getImageData();
		ImageLoader imageLoader = new ImageLoader();
		imageLoader.data = new ImageData[] { imageData };
		String filename = viewPart.getTitle() + ".png";
		imageLoader.save(path + "\\" + filename, SWT.IMAGE_PNG);
		System.out.println("saved " + path + "\\" + filename);
		} catch (Exception e) {
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					"Error Saving pictures",
					"The specified directory does not exist. Set a correct location under Preferences/Save pictures/Directory.");
		}
		/*catch (Exception e) {
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error Saving pictures", "Error encountered saving the pictures.");
		}*/
	}
}
