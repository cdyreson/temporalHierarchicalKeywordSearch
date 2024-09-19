# Process Trees dataset
sh queryTwoTrees.sh > results/twoTrees.txt
perl processExpSLCA.pl <results/twoTrees.txt > figures/expTwoTreesSLCA.dat 
perl processExpResult.pl <results/twoTrees.txt > figures/expTwoTreesResult.dat 
sh queryTwoTimeTrees.sh > results/twoTimeTrees.txt
perl processExpSLCA.pl <results/twoTimeTrees.txt > figures/expTwoTimeTreesSLCA.dat 
perl processExpResult.pl <results/twoTimeTrees.txt > figures/expTwoTimeTreesResult.dat 
# Process Nba dataset
sh queryTwoNba.sh > results/twoNba.txt
perl processExpSLCA.pl <results/twoNba.txt > figures/expTwoNbaSLCA.dat 
perl processExpResult.pl <results/twoNba.txt > figures/expTwoNbaResult.dat 
sh queryTwoTimeNba.sh > results/twoTimeNba.txt
perl processExpSLCA.pl <results/twoTimeNba.txt > figures/expTwoTimeNbaSLCA.dat 
perl processExpResult.pl <results/twoTimeNba.txt > figures/expTwoTimeNbaResult.dat
# Process Dblp dataset      
sh queryTwoDblp.sh > results/twoDblp.txt
perl processExpSLCA.pl <results/twoDblp.txt > figures/expTwoDblpSLCA.dat 
perl processExpResult.pl <results/twoDblp.txt > figures/expTwoDblpResult.dat 
sh queryTwoTimeDblp.sh > results/twoTimeDblp.txt
perl processExpSLCA.pl <results/twoTimeDblp.txt > figures/expTwoTimeDblpSLCA.dat 
perl processExpResult.pl <results/twoTimeDblp.txt > figures/expTwoTimeDblpResult.dat
