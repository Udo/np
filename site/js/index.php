<?php

header('content-type: text/javascript');

foreach(explode(',', 'main.js,vendor/modernizr-2.6.2-respond-1.1.0.min.js,jquery.min.js') as $js)
  print(file_get_contents($js)."\n");

?>