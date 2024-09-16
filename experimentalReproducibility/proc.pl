my @values = (20, 40, 60, 80, 100);
my @results = ();
my $slcas = ();
while (<>) {
  my $line = $_;
  if ($line =~ /Result time (\d+\.\d+)ms/) {
     push @results, $1;
  } 
  if ($line =~ /SLCA time (\d+\.\d+)ms/) {
     push @slcas, $1;
  } 
}

my $i = 0;
while ($i < scalar @values) {
   my $v = ($i + 1) * 20;
   print "($v, $slcas[$i])\n";
   $i++;
}
$i = 0;
while ($i < scalar @values) {
   my $v = ($i + 1) * 20;
   print "($v, $results[$i])\n";
   $i++;
}
