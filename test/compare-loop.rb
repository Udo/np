puts "---------------- Ruby test script --------------------"

$tbl = { }
$i = 0

while $i < 10000000  do
  $tbl['last'] = $i
  $i +=1
end

puts("end Ruby loop benchmark  #$i iterations")