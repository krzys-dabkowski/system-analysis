/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.table;

import ch.uzh.business.systemanalyse.tool.IProjectSettingsConstants;
import ch.uzh.business.systemanalyse.tool.model.ModelProvider;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableLabelProvider;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableModelProvider;
import ch.uzh.business.systemanalyse.tool.view.incominginfluence.InfluenceTableLabelProvider;
import ch.uzh.business.systemanalyse.tool.view.incominginfluence.IncomingInfluenceTableModelProvider;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class IncomingInfluenceTableView extends AbstractInfluenceTableView {

	public final static String ID = "ch.uzh.business.systemanalyse.tool.views.incominginfluence";
	
	
	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.table.AbstractTableView#createModelProvider()
	 */
	@Override
	protected AbstractTableModelProvider createModelProvider() {
		if(varCombo.getSelectionIndex() == -1) {
			return new IncomingInfluenceTableModelProvider();
		} else {
			//System.out.println("combo selection: " + varCombo.getSelectionIndex());
			return new IncomingInfluenceTableModelProvider(ModelProvider.INSTANCE.getVariable(analyzedVariableIndex));
		}
	}
	
	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.table.AbstractTableView#createLabelProvider()
	 */
	@Override
	protected AbstractTableLabelProvider createLabelProvider() {
		return new InfluenceTableLabelProvider(viewer);
	}

	@Override
	protected void checkValidation() {
	}
	
	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.view.table.AbstractTableView#setColumnsText()
	 */
	@Override
	protected void setColumnsText() {
		viewer.getTable().getColumn(0).setText("");
		viewer.getTable().getColumn(1).setText("From node no.");
		for(int i = 0; i < ModelProvider.INSTANCE.getVariables().size(); i++){
			viewer.getTable().getColumn(i + 
					IProjectSettingsConstants.NUMBER_OF_UNEDITABLE_COLUMNS).setText(
							String.valueOf(
							ModelProvider.INSTANCE.getVariables().get(i).getIndex()));
		}
	}

	@Override
	String getVariableLabelText() {
		return "End variable: ";
	}
}
