# link in the dist directory from the code
sh links.sh
# curl -L -O https://www.dropbox.com/scl/fi/ifge4ylsj3depyasa5evg/json.tar.gz?rlkey=8vql1q9utkr5lk67ai34l730f&dl=0
tar -xzf json.tar.gz
# construct the data sets
cd json
ln -s twotdblp.json three1dblp.json
ln -s twotdblp.json three50dblp.json
ln -s twotdblp.json three100dblp.json
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


