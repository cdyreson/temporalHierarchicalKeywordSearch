package test;

/**
 * Simple test of Morph queries. runs main
 * @author Curtis
 */
import usu.api.TXKS;
//import xmorph.edu.usu.grammar.*;
//import xmorph.edu.usu.algebra.*;
//import xmorph.edu.usu.shape.ShapeNode;
//import xmorph.edu.usu.reporting.*;
//import xmorph.edu.usu.evaluation.normal.Graph;
import messiah.database.Database;
//import xmorph.edu.usu.evaluation.normal.Render;
//import xmorph.edu.usu.parser.XMLParser;
//import org.xml.sax.*;
//import org.xml.sax.helpers.XMLReaderFactory;
//import java.io.*;
//import java.util.*;

//import org.antlr.runtime.*;

public class Main {
    static boolean verbose = false;
    
    public static void main(String args[]) throws Exception {
        ParseCmdLine.parse(args);
        TXKS txks;
        if (Database.inMemoryDB) {
            txks = new TXKS();
            if (verbose) System.out.println("Setting txks");
        } else {
            txks = new TXKS(ParseCmdLine.dbDirectory);
        }

        if (ParseCmdLine.parseFlag) {
            String fileName = ParseCmdLine.inputXMLFileName;
            if (verbose) System.out.println("Shredding " + fileName);
            //txks.loadXMLinDB(fileName);
            txks.compile(fileName);
        }

        // Evaluate a query
        if (verbose) System.out.println("Running TXKS ");
        System.out.println(txks.compile(ParseCmdLine.searchQueryFileName));
        //xmc.run(ParseCmdLine.xmorphQueryFileName);
        txks.close();
    }
}
