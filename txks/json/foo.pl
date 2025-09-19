use strict;
my $depth = 0;
my $element = "";
my $year = "2025";
while (<>) {
  my $s = $_;
  my $line = '';
  if ($s =~ m/\s*\"year\"\s*:\s*\"(\d+)\"/) {
    $year = "$1";
    $element .= $s;
  } elsif ($s =~ m/^\s*(\"\_id\":\s*)?\{/) {
    $depth++;
    # print "opening $depth\n";
    if ($depth > 2) {
      $element = $s;
    } elsif ($depth == 2) {
      
    } else {
      print "$s";
    }
  } elsif ($s =~ m/^\s*\},?\s*$/) {
    $depth--;
    # print "closing $depth\n";
    if ($depth == 1) {
      # $element = $s;
      print "{ \"#timestamp\": {\n    \"#time\": \"$year-$year\",\n    $element }$s"; 
    } elsif ($depth > 1) {
      $element .= $s;
    } else {
      print "$s";
    }  
  } else {
      if ($depth <= 1) {
        print "$s";
      } else {
        $element .= $s;
      }
  }

}
