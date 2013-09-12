package game;
import java.awt.*;
import java.awt.event.*;

        // Just handle WINDOW_CLOSING events.
        // The constructor's argument determines whether to exit
        // the program when the window is closed.
public class WindowCloser extends WindowAdapter {
    public WindowCloser(){
        // Exit by default.
        this(true);
    }

    public WindowCloser(boolean exitOnClose){
        setExitOnClose(exitOnClose);
    }

    public void windowClosing(WindowEvent e){
        // Find out which window has been closed.
        Window w = e.getWindow();
        // Hide it.
        w.setVisible(false);
        // Free its resources.
        w.dispose();
        if(exitOnClose()){
            System.exit(0);
        }
    }

    protected boolean exitOnClose(){
        return this.exitOnClose;
    }

    protected void setExitOnClose(boolean b){
        exitOnClose = b;
    }

    private boolean exitOnClose = false;
}

