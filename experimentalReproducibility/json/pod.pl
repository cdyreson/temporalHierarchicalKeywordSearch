use strict;
#my $number_args = $#ARGV + 1;  
#print "args are $number_args\n";
#while (--$number_args >= 0) {
#   print $ARGV[$number_args];
#}
#exit;

my $total = $ARGV[0];
my $overall = 5000;
my $x = ($total * $ARGV[1])/(100 * 5000);
my $fileName = $ARGV[2];
my $count = 0;
my $i = 0;
my @words = ('"foo" : "w", ', '"bar" : "x", ');
my @phrases = ('"foo1" : { "bar1" : "x" }, ',
               '"foo1" : { "bar1" : "x" }, ' .
               '"foo2" : { "bar2" : "x" }, ',
               '"foo1" : { "bar1" : "x" }, ' .
               '"foo2" : { "bar2" : "x" }, ' .
               '"foo3" : { "bar3" : "x" }, ',
               '"foo1" : { "bar1" : "x" }, ' .
               '"foo2" : { "bar2" : "x" }, ' .
               '"foo3" : { "bar3" : "x" }, ' .
               '"foo4" : { "bar4" : "x" }, ',
               '"foo1" : { "bar1" : "x" }, ' .
               '"foo2" : { "bar2" : "x" }, ' .
               '"foo3" : { "bar3" : "x" }, ' .
               '"foo4" : { "bar4" : "x" }, ' .
               '"foo5" : { "bar5" : "x" }, '
              );

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
       $line .= @phrases[$overall % 5];
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
