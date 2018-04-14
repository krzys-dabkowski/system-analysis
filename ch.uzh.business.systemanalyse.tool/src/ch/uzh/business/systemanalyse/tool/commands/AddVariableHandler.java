/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import ch.uzh.business.systemanalyse.tool.model.ModelProvider;

/**
 * A command handler for adding a variable to the model.
 * 
 * @author Krzysztof Dabkowski
 *
 */
public class AddVariableHandler extends AbstractHandler {

	public final static String ID = "ch.uzh.business.systemanalyse.tool.commands.AddVariable";
	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ModelProvider model = ModelProvider.INSTANCE;
		
		model.addVariable();
		
		ModelProvider.INSTANCE.modelChanged(true);
		return null;
	}

}
