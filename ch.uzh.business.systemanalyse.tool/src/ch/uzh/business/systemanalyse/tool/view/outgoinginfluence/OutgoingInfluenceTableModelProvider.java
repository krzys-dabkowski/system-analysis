/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.outgoinginfluence;

import java.util.ArrayList;

import ch.uzh.business.systemanalyse.tool.model.Variable;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableModelProvider;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableRowData;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class OutgoingInfluenceTableModelProvider extends
		AbstractTableModelProvider {

	public OutgoingInfluenceTableModelProvider(Variable variable) {
		rows = new ArrayList<AbstractTableRowData>();
		rows.add(new OutgoingInfluenceTableRowData(0, variable));
		rows.add(new OutgoingInfluenceTableRowData(1, variable));
	}
	public OutgoingInfluenceTableModelProvider() {
		rows = new ArrayList<AbstractTableRowData>();
	}
	
	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.table.AbstractTableModelProvider#createRow(ch.uzh.business.systemanalyse.tool.model.Variable)
	 */
	@Override
	protected AbstractTableRowData createRow(Variable variable) {
		return null;
	}

	/* (non-Javadoc)
	 * @see ch.uzh.business.systemanalyse.tool.table.AbstractTableModelProvider#createRow()
	 */
	@Override
	protected AbstractTableRowData createRow() {
		return null;
	}

}
