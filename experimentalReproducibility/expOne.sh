# Process Trees dataset
sh queryOneTrees.sh > results/oneTrees.txt
perl processExpSLCA.pl <results/oneTrees.txt > figures/expOneTreesSLCA.dat 
perl processExpResult.pl <results/oneTrees.txt > figures/expOneTreesResult.dat 
sh queryOneTimeTrees.sh > results/oneTimeTrees.txt
perl processExpSLCA.pl <results/oneTimeTrees.txt > figures/expOneTimeTreesSLCA.dat 
perl processExpResult.pl <results/oneTimeTrees.txt > figures/expOneTimeTreesResult.dat 
# Process Nba dataset
sh queryOneNba.sh > results/oneNba.txt
perl processExpSLCA.pl <results/oneNba.txt > figures/expOneNbaSLCA.dat 
perl processExpResult.pl <results/oneNba.txt > figures/expOneNbaResult.dat 
sh queryOneTimeNba.sh > results/oneTimeNba.txt
perl processExpSLCA.pl <results/oneTimeNba.txt > figures/expOneTimeNbaSLCA.dat 
perl processExpResult.pl <results/oneTimeNba.txt > figures/expOneTimeNbaResult.dat
# Process Dblp dataset      
sh queryOneDblp.sh > results/oneDblp.txt
perl processExpSLCA.pl <results/oneDblp.txt > figures/expOneDblpSLCA.dat 
perl processExpResult.pl <results/oneDblp.txt > figures/expOneDblpResult.dat 
sh queryOneTimeDblp.sh > results/oneTimeDblp.txt
perl processExpSLCA.pl <results/oneTimeDblp.txt > figures/expOneTimeDblpSLCA.dat 
perl processExpResult.pl <results/oneTimeDblp.txt > figures/expOneTimeDblpResult.dat
