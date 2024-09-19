for i in $(seq 1 5); 
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json dblp20.json --disk --search "@sequenced foo bar"
done
for i in $(seq 1 5); 
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json dblp40.json --disk --search "@sequencedfoo bar"
done
for i in $(seq 1 5); 
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json dblp60.json --disk --search "@sequencedfoo bar"
done
for i in $(seq 1 5); 
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json dblp80.json --disk --search "@sequencedfoo bar"
done
for i in $(seq 1 5); 
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json dblp100.json --disk --search "@sequencedfoo bar"
done