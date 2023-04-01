package messiah.parse;

import java.io.File;

import messiah.database.Database;


/**
 *
 * @author Amani Shatnawi
 */
public class RepresentationalParseTask {

    private final RepresentationalParser parser;

    public RepresentationalParseTask(Database db, File parsedFile) {
        this.parser = new RepresentationalParser(db, parsedFile);
    }

    public void doInBackground() {
        parser.createListeners();
        parser.startParser();
    }

}
