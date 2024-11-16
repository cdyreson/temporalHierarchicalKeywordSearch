# Process Trees dataset
sh queryTwoTrees.sh > results/twoTrees.txt
perl processExpTwoSLCA.pl <results/twoTrees.txt > figures/expTwoTreesSLCA.dat 
perl processExpTwoResult.pl <results/twoTrees.txt > figures/expTwoTreesResult.dat 
sh queryTwoTimeTrees.sh > results/twoTimeTrees.txt
perl processExpTwoSLCA.pl <results/twoTimeTrees.txt > figures/expTwoTimeTreesSLCA.dat 
perl processExpTwoResult.pl <results/twoTimeTrees.txt > figures/expTwoTimeTreesResult.dat 
# Process Nba dataset
sh queryTwoNba.sh > results/twoNba.txt
perl processExpTwoSLCA.pl <results/twoNba.txt > figures/expTwoNbaSLCA.dat 
perl processExpTwoResult.pl <results/twoNba.txt > figures/expTwoNbaResult.dat 
sh queryTwoTimeNba.sh > results/twoTimeNba.txt
perl processExpTwoSLCA.pl <results/twoTimeNba.txt > figures/expTwoTimeNbaSLCA.dat 
perl processExpTwoResult.pl <results/twoTimeNba.txt > figures/expTwoTimeNbaResult.dat
# Process Dblp dataset      
sh queryTwoDblp.sh > results/twoDblp.txt
perl processExpTwoSLCA.pl <results/twoDblp.txt > figures/expTwoDblpSLCA.dat 
perl processExpTwoResult.pl <results/twoDblp.txt > figures/expTwoDblpResult.dat 
sh queryTwoTimeDblp.sh > results/twoTimeDblp.txt
perl processExpTwoSLCA.pl <results/twoTimeDblp.txt > figures/expTwoTimeDblpSLCA.dat 
perl processExpTwoResult.pl <results/twoTimeDblp.txt > figures/expTwoTimeDblpResult.dat
