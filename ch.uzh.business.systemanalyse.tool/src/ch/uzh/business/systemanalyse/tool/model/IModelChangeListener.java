/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.model;

/**
 * Classes (like Views) should implement this interface
 * and register themselves in the {@link ModelProvider}
 * using ModelProvider.registerListener(IModelChangeListener)
 * in order to be informed about changes in the model.
 * 
 * ModelProvider decides about the gravity of the change.
 * In general, if a new {@link Variable} is added, the 
 * parameter clear should be set to true.
 * 
 * @author Krzysztof Dabkowski
 *
 */
public interface IModelChangeListener {

	public void fireModelChange(boolean clear);
}
