
package messiah;

import javax.swing.UIManager;

/**
 *
 * @author aswath
 * Edited by TEO KEE LIN (2010)
 */
public class Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            //invoke ui
            TempUserInterface ui = new TempUserInterface();
            ui.setVisible(true);

        } catch (Exception evt) {}

    }
}
