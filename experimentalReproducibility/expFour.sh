# Process Dblp nonsequenced query      
sh queryFourTimeDblp.sh > results/fourTimeDblp.txt
perl processExpSLCA.pl <results/fourTimeDblp.txt > figures/expFourTimeDblpSLCA.dat 
perl processExpResult.pl <results/fourTimeDblp.txt > figures/expFourTimeDblpResult.dat