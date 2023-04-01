package messiah.database;

/**
* A class used to handle internal errors. 
*
* @author Curtis Dyreson 
**/
public class DBError {

  /**
  * Print an error message and then continue.  
  **/
  public static void Error(String s) {
    System.out.println("\nInternal Error:" + s);
    System.err.println("\nInternal Error:" + s);
    System.out.flush();
    System.err.flush();
    //System.exit(-1);
    }
}
