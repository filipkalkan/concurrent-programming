import java.math.BigInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import client.view.StatusWindow;
import client.view.WorklistItem;
import client.view.ProgressItem;
import network.Sniffer;
import network.SnifferCallback;
import rsa.Factorizer;

public class CodeBreaker implements SnifferCallback {

    private final JPanel workList;
    private final JPanel progressList;
    private final ExecutorService threadPool;
    
    private final JProgressBar mainProgressBar;

    // -----------------------------------------------------------------------
    
    private CodeBreaker() {
        StatusWindow w  = new StatusWindow();

        workList        = w.getWorkList();
        progressList    = w.getProgressList();
        mainProgressBar = w.getProgressBar();
        // TODO: Make thread stuff
        
        threadPool = Executors.newFixedThreadPool(2);
        w.enableErrorChecks();
    }
    
    // -----------------------------------------------------------------------
    
    public static void main(String[] args) {

        /*
         * Most Swing operations (such as creating view elements) must be performed in
         * the Swing EDT (Event Dispatch Thread).
         * 
         * That's what SwingUtilities.invokeLater is for.
         */

        SwingUtilities.invokeLater(() -> {
            CodeBreaker codeBreaker = new CodeBreaker();
            new Sniffer(codeBreaker).start();
        });
    }

    // -----------------------------------------------------------------------

    /** Called by a Sniffer thread when an encrypted message is obtained. */
    @Override
    public void onMessageIntercepted(String message, BigInteger n){
        System.out.println("message intercepted (N=" + n + ")...");
        SwingUtilities.invokeLater(() -> {
        	createWorkListItem(message, n);
        });
    }

	private void createWorkListItem(String message, BigInteger n) {
    	JButton button = new JButton("Break");
    	WorklistItem workListItem = new WorklistItem(n, message);
    	
    	button.addActionListener((e) -> {
    		handleBreak(workListItem, message, n);
    	});
    	
    	workListItem.add(button);
    	workList.add(workListItem);
		
	}

	private void handleBreak(WorklistItem workListItem, String message, BigInteger n) {
		workList.remove(workListItem);
		ProgressItem progressItem = createProgressItem(message, n);
		
		progressItem.setTask(decryptAndDisplay(message, n, progressItem));
	}

	private ProgressItem createProgressItem(String message, BigInteger n) {
		ProgressItem progressItem = new ProgressItem(n, message);
		progressList.add(progressItem);
		
		JButton removeButton = new JButton("Remove");
		removeButton.addActionListener((e) -> {
			handleRemove(progressItem);
		});
		JButton cancelButton = progressItem.addCancelButton();
		cancelButton.addActionListener((e) -> {
			handleCancel(progressItem, cancelButton);
		});
		
		progressItem.add(removeButton);
		progressItem.add(cancelButton);
		return progressItem;
	}

	private void handleCancel(ProgressItem progressItem, JButton cancelButton) {
		progressItem.getTask().cancel(true);
		progressItem.getTextArea().setText("[cancelled]");
		int remainingProgress = progressItem.getProgressBar().getMaximum() - progressItem.getProgressBar().getValue();
		progressItem.getProgressBar().setValue(100);
		mainProgressBar.setValue(mainProgressBar.getValue() + remainingProgress);
		progressItem.removeCancelButton();
	}

	private void handleRemove(ProgressItem progressItem) {
		progressItem.getTask().cancel(true);
		progressList.remove(progressItem);
		mainProgressBar.setValue(mainProgressBar.getValue() - progressItem.getProgressBar().getValue());
		mainProgressBar.setMaximum(mainProgressBar.getMaximum() - 1000000);
	}

	private Future<String> decryptAndDisplay(String message, BigInteger n, ProgressItem progressItem) {
		return threadPool.submit(() -> {
			String decryptedMessage;
			try {
				decryptedMessage = Factorizer.crack(message, n, new MyTracker(progressItem, mainProgressBar));
				SwingUtilities.invokeLater(() -> {
					progressItem.getTextArea().setText(decryptedMessage);
					progressItem.removeCancelButton();
				});
			} catch (InterruptedException e) {
				
			}
			return null;
		});
	}
}
