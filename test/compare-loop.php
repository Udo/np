<?php

$i = 0;
$tbl = array();
$x = 0;
while($i < 10000000)
{
  $tbl['last'] = $i*$i*$i;
  $i = $i +1;
}
