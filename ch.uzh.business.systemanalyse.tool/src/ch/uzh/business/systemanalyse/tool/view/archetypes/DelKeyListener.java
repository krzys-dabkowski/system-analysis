package ch.uzh.business.systemanalyse.tool.view.archetypes;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;

public class DelKeyListener implements KeyListener {



	@Override
	public void keyPressed(KeyEvent e) {
		//
	}

	@Override
	public void keyReleased(KeyEvent e) {
		/*switch(e.keyCode){
		case SWT.DEL:	for (Object gI : ((Graph)e.getSource()).getSelection().toArray()) {
				((GraphItem) gI).dispose();
			};
			break;
		case 101:		for (Object gI : ((Graph)e.getSource()).getNodes().toArray()) {
			
			if (!((Graph)e.getSource()).getSelection().contains(gI)){
				if (gI instanceof GraphNode){
						((GraphNode) gI).dispose();
					}
				}
			};
			break;
			
		}*/
	}

}
