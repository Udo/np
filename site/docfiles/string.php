<?php

  $info = array(
    'title' => 'string',
    'type' => 'function',
    'tags' => array('object', 'string'),
    'params' => array(
      'args' => 'arguments to convert into a string',
      ),
    'nparams' => array(
      ),
    'see' => array('string.o', 'cat', 'number', 'boolean')
    );

?>
<h2>Description</h2>  

<p>
  <code>(string)</code> creates and returns a new String object. You may populate the string initially
  by passing any number of parameters to the function. 
</p>

<h2>Examples</h2>

<?= formatCode("println (string 1 2 3);") ?>

will output:

<?= formatOutput("123") ?>

<p>
  &nbsp;
</p>


