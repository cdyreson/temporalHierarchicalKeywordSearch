# Process Dblp nonsequenced query      
sh queryFourTimeDblp.sh > results/fourTimeDblp.txt
perl processExpTwoSLCA.pl <results/fourTimeDblp.txt > figures/expFourTimeDblpSLCA.dat 
perl processExpTwoResult.pl <results/fourTimeDblp.txt > figures/expFourTimeDblpResult.dat