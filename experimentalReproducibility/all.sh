# link in the dist directory from the code
sh links.sh
# git the test files
cd json
# construct the data sets
sh genOne.sh
sh genTwo.sh
# load the data
cd ..
sh loadAll.sh
# run the first experiment, results end up in figures folder
sh expOne.sh
# run the second experiment, results end up in figures folder
sh expTwo.sh
# run latex
cd figures
pdflatex main.tex


