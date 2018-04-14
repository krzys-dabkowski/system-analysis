package obsolete;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
/**
 * 
 */

import ch.uzh.business.systemanalyse.tool.systempaths.Tarjan;

/**
 * @author krzysztof.dabkowski
 *
 */
public class ExecuteTarjanHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Tarjan tarjan = new Tarjan();
		tarjan.findCycles();
		return null;
	}

}
