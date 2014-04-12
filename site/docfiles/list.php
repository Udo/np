<?php

  $info = array(
    'title' => 'list',
    'type' => 'function',
    'tags' => array('object', 'list'),
    'params' => array(
      'args' => 'any number of arguments',
      ),
    'nparams' => array(
      ),
    'see' => array('map', 'list.o')
    );

?>
<h2>Description</h2>  

<p>
  <code>(list)</code> creates and returns a new list object. You may populate the list initially
  by passing any number of arguments to the function.
</p>

<h2>Examples</h2>

<?= formatCode("myList = (list 1 2 3 4);
println myList;") ?>

will output:

<?= formatOutput("(list '1' '2' '3' '4')") ?>

<p>
  &nbsp;
</p>

