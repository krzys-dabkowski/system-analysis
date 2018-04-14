package obsolete;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;

import ch.uzh.business.systemanalyse.tool.Activator;

/**
 * 
 */

/**
 * @author Krzysztof Dabkowski
 *
 */
public class CreatePathFinderCommand extends AbstractHandler {

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		//ShortestPathFinder finder = new ShortestPathFinder();
		try {
			
			FontData font = PreferenceConverter.getFontData(Activator.getDefault().getPreferenceStore(), "TICK_FONT");
			if(font.getStyle() == SWT.BOLD) {
				System.out.println("bold");
				System.out.println(font.getStyle());
			}
			//if(font.getStyle() == SWT.)
			System.out.println(font.getStyle());
			System.out.println("locale: " + font.getLocale());
			System.out.println(font.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
