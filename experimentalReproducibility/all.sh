# link in the dist directory from the code
sh links.sh
# git the test files
cd json
# construct the data sets
cd json
sh genOne.sh
sh genTwo.sh
sh genThree.sh
# load the data
cd ..
sh loadAll.sh
# run the experiments, results end up in figures folder
sh expOne.sh
sh expTwo.sh
sh expThree.sh
sh expFour.sh
# run latex
cd figures
pdflatex main.tex


