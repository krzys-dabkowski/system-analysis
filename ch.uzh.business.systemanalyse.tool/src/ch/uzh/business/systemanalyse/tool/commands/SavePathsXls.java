/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import ch.uzh.business.systemanalyse.tool.model.Path;

/**
 * Command handler for saving paths (All paths
 * from A to B, paths from B, paths to B) in an .xls file.
 * 
 * @author krzysztof.dabkowski
 *
 */
public class SavePathsXls extends AbstractSaveXls {
	
	public final static String ID = "ch.uzh.business.systemanalyse.tool.commands.savepathsxls";

	List<Path> paths;
	
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		//paths = ModelProvider.INSTANCE.getFromToPaths();
		paths = findPaths(event);
		shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		//shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		File file;
		boolean proceed = false;
		do {
			filePath = findFile(event);
			if(filePath == null) {
				return null;
			}
			if(!filePath.endsWith(".xls")) {
				filePath = filePath + ".xls";
			}
			file = new File(filePath);
			if(file.exists()) {
				proceed = MessageDialog.openQuestion(shell, "File exists...", "The file you chose already exists. Do you want to overwrite it?");
			} else {
				proceed = true;
			}
		} while(!proceed);

		try {
			writeToFile(file);
		} catch (IOException e) {
			MessageDialog.openError(shell, "Save Model As...",
					"Problems encountered while accessing the file. " +
					"Could not write the model.\n\nPlease make sure " +
					"the file is not used by other program or you do " +
					"not want to read and write to the same file.");
		} catch (Exception e) {
			// TODO work on this exception
			e.printStackTrace();
		}
		
		return null;
	}
	
	protected List<Path> findPaths(ExecutionEvent event) {
		return (List<Path>)event.getTrigger();
	}

	protected void writeContent(WritableSheet excelSheet) throws RowsExceededException, WriteException {
		
		excelSheet.addCell(new Label(0, 0, "Path no.", format));
		excelSheet.addCell(new Label(1, 0, "No. of Edges", format));
		excelSheet.addCell(new Label(2, 0, "Delay", format));
		excelSheet.addCell(new Label(3, 0, "Effect", format));
		excelSheet.addCell(new Label(4, 0, "Path", format));
		
		for(int i = 0; i < paths.size(); i++) {
			Path path = paths.get(i);
			excelSheet.addCell(new Number(0, i + 1, path.getIndex(), format));
			excelSheet.addCell(new Number(1, i + 1, path.getNodes().size(), format));
			excelSheet.addCell(new Number(2, i + 1, path.getDelay(), format));
			excelSheet.addCell(new Number(3, i + 1, path.getEffect(), format));
			for(int j = 0; j < path.getNodes().size(); j++) {
				excelSheet.addCell(new Number(j + 4, i + 1, path.getNodes().get(j), format));
			}
		}
	}

	@Override
	protected String getSheetName() {
		return "Paths";
	}

}
