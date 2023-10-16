use strict;
#my $number_args = $#ARGV + 1;  
#print "args are $number_args\n";
#while (--$number_args >= 0) {
#   print $ARGV[$number_args];
#}
#exit;

my $total = 18978;
my $x = $total/200;
my $count = 0;
my $i = 0;
my @words = ('"foo" : "w", ', '"bar" : "x", ');
#int(rand(10));
while (<>) {
  my $s = $_;
  my $line = '';
  while ($s =~ m/\"[ a-zA-Z0-9_]*\"\s*:/) {
    $s = $';
    $line .= $`;
    $count = $count + 1;
    if ($count >= $x) {
       $line = @words[$i];
       $i = ($i == 1)? 0: 1;
       $line .= $&;
       $count = 0;
    } else {
       $line .= $&;
    }
  }
  $line .= $s;
  print $line;
}
