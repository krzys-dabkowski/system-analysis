package ch.uzh.business.systemanalyse.tool.view.policyonoff;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import ch.uzh.business.systemanalyse.tool.model.ModelProvider;
import ch.uzh.business.systemanalyse.tool.model.Path;
import ch.uzh.business.systemanalyse.tool.model.Variable;
import ch.uzh.business.systemanalyse.tool.systempaths.PathsFinder;

public class PolicyComposite {

	private static int counter;

	private PathsFinder pathsFinder;
	private int index;
	private ArrayList<Integer> variableIndices = new ArrayList<Integer>();
	private Composite composite;
	private Label headline;
	private Text sumText;
	private Text positiveText;
	private Text negativeText;
	private Text shortestText;
	private Text longestText;

	private String title;

	private Button startButton;

	private ArrayList<Path> feedbackLoops = new ArrayList<Path>();

	PolicyComposite() {
		this.setIndex(counter);
		counter++;

		// initFinder();
	}

	void initFinder() {
		variableIndices = new ArrayList<Integer>();
		pathsFinder = new PathsFinder(variableIndices);
		feedbackLoops = new ArrayList<Path>();
		calculateFeedbackLoops();

	}

	public void setTexts() {
		// initFinder();

		if (getHeadline() != null) {
			if (getIndex() == 0) {
				setTitle("ON Analysis");
			} else {
				StringBuilder excludedString = new StringBuilder();
				for (Integer i : getVariableIndices()) {
					excludedString.append(i.toString()).append(", ");
				}

				// getHeadline().setText(
				// "OFF Analysis " + (getIndex())
				// +
				getHeadline().setText(
						"OFF Analysis (excluded variables: "
								+ excludedString.toString().substring(0,
										excludedString.length() - 2) + ")");
				getHeadline().redraw();
				getHeadline().update();
				getHeadline().getShell().layout(true, true);
			}
		}

		if (getSumText() != null) {
			getSumText().setText(String.valueOf(feedbackLoops.size()));
		}
		if (getPositiveText() != null) {
			getPositiveText().setText(String.valueOf(calculatePositiveLoops()));
		}
		if (getNegativeText() != null) {
			getNegativeText().setText(String.valueOf(calculateNegativeLoops()));
		}
		if (getShortestText() != null) {
			getShortestText().setText(findShortestLoop());
		}
		if (getLongestText() != null) {
			getLongestText().setText(findLongestLoop());
		}
	}

	private void calculateFeedbackLoops() {
		// pathsFinder.findLoops();
		feedbackLoops.clear();
		for (Variable var : ModelProvider.INSTANCE.getVariables()) {
			ArrayList<Path> paths;
			paths = pathsFinder.findPaths(var.getIndex());
			for (Path p : paths) {
				if (p.getNodes().get(0) == var.getIndex()) {
					feedbackLoops.add(p);
				}
			}
		}
		int count = 1;
		for (Path p : feedbackLoops) {
			p.setIndex(count);
			count++;
		}
		// System.out.println("loops: " + count);
		if (feedbackLoops.size() > 20000) {
			MessageBox messageDialog = new MessageBox(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), SWT.ICON_QUESTION
					| SWT.YES | SWT.NO);
			messageDialog.setText("Feedback Loops");
			messageDialog
					.setMessage("Found a large number of feedback loops. Do you want to continue?"
							+ "\n(The following calculations may take several minutes.)");
			int returnCode = messageDialog.open();

			if (returnCode == SWT.NO) {
				feedbackLoops.clear();
				return;
			}
		}
		/*
		 * HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		 * for(Path p : feedbackLoops) { Integer size = p.getNodes().size();
		 * if(map.containsKey(size)) { int oldsize = map.get(size); oldsize++;
		 * map.put(size, oldsize); } else { map.put(size, 1); } } for(Integer
		 * ind : map.keySet()) { System.out.println(ind + " " + map.get(ind)); }
		 */

