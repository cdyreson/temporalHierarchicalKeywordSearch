package messiah.parse;

/**
 * This class contains helper methods to support processing path expression.
 * It should contain only helper static methods
 * @author John Truong Ba Quan
 */
public class PathExprUtilities {
    public static String getLastLabel(String pathExpr) {
        int lastPeriodPos = pathExpr.lastIndexOf('.');
        int lastHashPos = pathExpr.length() - 1;
        
        return pathExpr.substring(lastPeriodPos+1, lastHashPos);
    }
    
    public static String getParentPathExpr(String pathExpr) {
        int lastPeriodPos = pathExpr.lastIndexOf('.');
        return pathExpr.substring(0, lastPeriodPos);
    }
    
    public static int getLevel(String pathExpr) {
        return pathExpr.length() - pathExpr.replaceAll("#","").length();
    }
}
