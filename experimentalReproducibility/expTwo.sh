# Process Trees dataset
sh queryTwoTrees.sh > results/TwoTrees.txt
perl processExpSLCA.pl <results/TwoTrees.txt > figures/expTwoTreesSLCA.dat 
perl processExpResult.pl <results/TwoTrees.txt > figures/expTwoTreesResult.dat 
sh queryTwoTimeTrees.sh > results/TwoTimeTrees.txt
perl processExpSLCA.pl <results/TwoTimeTrees.txt > figures/expTwoTimeTreesSLCA.dat 
perl processExpResult.pl <results/TwoTimeTrees.txt > figures/expTwoTimeTreesResult.dat 
# Process Nba dataset
sh queryTwoNba.sh > results/TwoNba.txt
perl processExpSLCA.pl <results/TwoNba.txt > figures/expTwoNbaSLCA.dat 
perl processExpResult.pl <results/TwoNba.txt > figures/expTwoNbaResult.dat 
sh queryTwoTimeNba.sh > results/TwoTimeNba.txt
perl processExpSLCA.pl <results/TwoTimeNba.txt > figures/expTwoTimeNbaSLCA.dat 
perl processExpResult.pl <results/TwoNTimeba.txt > figures/expTwoTimeNbaResult.dat
# Process Dblp dataset      
sh queryTwoDblp.sh > results/TwoDblp.txt
perl processExpSLCA.pl <results/TwoDblp.txt > figures/expTwoDblpSLCA.dat 
perl processExpResult.pl <results/TwoDblp.txt > figures/expTwoDblpResult.dat 
sh queryTwoTimeDblp.sh > results/TwoTimeDblp.txt
perl processExpSLCA.pl <results/TwoTimeDblp.txt > figures/expTwoTimeDblpSLCA.dat 
perl processExpResult.pl <results/TwoTimeDblp.txt > figures/expTwoTimeDblpResult.dat
