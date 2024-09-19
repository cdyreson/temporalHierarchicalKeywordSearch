for i in {1..5}
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json twonba.json --disk --search "foo5 bar5"
done
for i in {1..5}
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json twonba.json --disk --search "foo4 bar4"
done
for i in {1..5}
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json twonba.json --disk --search "foo3 bar3"
done
for i in {1..5}
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json twonba.json --disk --search "foo2 bar2"
done
for i in {1..5}
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json twonba.json --disk --search "foo1 bar1"
done
