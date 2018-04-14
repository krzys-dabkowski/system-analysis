/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import ch.uzh.business.systemanalyse.tool.Activator;
import ch.uzh.business.systemanalyse.tool.IProjectSettingsConstants;
import ch.uzh.business.systemanalyse.tool.licence.tool.filegenerator.LicenceException;
import ch.uzh.business.systemanalyse.tool.licence.tool.parts.LicenceFileConsumer;
import ch.uzh.business.systemanalyse.tool.systempaths.PathsFinder;
import ch.uzh.business.systemanalyse.tool.systempaths.ShortestPathFinder;

/**
 * ModelProvider stores and manages the application model.
 * 
 * <p>
 * The model variables are stored in the <b>variables</b> collection.
 * Additionally, it stores information about listeners to the application model
 * and calls them, when necessary.
 * </p>
 * 
 * <p>
 * ModelProvider is a singleton. You reference the model if the following form:
 * <i>ModelProvider.INSTANCE</i>. Therefore, the creation takes place
 * automatically. First time the reference is made, the constructor is called
 * and the ready to use object is there.
 * </p>
 * 
 * <p>
 * After filling out the model (setting variables), the init method has to be
 * called.
 * </p>
 * 
 * @author Krzysztof Dabkowski
 * 
 */
public enum ModelProvider {
	INSTANCE;

	private boolean licenced = false;

	public boolean isLicenced() {
		// checkIfLicenced();
		// return licenced;

		return true;
	}

	private ArrayList<Variable> variables;
	private PathsFinder pathsFinder;
	private ArrayList<Path> fromToPaths;
	private ArrayList<Path> toPaths;
	private ArrayList<Path> fromPaths;
	private Set<IModelChangeListener> modelChangeListeners;
	private Set<IVariableValidationChangeListener> validationListeners;

	/**
	 * The default constructor, creates a ready to use object.
	 */
	private ModelProvider() {
		modelChangeListeners = new HashSet<IModelChangeListener>();
		validationListeners = new HashSet<IVariableValidationChangeListener>();
		variables = new ArrayList<Variable>();
		pathsFinder = null;
		fromToPaths = new ArrayList<Path>();
		toPaths = new ArrayList<Path>();
		fromPaths = new ArrayList<Path>();

		// generateRandomModel();
		// generateFloydWarshallModel();
		// generateArticleModel();
		// fillModel();

	}

	public void checkIfLicenced() {
		licenced = checkLicence();
	}

	private boolean checkLicence() {
		LicenceFileConsumer consumer = new LicenceFileConsumer();
		String filePath = Activator.getDefault().getPreferenceStore()
				.getString("LICENCE_FILE");
		if (filePath == null || filePath.equals("")) {

			return false;
		} else {
			try {
				boolean licenceValidationResult = consumer
						.consumeLicence(filePath);
				if (licenceValidationResult) {
					return true;
				} else {
					MessageDialog
							.openError(Display.getDefault().getActiveShell(),
									"Invalid licence",
									"The specified licence file contains an invalid licence");
					return false;
				}
			} catch (LicenceException e) {
				e.printStackTrace();
			}

		}
		return false;
	}

	/**
	 * Starts some calculations, that have do be done after the
	 */
	public void init() {

		generateShortestPathsDelay();
		generateShortestPathsImpact();
	}

	/**
	 * Clears the model - deletes the variables and calls modelChanged with the
	 * parameter true.
	 */
	public void clear() {
		ArrayList<Variable> varsTemp = new ArrayList<Variable>();
		varsTemp.addAll(variables);
		pathsFinder = null;
		for (Variable var : varsTemp) {
			deleteVariable(var.getIndex());
		}
		modelChanged(true);
	}

