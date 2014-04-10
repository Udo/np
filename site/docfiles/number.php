<?php

  $info = array(
    'title' => 'number',
    'type' => 'function',
    'tags' => array('object', 'number'),
    'params' => array(
      'arg' => 'single argument to convert into a number',
      ),
    'nparams' => array(
      ),
    'see' => array('number.o', 'string', 'boolean')
    );

?>
<h2>Description</h2>  

<p>
  <code>(number)</code> creates and returns a new Number object. The value of the number is equal to the parameter given.
  If the parameter is not numeric, it will be converted into a number.
</p>

<h2>Examples</h2>

<?= formatCode("println (number '123.45');") ?>

will output:

<?= formatOutput("123.45") ?>

<p>
  &nbsp;
</p>


