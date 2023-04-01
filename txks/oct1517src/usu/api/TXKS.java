/*
 */
package usu.api;

import usu.algebra.operator.Operator;
import usu.grammar.*;
import messiah.database.Database;

//usu.globals.Globals;
import java.io.*;
import java.util.*;
import org.antlr.runtime.*;
import usu.algebra.KeywordSearchExpression;

/**
 * TXKS is the TXKS API.
 * @author Curtis Dyreson
 */
public class TXKS {

    private Database db = null;
    private boolean verbose = true;

    /**
     * Open a new "inMemory" connection to XMorph 
     */
    public TXKS() {
        close();
        //System.out.println("new TXKS");
        if (db == null) {
            db = new messiah.database.memory.Database(true, /* does not matter */ false);
        }
    }

    /**
     * Open a new persistent connection to TXKS
     * String s - Name of the persistent db to use
     */
    public TXKS(String s) {
        close();
        //ystem.out.println("new TXKS plus " + s);
        if (db == null) {
            db = new messiah.database.berkeleydb.Database(s, true, true);
        }
    }

    public Database getDb() {
        return db;
    }

    /**
     * Compile a Search from the passed String.
     * @param searchQuery - A string
     * @return Operator - The parsed/compiled search, ready to execute
     */
    public KeywordSearchExpression compileString(String s) {
        TXKSLexer lex = null;

        try {
            if (verbose) System.out.println("Running query on " + s);
            lex = new TXKSLexer(new ANTLRStringStream(s));
        } catch (Exception t) {
            System.out.println("Error building the TXKS lexer: for the string " + s);
            t.printStackTrace();
            //System.exit(-1);
        }
        CommonTokenStream tokens = new CommonTokenStream(lex);
        TXKSParser parser = new TXKSParser(tokens);
        try {
            KeywordSearchExpression q = parser.program();
            //System.out.println("in here " + q);
            return q;
        } catch (TXKSParseException t) {
            System.out.println("TXKS.java: Error " + t.msg);
            //t.printStackTrace();
            //System.exit(-1);
        } catch (RecognitionException t) {
            System.out.println("TXKS.java: Parse Error " + t.getMessage() + " " + parser.getErrorMessage());
            //t.printStackTrace();
            //System.exit(-1);
        } catch (Exception t) {
            System.out.println("TXKS.java: other error ");
            t.printStackTrace();
            //System.exit(-1);
        }
        return null;
    }

    /**
     * Compile a SearchQuery stored in a file. The name
     * of the file is passed as a String.
     */
    public KeywordSearchExpression compile(String fileName) {
        TXKSLexer lex = null;

        try {
            if (verbose) System.out.println("Running query on " + fileName);
            lex = new TXKSLexer(new ANTLRFileStream(fileName));
        } catch (Exception t) {
            System.out.println("Error building the TXKS lexer: probably could not open file " + fileName);
            t.printStackTrace();
            //System.exit(-1);
        }
        CommonTokenStream tokens = new CommonTokenStream(lex);
        TXKSParser parser = new TXKSParser(tokens);
        try {
            KeywordSearchExpression q = parser.program();
            //System.out.println("in here " + q);
            return q;
        } catch (TXKSParseException t) {
            System.out.println("TXKS.java: Error " + t.msg);
            //t.printStackTrace();
            //System.exit(-1);
        } catch (RecognitionException t) {
            System.out.println("TXKS.java: Parse Error " + parser.getErrorMessage());
            //t.printStackTrace();
            //System.exit(-1);
        } catch (Exception t) {
            System.out.println("TXKS.java: other error ");
            t.printStackTrace();
            //System.exit(-1);
        }
        return null;
    }

    /**
     * Evaluate the XMorph program, producing a list of ShapeNodes
     * which represents the shape(s) to evaluate.
     */
    /*
    public List<ShapeNode> evaluate(Query query) {
        List<ShapeNode> shapes = new ArrayList();
        try {
            //System.out.println("Shape table is " + db.shapesTable.keySet().size());
            //System.out.println("Query is " + query);
            // XMorph tries on all the shapes in the Shapes Table
            if (Globals.dataShapes.isEmpty()) {
                for (String fname : db.shapesTable.keySet()) {
                    //System.out.println("Querying " + fname);
                    if (verbose) System.out.println("Shape of " + fname + " is \n" + db.shapesTable.get(fname).imageXML());
                    ShapeNode s = query.evaluate(db, db.shapesTable.get(fname));
                    if (verbose) System.out.println("Shape of query is \n" + s.imageXML());
                    shapes.add(s);
                }
            } // Exist tries on all the shapes in the dataShapes list
            else {
                for (ShapeNode d : Globals.dataShapes) {
                    //System.out.println("Querying " + fname);
                    if (verbose) System.out.println("Shape of data is \n" + d.imageXML());
                    ShapeNode s = query.evaluate(db, d);
                    if (verbose) System.out.println("Shape of query is \n" + s.imageXML());
                    shapes.add(s);

                }
            }
        } catch (Exception t) {
            System.out.println("XMC.java: Run-time error in running an XMorph program " + t.getMessage());
            t.printStackTrace();
            System.exit(-1);
        }
        return shapes;
    }
    */
    
    /**
     * Close up any open databases.
     */
    public void close() {
        if (db != null) {
            db.closeDatabase();
            db = null;
        }
    }
}
