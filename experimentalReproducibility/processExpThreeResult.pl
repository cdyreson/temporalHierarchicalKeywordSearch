my @trials = (1, 50, 100);
my $trial = 0;
my $count = 1;
my $total = 0;
while (<>) {
  if (/Result time (\d*.\d+)/) {
#    print "asdf $1\n";
    $total += $1;
    if ($count == 5) {
      print @trials[$trial] . " " . $total/$count . "\n";
      $count = 0;
      $trial++;
      $total = 0;
    }
    $count++;
  }
}