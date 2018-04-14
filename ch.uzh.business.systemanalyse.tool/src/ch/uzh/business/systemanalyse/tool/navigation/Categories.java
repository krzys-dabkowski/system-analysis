package ch.uzh.business.systemanalyse.tool.navigation;

import java.util.ArrayList;
import java.util.List;

public class Categories extends Model {
	protected List<Categories> categories;
	protected List<ViewItem> items;
	
	private static IModelVisitor adder = new Adder();
	private static IModelVisitor remover = new Remover();
	
	public Categories() {
		categories = new ArrayList<Categories>();
		items = new ArrayList<ViewItem>();
	}
	
	private static class Adder implements IModelVisitor {

		/*
		 * @see ModelVisitorI#visitBoardgame(BoardGame)
		 */

		/*
		 * @see ModelVisitorI#visitBook(MovingBox)
		 */

		/*
		 * @see ModelVisitorI#visitMovingBox(MovingBox)
		 */

		/*
		 * @see ModelVisitorI#visitBook(MovingBox, Object)
		 */
		public void visitViewItem(ViewItem viewItem, Object argument) {
			((Categories) argument).addViewItem(viewItem);
		}

		/*
		 * @see ModelVisitorI#visitMovingBox(MovingBox, Object)
		 */
		public void visitCategories(Categories box, Object argument) {
			((Categories) argument).addBox(box);
		}

	}

	private static class Remover implements IModelVisitor {

		/*
		 * @see ModelVisitorI#visitBook(MovingBox, Object)
		 */
		public void visitViewItem(ViewItem viewItem, Object argument) {
			((Categories) argument).removeViewItem(viewItem);
		}

		/*
		 * @see ModelVisitorI#visitMovingBox(MovingBox, Object)
		 */
		public void visitCategories(Categories box, Object argument) {
			((Categories) argument).removeBox(box);
			box.addListener(NullDeltaListener.getSoleInstance());
		}

	}
	
	public Categories(String name) {
		this();
		this.name = name;
	}
	
	public List<Categories> getBoxes() {
		return categories;
	}
	
	protected void addBox(Categories box) {
		categories.add(box);
		box.parent = this;
		fireAdd(box);
	}
	
	protected void addViewItem(ViewItem viewItem) {
		items.add(viewItem);
		viewItem.parent = this;
		fireAdd(viewItem);
	}
	
	public List<ViewItem> getBooks() {
		return items;
	}
	
	public void remove(Model toRemove) {
		toRemove.accept(remover, this);
	}
	
	protected void removeViewItem(ViewItem viewItem) {
		items.remove(viewItem);
		viewItem.addListener(NullDeltaListener.getSoleInstance());
		fireRemove(viewItem);
	}
	
	protected void removeBox(Categories box) {
		categories.remove(box);
		box.addListener(NullDeltaListener.getSoleInstance());
		fireRemove(box);	
	}

	public void add(Model toAdd) {
		toAdd.accept(adder, this);
	}
	
	/** Answer the total number of items the
	 * receiver contains. */
	public int size() {
		return getBooks().size() + getBoxes().size();
	}
	/*
	 * @see Model#accept(ModelVisitorI, Object)
	 */
	public void accept(IModelVisitor visitor, Object passAlongArgument) {
		visitor.visitCategories(this, passAlongArgument);
	}

}
