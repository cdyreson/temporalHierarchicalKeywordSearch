my $count = 1;
my $total = 0;
while (<>) {
  if (/SLCA time (\d*.\d+)/) {
    my $slca = $1;
    print "$1\n";
    $total += $1;
    print $total/$count . "\n";
    $count++;
  }
}
