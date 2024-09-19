my @trials = (20, 40, 60, 80, 100);
my $trial = 0;
my $count = 1;
my $total = 0;
while (<>) {
  if (/SLCA time (\d*.\d+)/) {
    my $slca = $1;
#    print "asdf $1\n";
    $total += $1;
    if ($count == 5) {
      print @trials[$trial] . " " . $total/$count . "\n";
      $count = 0;
      $trail++;
      $total = 0;
    }
    $count++;
  }
}
