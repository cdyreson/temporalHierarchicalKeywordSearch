package usu.temporal.timestamp;

public class Internal {

  public static void Error(String s) {
/*
    System.out.println("\nInternal Error:" + s);
    System.err.println("\nInternal Error:" + s);
    System.out.flush();
    System.err.flush();
    System.exit(-1);
*/
    }

  public static void Warning(String s) {
    System.out.println("\nInternal Warning:" + s);
    System.err.println("\nInternal Warning:" + s);
    System.out.flush();
    System.err.flush();
    }
}
