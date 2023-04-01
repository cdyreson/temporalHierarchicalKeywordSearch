/*
 * Copyright (C) 2017 Curtis Dyreson
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package messiah;

import java.io.File;
import messiah.database.berkeleydb.Database;

/**
 * Copy a nonTemporalDB to a TemporalDB and add timestamps randomly generated in
 * the range to rangeMax and of random size to intervalMax
 *
 * @author Curtis Dyreson
 */
public class CopyAndAddTimestamps {

    public static void main(String[] args) {
        int consumed = 0;
        Database fromDB;
        Database toDB;
        int rangeMax;
        int intervalMax;
        String xmlFileName = Config.XML_FOLDER_STRING + "curt.xml";
        String dbName = Config.DB_FOLDER_STRING;

        if (!args[consumed].contentEquals("--from")) {
            System.err.println("Bad Args from ");
            System.exit(-1);
        }
        consumed++;
        String fileName = args[consumed];
        consumed++;
        dbName = Config.DB_FOLDER_STRING + fileName;
        fromDB = new messiah.database.berkeleydb.Database(dbName, true, false);

        if (!args[consumed].contentEquals("--to")) {
            System.err.println("Bad Args to");
            System.exit(-1);
        }
        consumed++;
        fileName = args[consumed];
        consumed++;
        dbName = Config.DB_FOLDER_STRING + fileName;
        new File(dbName).mkdir();
        toDB = new messiah.database.berkeleydb.Database(dbName, false, true);

        if (!args[consumed].contentEquals("--rangeMax")) {
            System.err.println("Bad Args");
            System.exit(-1);
        }
        consumed++;
        Integer i = new Integer(args[consumed]);
        rangeMax = i.intValue();
        consumed++;

        if (!args[consumed].contentEquals("--intervalMax")) {
            System.err.println("Bad Args");
            System.exit(-1);
        }
        consumed++;
        i = new Integer(args[consumed]);
        intervalMax = i.intValue();
        consumed++;

        fromDB.copyNonTemporaltoTemporalDatabase(toDB, rangeMax, intervalMax);
        toDB.closeDatabase();
        fromDB.closeDatabase();
    }
}
