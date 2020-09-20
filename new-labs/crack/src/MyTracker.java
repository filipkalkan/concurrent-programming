import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import client.view.ProgressItem;
import rsa.ProgressTracker;

public class MyTracker implements ProgressTracker {
	ProgressItem progressItem;
	JProgressBar mainProgressBar;
	int totalProgress;
	
	public MyTracker(ProgressItem progressItem, JProgressBar mainProgressBar) {
		this.progressItem = progressItem;
		this.mainProgressBar = mainProgressBar;
		mainProgressBar.setMaximum(mainProgressBar.getMaximum() + 1000000);
	}

	@Override
	public void onProgress(int ppmDelta) {
		this.totalProgress += ppmDelta;
		SwingUtilities.invokeLater(() -> {
			this.progressItem.getProgressBar().setValue(totalProgress);
			mainProgressBar.setValue(mainProgressBar.getValue() + ppmDelta);
		});
	}

}
