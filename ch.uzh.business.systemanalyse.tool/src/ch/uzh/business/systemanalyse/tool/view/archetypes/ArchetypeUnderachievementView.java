package ch.uzh.business.systemanalyse.tool.view.archetypes;

public class ArchetypeUnderachievementView extends AbstractArchetypeView {

	public static final String ID = "ch.uzh.business.systemanalyse.tool.views.archetypes.underachievement";

	public ArchetypeUnderachievementView() {
		super();
		archetypeName = "Underachievement";
		keyVariableName = "Outcome";
		icReinforcing = true;
		ucReinforcing = false;
		ucDelay = true;
		description = "The following loop constellation consists of a reinforcing intended consequence loop and a balancing unintended consequence loop.\n" +
				"Special cases thereof are 'Limits to Growth', 'Tragedy of the Commons' or 'Growth and Underinvestment' archetypes.";
	}
}