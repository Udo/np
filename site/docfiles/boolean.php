<?php

  $info = array(
    'title' => 'boolean',
    'type' => 'function',
    'tags' => array('object', 'boolean'),
    'params' => array(
      'arg' => 'single argument to convert into a boolean',
      ),
    'nparams' => array(
      ),
    'see' => array('boolean.o', 'number', 'string')
    );

?>
<h2>Description</h2>  

<p>
  <code>(boolean)</code> creates and returns a new Boolean object. The value of the object is equal to the parameter given.
  If the parameter is not boolean, it will be converted into one.
</p>

<h2>Examples</h2>

<?= formatCode("println (boolean 1);") ?>

will output:

<?= formatOutput("true") ?>

<p>
  &nbsp;
</p>


