use strict;
#my $number_args = $#ARGV + 1;  
#print "args are $number_args\n";
#while (--$number_args >= 0) {
#   print $ARGV[$number_args];
#}
#exit;

my $total = $ARGV[0];
my $overall = 200;
my $x = ($total * $ARGV[1])/(100 * 200);
my $fileName = $ARGV[2];
my $count = 0;
my $i = 0;
my @words = ('"foo" : { "bar" : "x" }, ');
#int(rand(10));
open(FH, '<', $fileName) or die $!;
while (<FH>) {
  my $s = $_;
  my $line = '';
  while ($s =~ m/\"[ a-zA-Z0-9_]*\"\s*:/) {
    $s = $';
    $line .= $`;
    $count = $count + 1;
#    print "$overall $count $x\n";
    if ($overall > 0 && $count >= $x) {
       $line .= @words[0];
#       $line .= @words[1];
       $i = ($i == 1)? 0: 1;
       $line .= $&;
       $count = 0;
       $overall--;
    } else {
       $line .= $&;
    }
  }
  $line .= $s;
  print $line;
}
