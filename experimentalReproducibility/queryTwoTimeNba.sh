for i in {1..5}
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json twotnba.json --disk --search "@sequenced foo5 bar5"
done
for i in {1..5}
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json twotnba.json --disk --search "@sequenced foo4 bar4"
done
for i in {1..5}
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json twotnba.json --disk --search "@sequenced foo3 bar3"
done
for i in {1..5}
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json twotnba.json --disk --search "@sequenced foo2 bar2"
done
for i in {1..5}
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json twotnba.json --disk --search "@sequenced foo1 bar1"
done
