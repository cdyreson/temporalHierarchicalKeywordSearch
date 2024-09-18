for i in {1..5}
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json trees20.json --disk --search "foo bar"
done
for i in {1..5}
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json trees40.json --disk --search "foo bar"
done
for i in {1..5}
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json trees60.json --disk --search "foo bar"
done
for i in {1..5}
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json trees80.json --disk --search "foo bar"
done
for i in {1..5}
do
    java -jar dist/TemporalKeywordSearch.jar --verbose --json trees100.json --disk --search "foo bar"
done
