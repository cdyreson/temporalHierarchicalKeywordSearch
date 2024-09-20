for i in $(seq 1 5); 
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json twotdblp.json --disk --search "@nonsequenced foo5 @intersects bar5"
done
for i in $(seq 1 5); 
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json twotdblp.json --disk --search "@nonsequenced foo4 @intersects bar4"
done
for i in $(seq 1 5); 
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json twotdblp.json --disk --search "@nonsequenced foo3 @intersects bar3"
done
for i in $(seq 1 5); 
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json twotdblp.json --disk --search "@nonsequenced foo2 @intersects bar2"
done
for i in $(seq 1 5); 
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json twotdblp.json --disk --search "@nonsequenced foo1 @intersects bar1"
done

