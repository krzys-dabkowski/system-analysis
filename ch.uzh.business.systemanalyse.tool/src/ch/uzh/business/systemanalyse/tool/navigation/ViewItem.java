package ch.uzh.business.systemanalyse.tool.navigation;

public class ViewItem extends Model {
	
	public ViewItem(String viewID, String viewTitle) {
		super(viewID, viewTitle);
	}
	
	/*
	 * @see Model#accept(ModelVisitorI, Object)
	 */
	public void accept(IModelVisitor visitor, Object passAlongArgument) {
		visitor.visitViewItem(this, passAlongArgument);
	}

}
