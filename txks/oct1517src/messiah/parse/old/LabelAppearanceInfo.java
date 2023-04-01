/*
 * Class to get the number of appearances of a label, for frequency counting
 */
package messiah.parse;

/**
 *
 */
public class LabelAppearanceInfo {
    
    private final int numApp;

    public int getNumApp() {
        return numApp;
    }
    
    private final int numAppInParent;

    public int getNumAppInParent() {
        return numAppInParent;
    }

    public LabelAppearanceInfo(int numApp, int numAppInParent) {
        this.numApp = numApp;
        this.numAppInParent = numAppInParent;
    }
}
