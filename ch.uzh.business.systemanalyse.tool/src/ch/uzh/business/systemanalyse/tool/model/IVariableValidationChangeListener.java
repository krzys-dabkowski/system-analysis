/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.model;

/**
 * Classes (like Views) should implement this interface
 * and register themselves in the {@link ModelProvider}
 * using ModelProvider.registerValidationListener
 * in order to be informed about changes in the model's
 * validation.
 * 
 * @author krzysztof.dabkowski
 *
 */
public interface IVariableValidationChangeListener {

	public void variableValidationChanged(VariableValidationType type);
}