	public void addVariable() {
		Variable var = new Variable("new var");

		// fill the impact array for the new array
		for (Variable v : variables) {
			try {
				var.addImpactValue(v.getIndex(), 0);
				var.addDelayValue(v.getIndex(), 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		variables.add(var);
		// for each new var add an impact value and a delay value for the new
		// var
		for (Variable v1 : variables) {
			try {
				v1.addImpactValue(var.getIndex(), 0);
				v1.addDelayValue(var.getIndex(), 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean deleteVariable(int index) {
		boolean found = false;
		Variable varToDelete = null;
		for (Variable v : variables) {
			if (v.getIndex() == index) {
				found = true;
				varToDelete = v;
				break;
			}
		}

		if (found) {
			variables.remove(varToDelete);

			// remove all impact values with deleted variable
			VariableValue impactValueToDelete = null;
			for (Variable v : variables) {
				for (VariableValue impactValue : v.getImpactArray()) {
					if (impactValue.getVariableIndex() == index) {
						impactValueToDelete = impactValue;
					}
				}
				v.getImpactArray().remove(impactValueToDelete);
			}

			// remove all delay values with deleted variable
			VariableValue delayValueToDelete = null;
			for (Variable v : variables) {
				for (VariableValue delayValue : v.getDelayArray()) {
					if (delayValue.getVariableIndex() == index) {
						delayValueToDelete = delayValue;
					}
				}
				v.getDelayArray().remove(delayValueToDelete);
			}

			varToDelete.delete();
			for (int i = 0; i < variables.size(); i++) {
				Variable varToAdjust = variables.get(i);
				varToAdjust.setIndex(i + 1);
			}

			// TODO set indexes of other variables
		}
		return found;
	}

	/**
	 * Updates listeners, when the model has changed.
	 * 
	 * @param clear
	 *            indicates, if
	 */
	public void modelChanged(boolean clear) {

		validateModel();

		// Status status = new Status(IStatus.ERROR,
		// "ch.uzh.business.systemanalyse.tool", "Model Changed");
		System.out.println();
		System.out
				.println("================== Model Changed =================");
		for (IModelChangeListener listener : modelChangeListeners) {
			System.out.println(listener.getClass());
			listener.fireModelChange(clear);
		}
		System.out
				.println("================== Model Changed =================");
		System.out.println();
	}

	/**
	 * Validates, if all the variables, that have an impact value to another
	 * one, also have a delay value. The results of the validation are set to
	 * each variable value.
	 * 
	 */
	private void validateModel() {
		boolean validated = false;
		boolean delayValidated = true;
		boolean impactValidated = true;
		// VariableValidationType type;
		// Variable invalidVariable;
		// VariableValue invalidValue;
		// go over all variables
		for (Variable fromVariable : variables) {

			// for each variable go over delay values
			for (VariableValue delayValue : fromVariable.getDelayArray()) {
				Integer toVariableIndex = delayValue.getVariableIndex();
				// if delay value is not 0...
				if (delayValue.getVariableValue() != 0) {
					// .. but impact value is 0
					if (fromVariable.getImpactValueToVariable(toVariableIndex) == 0) {
						// impact validation is false
						validated = false;
						impactValidated = false;
					} else {
						validated = true;
					}

					for (VariableValue impactValue : fromVariable
							.getImpactArray()) {
						if (impactValue.getVariableIndex() == delayValue
								.getVariableIndex()) {
							impactValue.setValidated(validated);
							// System.out.println("setting " + validated +
							// " for impact for fromVariable index " +
							// fromVariable.getIndex() +
							// " and toVariable index " + toVariableIndex);
						}
					}
					delayValue.setValidated(validated);
				}
			}

			// now the same the other way round - for all impact values
			// is there a delay value..
			for (VariableValue impactValue : fromVariable.getImpactArray()) {
				Integer toVariableIndex = impactValue.getVariableIndex();
				if (impactValue.getVariableValue() != 0) {
					if (fromVariable.getDelayValueToVariable(toVariableIndex) == 0) {
						validated = false;
						delayValidated = false;
					} else {
						validated = true;
					}

					for (VariableValue delayValue : fromVariable
							.getDelayArray()) {
						if (delayValue.getVariableIndex() == impactValue
								.getVariableIndex()) {
							delayValue.setValidated(validated);
							// System.out.println("setting " + validated +
							// " for delay for fromVariable index " +
							// fromVariable.getIndex() +
							// " and toVariable index " + toVariableIndex);
						}
					}
					impactValue.setValidated(validated);
				}
			}
		}

		if (!delayValidated) {
			if (!impactValidated) {
				// both are not validated
				for (IVariableValidationChangeListener listener : validationListeners) {
					listener.variableValidationChanged(VariableValidationType.BOTH);
				}
			} else {
				// impact is validated, delay not
				for (IVariableValidationChangeListener listener : validationListeners) {
					listener.variableValidationChanged(VariableValidationType.DELAY);
				}
			}
		} else {
			// delay validated
			if (!impactValidated) {
				for (IVariableValidationChangeListener listener : validationListeners) {
					listener.variableValidationChanged(VariableValidationType.IMPACT);
				}
			}
		}
	}

	public void registerListener(IModelChangeListener listener) {
		modelChangeListeners.add(listener);
		System.out.println();
		System.out
				.println("================== Register Listener ================= ");
		System.out.println(listener.getClass());
		System.out.println("New Listeners:");
		for (IModelChangeListener l : modelChangeListeners) {
			System.out.println(l.getClass());
		}
		System.out
				.println("================== Register Listener =================");
		System.out.println();
	}

	public void unregisterListener(IModelChangeListener listener) {
		modelChangeListeners.remove(listener);
		System.out.println();
		System.out
				.println("================== Unregister Listener ================= ");
		System.out.println(listener.getClass());
		System.out.println("New Listeners:");
		for (IModelChangeListener l : modelChangeListeners) {
			System.out.println(l.getClass());
		}
		System.out
				.println("================== Unregister Listener =================");
		System.out.println();
	}

	public void registerValidationListener(
			IVariableValidationChangeListener listener) {
		validationListeners.add(listener);
	}

	public void unregisterValidationListener(
			IVariableValidationChangeListener listener) {
		validationListeners.remove(listener);
	}

	/**
	 * A method, that can be called in the constructor in order to create an
	 * example model.
	 * 
	 * Not used since the load model from file functionality was added.
	 */
	private void generateRandomModel() {
		Variable var1 = new Variable("Population");
		variables.add(var1);
		Variable var2 = new Variable("Birth");
		variables.add(var2);
		variables.add(new Variable("Death"));
		variables.add(new Variable("Fertility rate"));
		variables.add(new Variable("Death rate"));
		variables.add(new Variable("Carrying capacity"));
		variables.add(new Variable("Pop rel. to Cc"));
		variables.add(new Variable("Regeneration of Cc"));
		variables.add(new Variable("Degradation of Cc"));

		for (int i = 0; i < variables.size(); i++) {
			for (int j = 1; j < variables.size() + 1; j++) {
				try {

					variables
							.get(i)
							.addImpactValue(
									j,
									IProjectSettingsConstants.DEFAULT_IMPACT_VALUES[(int) Math
											.floor(Math.random()
													* IProjectSettingsConstants.DEFAULT_IMPACT_VALUES.length)]);
					variables
							.get(i)
							.addDelayValue(
									j,
									IProjectSettingsConstants.DEFAULT_DELAY_VALUES[(int) Math
											.floor(Math.random()
													* IProjectSettingsConstants.DEFAULT_DELAY_VALUES.length)]);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * A method, that can be called in the constructor in order to create an
	 * example model.
	 * 
	 * Not used since the load model from file functionality was added.
	 */
	private void generateFloydWarshallModel() {
		Variable var1 = new Variable("Population");
		variables.add(var1);
		Variable var2 = new Variable("Birth");
		variables.add(var2);
		Variable var3 = new Variable("Death");
		variables.add(var3);
		Variable var4 = new Variable("Fertility rate");
		variables.add(var4);
		Variable var5 = new Variable("Death rate");
		variables.add(var5);
		for (int i = 0; i < variables.size(); i++) {
			for (int j = 1; j < variables.size() + 1; j++) {
				try {

					// variables.get(i).addImpactValue(j,
					// IProjectSettingsConstants.IMPACT_VALUES[(int)
					// Math.floor(Math.random() *
					// IProjectSettingsConstants.IMPACT_VALUES.length)]);
					variables.get(i).addImpactValue(j, 0);
					variables.get(i).addDelayValue(j, 0);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// generateShortestPaths();
	}

	private void generateShortestPathsDelay() {
		ShortestPathFinder finder = new ShortestPathFinder();
		Path[][] paths = null;
		try {
			paths = finder.findPathsFW();
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < paths.length; i++) {
			for (int j = 0; j < paths.length; j++) {
				variables.get(i).addShortestPathValue(j + 1,
						paths[i][j].getDelay());
			}
		}
	}

	private void generateShortestPathsImpact() {
		ShortestPathFinder finder = new ShortestPathFinder();
		Path[][] paths = null;
		try {
			// paths = finder.findPathsAdaptedFW();
			paths = finder.findPathsFW();
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < paths.length; i++) {
			for (int j = 0; j < paths.length; j++) {
				variables.get(i).addStrongestEffectValue(j + 1,
						paths[i][j].getEffect());
			}
		}
	}

	public void generateArticleModel() {
		Variable var1 = new Variable("Population");
		variables.add(var1);
		Variable var2 = new Variable("Birth");
		variables.add(var2);
		Variable var3 = new Variable("Death");
		variables.add(var3);
		Variable var4 = new Variable("Fertility rate");
		variables.add(var4);
		Variable var5 = new Variable("Death rate");
		variables.add(var5);
		Variable var6 = new Variable("Carrying capacity");
		variables.add(var6);
		Variable var7 = new Variable("Pop rel. to Cc");
		variables.add(var7);
		Variable var8 = new Variable("Regeneration of Cc");
		variables.add(var8);
		Variable var9 = new Variable("Degradation of Cc");
		variables.add(var9);

		for (int i = 0; i < variables.size(); i++) {
			for (int j = 1; j < variables.size() + 1; j++) {
				try {

					variables.get(i).addImpactValue(j, 0);
					variables.get(i).addDelayValue(j, 0);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void fillModel() {
		/*
		 * variables.get(0).addDelayValue(3, 2);
		 * variables.get(1).addDelayValue(1, 1);
		 * variables.get(1).addDelayValue(4, 3);
		 * variables.get(2).addDelayValue(4, 1);
		 * variables.get(2).addDelayValue(5, 3);
		 * variables.get(3).addDelayValue(1, -1);
		 * variables.get(3).addDelayValue(5, 1);
		 * variables.get(4).addDelayValue(1, -3);
		 * 
		 * variables.get(0).addImpactValue(2, 2f);
		 * variables.get(0).addImpactValue(3, 2f);
		 * variables.get(1).addImpactValue(1, 1);
		 * variables.get(1).addImpactValue(5, 2);
		 * variables.get(2).addImpactValue(4, 5);
		 * variables.get(2).addImpactValue(5, 1);
		 * variables.get(3).addImpactValue(1, 4);
		 * variables.get(4).addImpactValue(1, 1);
		 * variables.get(4).addImpactValue(4, 3);
		 */

		/*
		 * getVariable(1).addDelayValue(2, 2); getVariable(1).addDelayValue(3,
		 * 2); getVariable(2).addDelayValue(1, 1);
		 * getVariable(2).addDelayValue(5, 2); getVariable(3).addDelayValue(4,
		 * 5); getVariable(3).addDelayValue(5, 1);
		 * getVariable(4).addDelayValue(1, 4); getVariable(5).addDelayValue(1,
		 * 1); getVariable(5).addDelayValue(4, 3);
		 * 
		 * getVariable(1).addImpactValue(2, 0.5f);
		 * getVariable(1).addImpactValue(3, 1); getVariable(2).addImpactValue(1,
		 * 1); getVariable(2).addImpactValue(5, 1);
		 * getVariable(3).addImpactValue(4, 0.5f);
		 * getVariable(3).addImpactValue(5, 2); getVariable(4).addImpactValue(1,
		 * 2); getVariable(5).addImpactValue(1, 0.5f);
		 * getVariable(5).addImpactValue(4, 1);
		 */

		getVariable(1).addImpactValue(2, 2f / 3f);
		getVariable(1).addImpactValue(3, 2f / 3f);
		getVariable(1).addImpactValue(7, 1f);
		getVariable(1).addImpactValue(9, 2f / 3f);
		getVariable(2).addImpactValue(1, 1f);
		getVariable(3).addImpactValue(1, -1f);
		getVariable(4).addImpactValue(2, 2f / 3f);
		getVariable(5).addImpactValue(3, 2f / 3f);
		getVariable(6).addImpactValue(7, -1f);
		getVariable(6).addImpactValue(9, 2f / 3f);
		getVariable(7).addImpactValue(4, -1.5f);
		getVariable(7).addImpactValue(5, 2f / 3f);
		getVariable(8).addImpactValue(6, 2f / 3f);
		getVariable(9).addImpactValue(6, -1.5f);

		getVariable(1).addDelayValue(2, 5f);
		getVariable(1).addDelayValue(3, 10f);
		getVariable(1).addDelayValue(7, 2f);
		getVariable(1).addDelayValue(9, 5f);
		getVariable(2).addDelayValue(1, 1f);
		getVariable(3).addDelayValue(1, 1f);
		getVariable(4).addDelayValue(2, 2f);
		getVariable(5).addDelayValue(3, 2f);
		getVariable(6).addDelayValue(7, 2f);
		getVariable(6).addDelayValue(9, 10f);
		getVariable(7).addDelayValue(4, 5f);
		getVariable(7).addDelayValue(5, 10f);
		getVariable(8).addDelayValue(6, 10f);
		getVariable(9).addDelayValue(6, 5f);
	}

	public double[] getActiveSums() {
		double[] activeSums = new double[getVariables().size()];
		for (int i = 0; i < getVariables().size(); i++) {
			activeSums[i] = getVariables().get(i).calculateActiveSum();
		}
		return activeSums;
	}

	public double[] getPassiveSums() {
		double[] passiveSums = new double[getVariables().size()];
		for (int i = 0; i < getVariables().size(); i++) {
			passiveSums[i] = getVariables().get(i).calculatePassiveSum();
		}
		return passiveSums;
	}

	public double[] getProducedDelays() {
		double[] producedDelays = new double[getVariables().size()];
		for (int i = 0; i < getVariables().size(); i++) {
			producedDelays[i] = getVariables().get(i).calculateProducedDelay();
		}
		return producedDelays;
	}

	public double[] getReceivedDelays() {
		double[] receivedDelays = new double[getVariables().size()];
		for (int i = 0; i < getVariables().size(); i++) {
			receivedDelays[i] = getVariables().get(i).calculateReceivedDelay();
		}
		return receivedDelays;
	}

	public ArrayList<VariableValue> getShortestPathsTo(Variable variable) {
		ArrayList<VariableValue> resultArray = new ArrayList<VariableValue>();
		if (variable != null) {
			for (Variable v : variables) {
				for (VariableValue value : v.getShortestPathsArray()) {
					if (value.getVariableIndex() == variable.getIndex()) {
						resultArray.add(value);
					}
				}
			}
		}
		return resultArray;
	}

	public ArrayList<VariableValue> getStrongestEffectTo(Variable variable) {
		ArrayList<VariableValue> resultArray = new ArrayList<VariableValue>();
		if (variable != null) {
			for (Variable v : variables) {
				for (VariableValue value : v.getStrongestEffectsArray()) {
					if (value.getVariableIndex() == variable.getIndex()) {
						resultArray.add(value);
					}
				}
			}
		}
		return resultArray;
	}

	public Variable getVariable(int index) {
		for (Variable v : variables) {
			if (v.getIndex() == index) {
				return v;
			}
		}
		return null;
	}

	public List<Variable> getVariables() {
		return variables;
	}

	public List<Path> getToPaths() {
		return toPaths;
	}

	public List<Path> getFromPaths() {
		return fromPaths;
	}

	public void setToPaths(Integer end) {
		if (pathsFinder == null) {
			pathsFinder = new PathsFinder();
		}
		toPaths = pathsFinder.findPaths(end);
		modelChanged(false);
	}

	public void setFromPaths(Integer start) {
		if (pathsFinder == null) {
			pathsFinder = new PathsFinder();
		}
		fromPaths = pathsFinder.findPathsTo(start);
		modelChanged(false);
	}

	public List<Path> getFromToPaths() {
		return fromToPaths;
	}

	public void setFromToPaths(Integer start, Integer end) {
		// if(pathsFinder == null) {
		pathsFinder = new PathsFinder();
		// }
		fromToPaths = pathsFinder.findAllPaths(start, end);
		modelChanged(false);
	}
}