		ArrayList<Path> toRemove = new ArrayList<Path>();
		// for(int i1 = 0; i1 < feedbackLoops.size(); i1++) {//Path loop :
		// feedbackLoops) {
		for (int i1 = feedbackLoops.size() - 1; i1 >= 0; i1--) {// Path loop :
																// feedbackLoops)
																// {
			Path loop = feedbackLoops.get(i1);
			// System.out.println("checking path " + loop.getIndex());
			for (int i2 = 0; i2 <= i1 - 1; i2++) { // Path loop2 :
													// feedbackLoops) {
				// for(int i2 = i1 + 1; i2 < feedbackLoops.size(); i2++) {
				// //Path loop2 : feedbackLoops) {

				Path loop2 = feedbackLoops.get(i2);
				if (comparePaths(loop, loop2)) {
					toRemove.add(loop);
					break;
				}
			}
		}
		// System.out.println(compareCount);
		for (Path pathToRemove : toRemove) {
			feedbackLoops.remove(pathToRemove);
		}

		count = 1;
		for (Path p : feedbackLoops) {
			p.setIndex(count);
			count++;
		}
	}

	private boolean comparePaths(Path path1, Path path2) {
		/*
		 * if(path1.getDelay() == 20 && path2.getDelay() == 20) {
		 * System.out.println(); }
		 */
		if (path1 == null || path2 == null) {
			return false;
		}
		ArrayList<Integer> array1 = new ArrayList<Integer>();
		for (Integer integer : path1.getNodes()) {
			array1.add(integer);
		}

		ArrayList<Integer> array2 = new ArrayList<Integer>();
		for (Integer integer : path2.getNodes()) {
			array2.add(integer);
		}

		// if paths not same size - false
		if (array1.size() != array2.size()) {
			return false;
		}

		// is size 0 - true
		if (array1.size() == 0) {
			return true;
		}
		array1.remove(array1.size() - 1);
		array2.remove(array2.size() - 1);

		/*
		 * Object[] arr1 = array1.toArray(); Object[] arr2 = array2.toArray();
		 */

		Integer firstElement = array1.get(0);

		// turn array2 to start with firstElement
		ArrayList<Integer> tempArr = new ArrayList<Integer>();
		for (int i = 0; i < array2.size(); i++) {
			tempArr.add(0);
		}
		int pointer = 0;
		int firstElementPosition = array2.indexOf(firstElement);
		// if array2 doesn't contain the first element of array1 - false
		if (firstElementPosition == -1) {
			return false;
		}

		if (firstElementPosition == 0) {
			return compareArrays(array1, array2);
		}

		for (int i = 0; i < array2.size(); i++) {
			pointer = firstElementPosition + i;
			if (pointer > array2.size() - 1) {
				pointer = pointer - array2.size();
			}
			tempArr.set(i, array2.get(pointer));
		}

		return compareArrays(array1, tempArr);

		/*
		 * for(int chainCounter = 0; chainCounter < array1.size(); chainCounter
		 * ++) { boolean equal = true; for(int compCounter = 0; compCounter <
		 * array1.size(); compCounter ++) {
		 * 
		 * if(array1.get(compCounter) != array2.get(compCounter)) { equal =
		 * false; break; } } if(equal) { return true; }
		 * 
		 * ArrayList<Integer> tempArray = new ArrayList<Integer>(); for(int k =
		 * 0; k < array2.size(); k++) { tempArray.add(0); } for(int k = 0; k <
		 * array2.size() - 1; k++) { tempArray.set(k + 1, array2.get(k)); }
		 * tempArray.set(0, array2.get(array2.size() - 1));
		 * 
		 * array2 = tempArray; String s3 = ""; for(int j : array2) { s3 = s3 + j
		 * + " "; } System.out.println("new array2: " + s3); }
		 */
		// return false;
	}

	/**
	 * Arguments must be equal size!!!!
	 * 
	 * @param arr1
	 * @param arr2
	 * @return
	 */
	private boolean compareArrays(ArrayList<Integer> arr1,
			ArrayList<Integer> arr2) {
		boolean result = true;
		for (int i = 0; i < arr1.size(); i++) {
			if (arr1.get(i) != arr2.get(i)) {
				result = false;
				break;
			}
		}
		return result;
	}

	private int calculatePositiveLoops() {
		int result = 0;
		for (Path p : feedbackLoops) {
			if (p.getEffect() > 0) {
				result++;
			}
		}
		return result;
	}

	private int calculateNegativeLoops() {
		int result = 0;
		for (Path p : feedbackLoops) {
			if (p.getEffect() < 0) {
				result++;
			}
		}
		return result;
	}

	private String findShortestLoop() {
		float delay = Float.MAX_VALUE;
		Path path = null;
		for (Path p : feedbackLoops) {
			if (p.getDelay() < delay) {
				delay = p.getDelay();
				path = p;
			}
		}

		String label = "";
		if (path == null) {
			return label;
		}
		for (Integer node : path.getNodes()) {
			label += String.valueOf(node) + "->";
		}
		label = label.substring(0, label.length() - 2);
		return label;
	}

	private String findLongestLoop() {
		float delay = 0;
		Path path = null;
		for (Path p : feedbackLoops) {
			if (p.getDelay() > delay) {
				delay = p.getDelay();
				path = p;
			}
		}

		String label = "";
		if (path == null) {
			return label;
		}
		for (Integer node : path.getNodes()) {
			label += String.valueOf(node) + "->";
		}
		label = label.substring(0, label.length() - 2);
		return label;
	}

	/**
	 * @return the variableIndex
	 */
	public ArrayList<Integer> getVariableIndices() {
		return variableIndices;
	}

	/**
	 * @param variableIndex
	 *            the variableIndex to set
	 */
	public void setVariableIndex(int variableIndex, ArrayList<Integer> arrayList) {
		this.variableIndices.clear();
		this.variableIndices.addAll(arrayList);
		this.variableIndices.add(variableIndex);
		pathsFinder = new PathsFinder(this.variableIndices);
		calculateFeedbackLoops();
	}

	public SelectionListener getShowPathsSelectionListener() {
		SelectionListener listener = new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell parentShell = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell();
				/*
				 * PathsDialog dialog = new
				 * PathsDialog(PlatformUI.getWorkbench()
				 * .getActiveWorkbenchWindow().getShell());
				 * dialog.setFeedbackLoops(feedbackLoops); dialog.create();
				 * dialog.open();
				 */

				PathsWindow window = new PathsWindow(parentShell);
				window.setFeedbackLoops(feedbackLoops);
				window.setTitle("Paths details for " + getTitle());
				window.create();
				window.open();

				/*
				 * window = new PathsWindow(parentShell);
				 * window.setFeedbackLoops(feedbackLoops); window.create();
				 * window.open();
				 */

				/*
				 * ScrollableDialog dialog = new
				 * ScrollableDialog(PlatformUI.getWorkbench
				 * ().getActiveWorkbenchWindow().getShell(), "title", "text",
				 * "scrollable text ;lasjdk ;adlskj a;sdlkj adfs;lkjads;lkjads ;laskjd a;fdslkjdsk hlksjh salkjhasdlkjdha lkajsihlsajhdfaslkjhadslkjsahlksjaih slkdajh sdlkjh slkjshd lkjhfd slkjhsfdlk jhlkjhlkjh ljkhfd lkjhask ljsahlkajshalkjhafdslkjalkjsahlsjhsfdkjhsafd df kjasth kajsh fdsajkh fdaskj dsfkjsath dfas fdskjhsad kjh asjhfd sakjha kjah ksjah fdasjkhd"
				 * ); dialog.create(); dialog.open();
				 */

				/*
				 * ConnectionDialog dialog = new
				 * ConnectionDialog(PlatformUI.getWorkbench
				 * ().getActiveWorkbenchWindow().getShell()); dialog.create();
				 * dialog.open();
				 */
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		};
		return listener;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Composite getComposite() {
		return composite;
	}

	public void setComposite(Composite composite) {
		this.composite = composite;
	}

	public Label getHeadline() {
		return headline;
	}

	public void setHeadline(Label headline) {
		this.headline = headline;
	}

	public Text getSumText() {
		return sumText;
	}

	public void setSumText(Text sumText) {
		this.sumText = sumText;
	}

	public Text getPositiveText() {
		return positiveText;
	}

	public void setPositiveText(Text positiveText) {
		this.positiveText = positiveText;
	}

	public Text getNegativeText() {
		return negativeText;
	}

	public void setNegativeText(Text negativeText) {
		this.negativeText = negativeText;
	}

	public Text getShortestText() {
		return shortestText;
	}

	public void setShortestText(Text shortestText) {
		this.shortestText = shortestText;
	}

	public Text getLongestText() {
		return longestText;
	}

	public void setLongestText(Text longestText) {
		this.longestText = longestText;
	}

	public void setStartButton(Button startButton) {
		this.startButton = startButton;
	}

	public Button getStartButton() {
		return startButton;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}