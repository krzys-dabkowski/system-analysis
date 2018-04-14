package ch.uzh.business.systemanalyse.tool.navigation;
public interface IDeltaListener {
	public void add(DeltaEvent event);
	public void remove(DeltaEvent event);
}
