# Welcome to the Temporal Keyword Search Project

The project is setup as an Apache Netbeans project (version 22), so it is easiest to use NetBeans. But the code has been compiled to a JAR file which can also be run on the command line.  Skip the next section if you use Netbeans.

## Building using the command line
The project can be compiled and run through the command line using Apache Ant. Some useful targets are given below.

To compile the project and construct the jarfile for the project use
    ```
      ant jar  
    ```
    Unlike building the jarfile through NetBeans however, the `lib` jarfiles are not included in the build, but the `ant jar` build will print the `java` command needed to run the project and include all of the necessary libraries.
    
There is a `run.xml` file that has numerous run targets setup.  Any of these can be run using
```
      ant -f run.xml xxxxx
```
   where `xxxxx` is the name of the target.
 
# Using Temporal Keyword Search
There are two ways to easily interact with the system.  The first is through a GUI.  Navigate in NetBeans to the 
file 
```
  src/Messiah/Application.java
```
and choose to `Run File` by right-clicking on the file. 

The second method is through the command line interface.  
```
   java -jar dist/TemporalKeywordSearch.jar messiah/Application.java
```

There are many targets set up in the Ant file `run.xml` at the top level.  These can be run through NetBeans (right-click on the target to run it) or Ant on the command line, or from the command line. 

The following command line flags are available.

### --verbose
Turn on verbose mode to report on internals of the search.

### --json filename
Use the JSON file (or database created from that file).

### --xml fileName
Use the XML file (or database created from that file).

### --memory
Use an in-memory database.

### --disk
Create/use an disk-resident database.

### --parse
Use the XML parser to parse a file and create the database (in memory or on disk).

### --jsonParse
Use the JSON parser to parse a file and create the database (in memory or on disk).

### --timestamps
Use the temporal search engine, timestamps are present

### --maxRange n
Add timestamps during parsing, this is the size of the timeline, e.g., 100.

### --maxInterval n
Within the range how big should each timestamp interval be, e.g., 20?

### --randomIntervals
Make each temporal interval of random size, between 1 and maxInterval.

### --search "query"
Specify the search query, example would be

  - "foo bar"
  - "@sequenced foo bar"
  - "@nonsequenced foo @contains bar"
