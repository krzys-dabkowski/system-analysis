/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.commands;

import java.io.File;
import java.io.IOException;

import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import ch.uzh.business.systemanalyse.tool.view.table.AbstractTableView;

/**
 * Command handler for saving tables (e.g Influences) in an .xls file.
 * 
 * @author krzysztof.dabkowski
 * 
 */
public class SaveTableXls extends AbstractSaveXls {

	private TableViewer tableViewer;
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
		viewPart = (IViewPart) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart();
		if(viewPart instanceof AbstractTableView) {
			shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			File file;
			file = openFile(event);
			
			if(null != file) {
			try {

				tableViewer = ((AbstractTableView)viewPart).getViewer();
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
			}
		}
		return null;
	}

	/**
	 * @param event
	 */
	protected File openFile(ExecutionEvent event) {
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

		return file;
	}

	@Override
	protected void writeContent(WritableSheet excelSheet) throws RowsExceededException, WriteException {

		Table table = tableViewer.getTable();
		int index = 0;
		for(TableColumn column : table.getColumns()) {
			excelSheet.addCell(new Label(index, 0, column.getText(), format));
			index++;
		}

		TableItem[] items = table.getItems();
		int columnCount = table.getColumnCount();
		for (int i = 0; i < items.length; i++) {
			TableItem item = items[i];
			for (int j = 0; j < columnCount; j++) {
				try {
					float value = Float.valueOf(item.getText(j));
					excelSheet.addCell(new Number(j, i + 1, value, format));

				} catch (NumberFormatException e) {
					excelSheet.addCell(new Label(j, i + 1, item.getText(j), format));

				}
			}
		}
	}



	@Override
	protected String getSheetName() {
		if(viewPart == null) {
			return "Table";
		} else {
			return viewPart.getTitle();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.AbstractHandler#isEnabled()
	 */
	/*@Override
	public boolean isEnabled() {
		IViewPart currentViewPart = (IViewPart) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart();
		boolean en = currentViewPart instanceof AbstractTableView;
		System.out.println(en);
		return en;
	}*/
	
	

}
