package client.view;

import javax.swing.JComponent;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

/**
 * A RepaintManager that is less forgiving than stock Swing.
 * Detects illegal Swing access (i.e., from other threads than the EDT).
 * 
 * Based on ideas from https://stackoverflow.com/a/17760977
 */
public class StrictRepaintManager extends RepaintManager {

    @Override
    public synchronized void addInvalidComponent(JComponent component) {
        check(component);
        super.addInvalidComponent(component);
    }

    @Override
    public void addDirtyRegion(JComponent component, int x, int y, int w, int h) {
        check(component);
        super.addDirtyRegion(component, x, y, w, h);
    }

    private static void check(JComponent c) {

        // If the current thread is the EDT, all is fine.
        // Otherwise, we need to check more carefully.

        if (!SwingUtilities.isEventDispatchThread()) {
            
            Error e = new SwingThreadingError();
            boolean repaintInvoked = false;
            for (StackTraceElement st : e.getStackTrace()) {
                if (repaintInvoked && st.getClassName().startsWith("javax.swing.")) {
                    // If repaint() was invoked from within Swing, we assume it was
                    // due to a modification of some kind. That would be an error. 
                    throw e;
                }
                if (st.getMethodName().equals("repaint")) {
                    repaintInvoked = true;
                }
            }

            if (!repaintInvoked) {
                // If repaint() was _not_ invoked, we got here because another
                // Swing method was invoked. That is also presumed to be an error.
                throw e;
            }
        }
    }
}

// ===========================================================================

@SuppressWarnings("serial")
class SwingThreadingError extends Error {
    public SwingThreadingError() {
        super("Swing accessed from thread '" + Thread.currentThread().getName() + "'");
        printStackTrace();
    }
}
