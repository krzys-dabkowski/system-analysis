/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import ch.uzh.business.systemanalyse.tool.model.ModelProvider;
import ch.uzh.business.systemanalyse.tool.model.Variable;

/**
 * A command not meant for production, rather for testing.
 * 
 * @author Krzysztof Dabkowski
 *
 */
public class PrintModelHandler extends AbstractHandler {

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		List<Variable> variableList = ModelProvider.INSTANCE.getVariables();
		for(Variable v : variableList) {
			System.out.println(v);
		}
		return null;
	}

}
