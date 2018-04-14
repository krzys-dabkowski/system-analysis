/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.model;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

import ch.uzh.business.systemanalyse.tool.IProjectSettingsConstants;
import ch.uzh.business.systemanalyse.tool.commands.SaveAsHandler;

/**
 * This is a command handler. Exceptionally, this handler has been put into the
 * <b>model</b> package, although all the other handlers have been put into the
 * <b>commands</b> package. The reason for that is, that this class needs an
 * access to the constructors of {@link Variable}.
 * 
 * LoadModelHandler reads a saved in .xls delay and impact tables and restores
 * an application model out of it.
 * 
 * 
 * @author Krzysztof Dabkowski
 * 
 */
public class LoadModelHandler extends AbstractHandler {

	public final static String ID = "ch.uzh.business.systemanalyse.tool.commands.loadmodel";

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

			FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
			String fileName = fileDialog.open();
			if (fileName == null) {
				return null;
			}
			File file = new File(fileName);
			Workbook w;
			try {
				w = Workbook.getWorkbook(file);
				Sheet sheetImpact = w.getSheet(0);
				Sheet sheetDelay = w.getSheet(1);
				List<Variable> variables = ModelProvider.INSTANCE
						.getVariables();
				ModelProvider.INSTANCE.clear();
				for (int i = 0; i < sheetImpact.getColumns() - 2; i++) {
					Cell indexCell = sheetImpact.getCell(0, i + 1);
					Cell nameCell = sheetImpact.getCell(1, i + 1);
					int index = -1;
					String name = "";
					if (indexCell.getType() == CellType.NUMBER) {
						index = Integer.valueOf(indexCell.getContents());
					}
					if (nameCell.getType() == CellType.LABEL) {
						name = nameCell.getContents();
					}
					Variable var;
					if (index == -1) {
						var = new Variable(name);
					} else {
						var = new Variable(name, index);
						// var = new Variable(name);
						// TODO: change to read with index and load dependencies
						// for
						// correct indexes...
					}
					for (int j = 2; j < sheetImpact.getColumns(); j++) {
						String value = sheetImpact.getCell(j, i + 1)
								.getContents();
						float fValue;
						if (value == null || value.equals("")) {
							fValue = 0f;
						} else {
							fValue = Float.valueOf(value.replace(",", "."));
						}
						var.addImpactValue(j - 1, fValue);

						value = sheetDelay.getCell(j, i + 1).getContents();
						if (value == null || value.equals("")) {
							fValue = 0f;
						} else {
							fValue = Float.valueOf(value.replace(",", "."));
						}
						var.addDelayValue(j - 1, fValue);
					}
					variables.add(var);

				}
				ModelProvider.INSTANCE.init();
				ModelProvider.INSTANCE.modelChanged(true);
			} catch (BiffException e) {
				MessageDialog.openError(shell, "Error", e.getMessage());
			} catch (IOException e) {
				MessageDialog.openError(shell, "Error openning the file",
						e.getMessage());
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				String stack = sw.toString(); // stack trace as a string
				MessageDialog
						.openError(
								shell,
								"Wrong file format",
								"The model could not be restored from the file. Could not read the file because of:\n\n"
										+ e.getClass().getSimpleName()
										+ " "
										+ e.getMessage()
										+ "\n\nThe file is probably in wrong format.\n\n\n"
										+ stack);
			}
		} else {
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					IProjectSettingsConstants.LICENCE_INVALID_TITLE,
					IProjectSettingsConstants.LICENCE_INVALID_TEXT);
		}
		return null;

	}

}
