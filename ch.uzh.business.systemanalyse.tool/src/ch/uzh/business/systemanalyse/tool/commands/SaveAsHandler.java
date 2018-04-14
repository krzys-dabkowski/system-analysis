package ch.uzh.business.systemanalyse.tool.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import ch.uzh.business.systemanalyse.tool.IProjectSettingsConstants;
import ch.uzh.business.systemanalyse.tool.model.ModelProvider;
import ch.uzh.business.systemanalyse.tool.model.Variable;

/**
 * 
 */

/**
 * Saves the model in an .xls file. The two matrices need to be saved -
 * Cross-Impact and Cross-Time
 * 
 * @author Krzysztof Dabkowski
 * 
 */
public class SaveAsHandler extends AbstractHandler {

	public final static String ID = "ch.uzh.business.systemanalyse.tool.commands.saveas";

	String filePath;
	Shell shell;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		if (ModelProvider.INSTANCE.isLicenced()) {

			File file;
			boolean proceed = false;
			do {
				filePath = findFile(event);
				if (filePath == null) {
					return null;
				}
				if (!filePath.endsWith(".xls")) {
					filePath = filePath + ".xls";
				}
				file = new File(filePath);
				if (file.exists()) {
					proceed = MessageDialog
							.openQuestion(shell, "File exists...",
									"The file you chose already exists. Do you want to overwrite it?");
				} else {
					proceed = true;
				}
			} while (!proceed);

			try {
				writeToFile(file);
			} catch (IOException e) {
				MessageDialog
						.openError(
								shell,
								"Save Model As...",
								"Problems encountered while accessing the file. "
										+ "Could not write the model.\n\nPlease make sure "
										+ "the file is not used by other program or you do "
										+ "not want to read and write to the same file.");
			} catch (Exception e) {
				// TODO work on this exception
				e.printStackTrace();
			}
		} else {
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					IProjectSettingsConstants.LICENCE_INVALID_TITLE,
					IProjectSettingsConstants.LICENCE_INVALID_TEXT);
		}
		return null;

	}

	private String findFile(ExecutionEvent event) {
		FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
		fileDialog.setText("Select File To Save");
		fileDialog.setFilterExtensions(new String[] { "*.xls" });
		return fileDialog.open();
	}

	private void writeToFile(File file) throws IOException,
			RowsExceededException, WriteException {

		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));

		WritableWorkbook workbook;
		workbook = Workbook.createWorkbook(file);
		workbook.createSheet("Delay", 0);
		WritableSheet excelSheet = workbook.getSheet(0);

		writeLabels(excelSheet);
		writeDelayValues(excelSheet);

		workbook.createSheet("Impact", 0);
		excelSheet = workbook.getSheet(0);

		writeLabels(excelSheet);
		writeImpactValues(excelSheet);

		workbook.write();
		workbook.close();
	}

	private void writeLabels(WritableSheet excelSheet)
			throws RowsExceededException, WriteException {
		// WritableFont font = new WritableFont
		/*
		 * CellView cv = new CellView(); cv
		 */
		List<Variable> variables = ModelProvider.INSTANCE.getVariables();
		for (int i = 0; i < variables.size(); i++) {
			Number number1 = new Number(0, i + 1, variables.get(i).getIndex());
			excelSheet.addCell(number1);
			Number number2 = new Number(i + 2, 0, variables.get(i).getIndex());
			excelSheet.addCell(number2);
			Label label2 = new Label(1, i + 1, String.valueOf(variables.get(i)
					.getName()));
			excelSheet.addCell(label2);
		}
	}

	private void writeDelayValues(WritableSheet excelSheet)
			throws RowsExceededException, WriteException {
		List<Variable> variables = ModelProvider.INSTANCE.getVariables();
		for (int i = 0; i < variables.size(); i++) {
			Variable varFrom = variables.get(i);
			for (int j = 0; j < variables.size(); j++) {
				Variable varTo = variables.get(j);
				float value = varFrom.getDelayValueToVariable(varTo.getIndex());
				if (value != 0) {
					Number number = new Number(j + 2, i + 1, value);
					excelSheet.addCell(number);
				}

			}
		}
	}

	private void writeImpactValues(WritableSheet excelSheet)
			throws RowsExceededException, WriteException {
		List<Variable> variables = ModelProvider.INSTANCE.getVariables();
		for (int i = 0; i < variables.size(); i++) {
			Variable varFrom = variables.get(i);
			for (int j = 0; j < variables.size(); j++) {
				Variable varTo = variables.get(j);
				float value = varFrom
						.getImpactValueToVariable(varTo.getIndex());
				if (value != 0) {
					Number number = new Number(j + 2, i + 1, value);
					excelSheet.addCell(number);
				}
			}
		}
	}

}
