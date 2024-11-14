# Temporal JSON Keyword Search Experimental Reproducibility
The experiments have the following system requirements.

 - `Java` (latest version)
 - `Linux` (to run shell commands, any flavor should work)
 - `Perl` (any version)
 - `pdflatex` to create the figures, but as this is a manual step any latex implementation should suffice
 - `curl` to get the datasets (present in most Linux versions already)
 - `tar` to uncompress the datasets
 - `git` to get the code

### Optional section - skip this if the system requirements are already met 
If your system lacks these features we've set up a Dockerfile in the top level of the `experimentalReproducibility` folder to construct a Docker image with the required components, except for `pdflatex`. Create the image using.
```
   cd experimentalReproducibility
   docker buildx build --no-cache --file Dockerfile-java -t txks .     
```
Create the container and access it using the following (or on windows the Exec feature in Docker Desktop for the container).
```
   docker run --entrypoint "/bin/sh" -it txks 
``` 
To copy the `figures` from the running container to the host system use the following.
```
   docker cp xxxxxxxx:/work/temporalHierarchicalKeywordSearch/experimentalReproducibility/figures  .
```
where you need to replace `xxxxxxxxxx` with your container id (use `docker ps` to see the container id).

## Running the Experiments
First git the Temporal Keyword Search system.
```
    git clone https://github.com/cdyreson/temporalHierarchicalKeywordSearch.git
```
Next change to `experimentalReproducibility` directory in the downloaded code.
```
    cd temporalHierarchicalKeywordSearch/experimentalReproducibility
```
Run the script `getData.sh` as follows.
```
    sh getData.sh
```
The script will execute a curl call to download the data.  Be sure to wait until the call completes (the  `json.tar.gz` file will be created and populated, it is several GB in size so should take some time to download).

Run the script `all.sh` as follows.
```
    sh all.sh
```
It will take approximately two hours for the script to complete.  The latex code for the figures in the experiment will be generated in the `figures` directory. Change to that directory and latex the document as follows.
```
    cd figures
    pdflatex main.tex
```
This should create `main.pdf` which you can view.

## More Explanation about the Experimental Reproducibility Setup
The experiments folder is organized as follows.

 - At the top level are `*.sh` files.  Each contains the code to run Java.   The `.sh` files are explained in more detail below.  There are also `*.pl` files, which are Perl scripts, to process the data.
 - The `json` directory contains the test JSON datasets.
 - The `dist` directory is the JAR of the TXKS system copied from the code directory.
 - The `results` directory will get the results of the experiments.
 - The `figures` directory will get the data files and latex code for creating the figures.

### Loading the Database
To run the experiments the JSON data needs to be parsed into a BerkeleyDB database.  We assume that the home directory is the `experimentalReproducibility` directory.  You can establish this as follows.
```
    cd experimentalReproducibility
```
First, get the datasets at https://www.dropbox.com/scl/fi/ifge4ylsj3depyasa5evg/json.tar.gz?rlkey=8vql1q9utkr5lk67ai34l730f&dl=0.  This will retrieve  `json.tar.gz`.  Next uncompress `json.tar.gz`.  You can use the command below.
```
    tar -xvzf json.tar.gz
```
We've compressed the source data to reduce the size from 23GB to 3.5GB.  It should reside in the `json` directory.

The JSON data is parsed and stored in BerkelyDB using the `loadAll.sh` script.   Run it using
```
    sh loadAll.sh
```
will parse the DBLP JSON datasets for the first experiment and create corresponding database in the `Databases` directory.  There are three primary datasets: DBLP, NBA, and Trees.

### Running the Experiments
Once the databases are set up the four experiments can be run as follows.
```
    sh expOne.sh
    sh expTwo.sh
    sh expThree.sh
    sh expFour.sh
```
