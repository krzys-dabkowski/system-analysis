package ch.uzh.business.systemanalyse.tool.commands;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * Abstract command handler for saving data in an Excel file.
 * It stores the common functionality of such a command.
 * 
 * @author krzysztof.dabkowski
 *
 */
public abstract class AbstractSaveXls extends AbstractHandler {

	protected Shell shell;
	protected String filePath;
	static protected WritableCellFormat format = new WritableCellFormat(
			new WritableFont(WritableFont.ARIAL, 10));

	public AbstractSaveXls() {
		super();
	}

	protected String findFile(ExecutionEvent event) {
		FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
		fileDialog.setText("Select File To Save");
		fileDialog.setFilterExtensions(new String[] { "*.xls" });
		return fileDialog.open();
	}

	protected void writeToFile(File file) throws IOException,
			WriteException {
				WorkbookSettings wbSettings = new WorkbookSettings();
				wbSettings.setLocale(new Locale("en", "EN"));
			
				WritableWorkbook workbook;
				workbook = Workbook.createWorkbook(file);
				workbook.createSheet(getSheetName(), 0);
				WritableSheet excelSheet = workbook.getSheet(0);
			
				writeContent(excelSheet);
				
				
				workbook.write();
				workbook.close();
			}

	abstract protected String getSheetName();

	abstract protected void writeContent(WritableSheet excelSheet) throws RowsExceededException, WriteException; 

}