import java.math.BigInteger;

import javax.swing.JProgressBar;

import client.view.ProgressItem;
import rsa.Factorizer;

public class MyTask implements Runnable {
	CodeBreaker codeBreaker;
	String message;
	BigInteger n;
	ProgressItem progressItem;
	JProgressBar mainProgressBar;
	
	public MyTask(CodeBreaker codeBreaker, String message, BigInteger n, ProgressItem progressItem, JProgressBar mainProgressBar) {
		this.codeBreaker = codeBreaker;
		this.message = message;
		this.n = n;
		this.progressItem = progressItem;
		this.mainProgressBar = mainProgressBar;
	}
	
	public void run() {
		try {			
			String decryptedCode = Factorizer.crack(message, n, new MyTracker(progressItem, mainProgressBar));
			codeBreaker.onCrack(decryptedCode, progressItem);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

	}
}

