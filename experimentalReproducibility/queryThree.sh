for i in $(seq 1 5); 
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json three1dblp.json --disk --search "foo5 bar5"
done
for i in $(seq 1 5); 
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json three50dblp.json --disk --search "foo5 bar5"
done
for i in $(seq 1 5); 
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json three100dblp.json --disk --search "foo5 bar5"
done
