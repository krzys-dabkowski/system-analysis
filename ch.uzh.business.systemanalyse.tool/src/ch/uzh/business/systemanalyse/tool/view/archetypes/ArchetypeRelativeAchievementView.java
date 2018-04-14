package ch.uzh.business.systemanalyse.tool.view.archetypes;

public class ArchetypeRelativeAchievementView extends AbstractArchetypeView {

	public static final String ID = "ch.uzh.business.systemanalyse.tool.views.archetypes.relativeachievement";

	public ArchetypeRelativeAchievementView() {
		super();
		archetypeName = "Relative_Achievement";
		keyVariableName = "Relative outcome for";
		icReinforcing = true;
		ucReinforcing = true;
		ucDelay = true;
		description = "This generic loop constellation consists of a reinforcing intended consequence loop and a reinforcing unintended consequence loop.\n" +
				"Special case thereof is the 'Success To The Successful' archetype.";
	}


}