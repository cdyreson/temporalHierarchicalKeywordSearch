package test;

/**
 * Parse the command line setting various options
 * @author Curtis
 */
//import database.Database;
//import database.berkeleyDB.Database;
public class ParseCmdLine {

    public static Boolean renderXQuery = false;
    public static Boolean renderXML = true;
    public static Boolean renderNothing = false;
    public static Boolean renderSQL = false;
    public static String dbDirectory = ".";
    public static String inputXMLFileName = null;
    public static Boolean parseFlag = false;
    public static String searchQueryFileName = null;

    public static void parse(String args[]) {
        int i = 0, j;
        String arg;

        // Default to inmemory
        messiah.database.Database.inMemoryDB = true;
        
        while (i < args.length && (args[i].startsWith("-") || args[i].startsWith("--"))) {
            arg = args[i++];

            //System.out.println("arg is " + arg);
            // use this type of check for "wordy" arguments
            if (arg.equals("-verbose") || arg.equals("-v") || arg.equals("--verbose")) {
                usu.grammar.TXKSParser.verbose = true;
            } else if (arg.equals("-memory") || arg.equals("-m") || arg.equals("--memory")) {
                messiah.database.Database.inMemoryDB = true;
            } else if (arg.equals("-parse") || arg.equals("-p") || arg.equals("--parse")) {
                parseFlag = true;
                if (i < args.length) {
                    inputXMLFileName = args[i++];
                } else {
                    System.err.println("-parse requires an XML file name");
                }
            } else if (arg.equals("-db")) {
                if (i < args.length) {
                    messiah.database.Database.inMemoryDB = false;
                    dbDirectory = args[i++];
                } else {
                    System.err.println("-db requires a directory name");
                }



            } 
            // use this type of check for a series of flag arguments
            //else {
            //    for (j = 1; j < arg.length(); j++) {
            //        flag = arg.charAt(j);
            //        switch (flag) {
            //            case 'x':
            //                break;
            //            case 'n':
            //                break;
            //            default:

            //                System.err.println("ParseCmdLine: illegal option " + flag);
            //                break;
            //        }
            //    }
            //}
        }
        if (i == args.length) {
            System.err.println("Usage: TXKS [-verbose] [-parse afile] [-db directory] filename");
        } else {
            searchQueryFileName = args[i];
        }
    }
}
    

