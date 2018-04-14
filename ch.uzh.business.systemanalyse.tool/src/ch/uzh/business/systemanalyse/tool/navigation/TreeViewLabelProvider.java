package ch.uzh.business.systemanalyse.tool.navigation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import ch.uzh.business.systemanalyse.tool.Activator;
import ch.uzh.business.systemanalyse.tool.navigation.Categories;
import ch.uzh.business.systemanalyse.tool.navigation.ViewItem;

public class TreeViewLabelProvider extends LabelProvider {	
	private Map<ImageDescriptor, Image> imageCache = new HashMap<ImageDescriptor, Image>(11);
	
	/*
	 * @see ILabelProvider#getImage(Object)
	 */
	public Image getImage(Object element) {
		ImageDescriptor descriptor = null;
		/*if (element instanceof Categories) {
			descriptor = TreeViewerPlugin.getImageDescriptor("movingBox.gif");
		} else if (element instanceof ViewItem) {
			descriptor = TreeViewerPlugin.getImageDescriptor("book.gif");
		} else {
			throw unknownElement(element);
		}*/
		
		if (element instanceof Categories) {
			descriptor = Activator.getImageDescriptor("icons/folder_open16x16.png");
		} else if (element instanceof ViewItem) {
			if (((ViewItem) element).name.contains("Chart")){
				descriptor = Activator.getImageDescriptor("icons/line_graph_16x16.png");
			} else if(((ViewItem) element).name.contains("Matrix")){
				descriptor = Activator.getImageDescriptor("icons/table_16x16.png");
			} else{
				descriptor = Activator.getImageDescriptor("icons/table_16x16.png");
			}
		} else {
			throw unknownElement(element);
		}
		
		//obtain the cached image corresponding to the descriptor
		Image image = (Image)imageCache.get(descriptor);
		if (image == null) {
			image = descriptor.createImage();
			imageCache.put(descriptor, image);
		}
		return image;
	}

	/*
	 * @see ILabelProvider#getText(Object)
	 */
	public String getText(Object element) {
		if (element instanceof Categories) {
			if(((Categories)element).getName() == null) {
				return "Category";
			} else {
				return ((Categories)element).getName();
			}
		} else if (element instanceof ViewItem) {
			return ((ViewItem)element).getName();
		} else {
			throw unknownElement(element);
		}
	}
	
	public String getID(Object element){
		if (element instanceof Categories) {
			if(((Categories)element).getName() == null) {
				return null;
			} else {
				return ((Categories)element).getID();
			}
		} else if (element instanceof ViewItem) {
			return ((ViewItem)element).getID();
		} else {
			throw unknownElement(element);
		}
	}

	public void dispose() {
		for (Iterator<Image> i = imageCache.values().iterator(); i.hasNext();) {
			((Image) i.next()).dispose();
		}
		imageCache.clear();
	}

	protected RuntimeException unknownElement(Object element) {
		return new RuntimeException("Unknown type of element in tree of type " + element.getClass().getName());
	}

}
