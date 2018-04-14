/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.view.incominginfluence;

import java.util.ArrayList;

import ch.uzh.business.systemanalyse.tool.model.Variable;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableModelProvider;
import ch.uzh.business.systemanalyse.tool.table.AbstractTableRowData;

/**
 * @author Krzysztof Dabkowski
 *
 */
public class IncomingInfluenceTableModelProvider extends AbstractTableModelProvider {

	
	public IncomingInfluenceTableModelProvider(Variable variable) {
		rows = new ArrayList<AbstractTableRowData>();
		rows.add(new IncomingInfluenceTableRowData(0, variable));
		rows.add(new IncomingInfluenceTableRowData(1, variable));
	}

	public IncomingInfluenceTableModelProvider() {
		rows = new ArrayList<AbstractTableRowData>();
	}
	
	@Override
	protected AbstractTableRowData createRow(Variable variable) {
		return null;
	}

	@Override
	protected AbstractTableRowData createRow() {
		return null;
	}
}
