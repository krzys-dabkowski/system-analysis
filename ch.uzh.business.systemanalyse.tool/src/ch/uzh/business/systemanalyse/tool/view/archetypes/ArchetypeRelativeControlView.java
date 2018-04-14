package ch.uzh.business.systemanalyse.tool.view.archetypes;

public class ArchetypeRelativeControlView extends AbstractArchetypeView {

	public static final String ID = "ch.uzh.business.systemanalyse.tool.views.archetypes.relativecontrol";

	
	public ArchetypeRelativeControlView() {
		super();
		archetypeName = "Relative_Control";
		keyVariableName = "Relative outcome for";
		icReinforcing = false;
		ucReinforcing = false;
		ucDelay = true;
		description = "This generic loop constellation consists of a balancing intended consequence loop and a balancing unintended consequence loop.\n" +
				"Special cases thereof are 'Escalation' and 'Drifting Goals' archetypes.";
	}
}