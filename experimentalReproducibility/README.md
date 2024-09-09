# Temporal JSON Keyword Search Experimental Reproducibility
The experiments require Java (any version should do) and Linux (to run shell commands, any flavor should work). 

The experiments folder is organized as follows.

 - At the top level are `*.sh` files.  Each contains the code to run Java.   The `.sh` files are explained in more detail below.
 - The `json` directory contains the test JSON datasets.
 - The `dist` directory is the JAR of the TXKS system copied from the code directory.
 - The `lib` directory is the lib for running the system.

## Loading the Database
To run the experiments the JSON data needs to be parsed into a BerkeleyDB database.  We assume that the home directory is the `experimentalReproducibility` directory.  You can establish this as follows.
```
    cd experimentalReproducibility
```
First, get the datasets at https://www.dropbox.com/scl/fi/ifge4ylsj3depyasa5evg/json.tar.gz?rlkey=8vql1q9utkr5lk67ai34l730f&dl=0.  This will retrieve  `json.tar.gz`.  Next uncompress `json.tar.gz`.  You can use the command below.
```
    tar -xvzf json.tar.gz
```
We've compressed the source data to reduce the size from 23GB to 3.5GB.  It should reside in the `json` directory.

The JSON data is parsed into using the `load*.sh` scripts.  For instance running
```
    sh loadOneDblp.sh
```
will parse the DBLP JSON datasets for the first experiment and create corresponding database in the `Databases` directory.  There are three primary datasets: DBLP, NBA, and Trees.

## Running Experiment One
To run the experiment use the `queryOne*.sh` scripts.  Each script has a nontemporal and temporal version.  For instance running
```
    sh queryOneDBLP.sh
    sh queryOneTimeDBLP.sh
```
the first is the nontemporal run, while the second is the temporal run.  There are queries for all three datasets: DBLP, NBA, and Trees. 

## Running Experiment Two
To run the experiment use the `queryTwo*.sh` scripts.  Each script has a nontemporal and temporal version.  For instance running
```
    sh queryTwoDblp.sh
    sh queryTwoTimeDblp.sh
```
the first is the nontemporal run, while the second is the temporal run.  There are queries for all three datasets: DBLP, NBA, and Trees. 
