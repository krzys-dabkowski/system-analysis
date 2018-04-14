package ch.uzh.business.systemanalyse.tool.navigation;

public interface IModelVisitor {
	public void visitCategories(Categories box, Object passAlongArgument);
	public void visitViewItem(ViewItem viewItem, Object passAlongArgument);
}
