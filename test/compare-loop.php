---------------- PHP test script --------------------
<?php

$i = 0;
$tbl = array();
while($i < 10000000)
{
  $i = $i +1;
  $tbl['test'] = $i;
}

print('end PHP loop benchmark  '.$i.' iterations');
