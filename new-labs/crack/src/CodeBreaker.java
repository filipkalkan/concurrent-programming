import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Insets;
import java.math.BigInteger;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.BoxLayout;
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
    private final Executor threadPool;
    
    private final JProgressBar mainProgressBar;

    // -----------------------------------------------------------------------
    
    private CodeBreaker() {
        StatusWindow w  = new StatusWindow();

        workList        = w.getWorkList();
        progressList    = w.getProgressList();
        mainProgressBar = w.getProgressBar();
        // TODO: Make thread stuff
        
        threadPool = Executors.newFixedThreadPool(2);
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
        	JButton button = new JButton("Break");
        	WorklistItem workListItem = new WorklistItem(n, message);
        	
        	button.addActionListener((e) -> {
        		workList.remove(workListItem);
        		ProgressItem progressItem = new ProgressItem(n, message);
        		progressList.add(progressItem);
        		
        		// Crack-Cocaine
        		threadPool.execute(() -> {
					try {
						String decryptedCode = Factorizer.crack(message, n, new MyTracker(progressItem, mainProgressBar));
						progressItem.getTextArea().setText(decryptedCode);
						
						JButton removeButton = new JButton("Remove");
						removeButton.addActionListener((e2) -> {
							progressList.remove(progressItem);
							mainProgressBar.setValue(mainProgressBar.getValue() - 1000000);
							mainProgressBar.setMaximum(mainProgressBar.getMaximum() - 1000000);
						});
						progressItem.add(removeButton);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				});
        	});
        	
        	workListItem.add(button);
        	workList.add(workListItem);
        });
    }
}
