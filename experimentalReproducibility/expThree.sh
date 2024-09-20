# Process Dblp query     
sh queryThree.sh > results/three.txt
perl processExpThreeSLCA.pl <results/three.txt > figures/expThreeSLCA.dat 
perl processExpThreeResult.pl <results/three.txt > figures/expThreeResult.dat