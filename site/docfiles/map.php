<?php

  $info = array(
    'title' => 'map',
    'type' => 'function',
    'tags' => array('object', 'map'),
    'params' => array(
      'x:args' => 'any number of named arguments',
      ),
    'nparams' => array(
      ),
    'see' => array('list', 'map.o')
    );

?>
<h2>Description</h2>  

<p>
  <code>(map)</code> creates and returns a new map object. You may populate the map initially
  by passing any number of named arguments to the function.
</p>

<h2>Examples</h2>

<?= formatCode("m = map 'a':1 'b':2 'c':3;
println m::'a';") ?>

will output:

<?= formatOutput("1") ?>

<p>
  &nbsp;
</p>


