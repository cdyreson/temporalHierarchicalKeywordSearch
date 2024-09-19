for i in $(seq 1 5); 
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json nba20.json --disk --search "@sequenced foo bar"
done
for i in $(seq 1 5); 
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json nba40.json --disk --search "@sequenced foo bar"
done
for i in $(seq 1 5); 
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json nba60.json --disk --search "@sequenced foo bar"
done
for i in $(seq 1 5); 
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json nba80.json --disk --search "@sequenced foo bar"
done
for i in $(seq 1 5); 
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json nba100.json --disk --search "@sequenced foo bar"
done