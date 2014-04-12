<?php

  $info = array(
    'title' => 'math',
    'type' => 'object',
    'tags' => array('math'),
    'params' => array(
      ),
    'nparams' => array(
      ),
    'see' => array('number', 'enc.o')
    );

?>
<h2>Description</h2>  

<p>
  The <code>math</code> objects contains a few useful math operations.
</p>

<h2>Object Members</h2>

<? displayFunctionHead('abs', array('value or list')) ?>

  <p>
  Returns the absolute value.
  </p>

  <?= formatCode("println (math.abs (list -2 -1 -3));") ?>
  
  <?= formatOutput("(list 2 1 3)") ?>
  
  <br/>


<? displayFunctionHead('acos', array('value or list')) ?>

  <p>
  Returns the arc cosine of the value.
  </p>

  <?= formatCode("println (math.acos 1);") ?>
  
  <?= formatOutput("0") ?>
  
  <br/>



<? displayFunctionHead('asin', array('value or list')) ?>

  <p>
  Returns the arc sine of the value.
  </p>

  <?= formatCode("println (math.asin 0);") ?>
  
  <?= formatOutput("0") ?>
  
  <br/>



<? displayFunctionHead('atan', array('value or list')) ?>

  <p>
  Returns the arc tangent of the value.
  </p>

  <?= formatCode("println (math.atan 0);") ?>
  
  <?= formatOutput("0") ?>
  
  <br/>



<? displayFunctionHead('atan2', array('value1', 'value2')) ?>

  <p>
  Returns the arc tangent of value1 and value2.
  </p>

  <?= formatCode("println 
  (math.atan2 3 2);") ?>
  
  <?= formatOutput("0.982793723247329") ?>
  
  <br/>



<? displayFunctionHead('cbrt', array('value or list')) ?>

  <p>
  Returns the cube root of the value.
  </p>

  <?= formatCode("println 
  (math.cbrt 27);") ?>
  
  <?= formatOutput("3") ?>
  
  <br/>



<? displayFunctionHead('ceil', array('value or list')) ?>

  <p>
  Returns the next integer that is higher than or equal to the value.
  </p>

  <?= formatCode("println 
  (math.ceil (list 1 1.603 2.9));") ?>
  
  <?= formatOutput("(list 1 2 3)") ?>
  
  <br/>



<? displayFunctionHead('cos', array('value or list')) ?>

  <p>
  Returns the cosine of the value.
  </p>

  <?= formatCode("println 
  (math.cos 0);") ?>
  
  <?= formatOutput("1") ?>
  
  <br/>



<? displayFunctionHead('cosh', array('value or list')) ?>

  <p>
  Returns the hyperbolic cosine of the value.
  </p>

  <?= formatCode("println 
  (math.cosh 0);") ?>
  
  <?= formatOutput("1") ?>
  
  <br/>


<? displayFunctionHead('deg', array('value or list')) ?>

  <p>
  Converts the value into degrees.
  </p>

  <?= formatCode("println 
  (math.round (math.deg 3.1415));") ?>
  
  <?= formatOutput("180") ?>
  
  <br/>



<? displayFunctionHead('exp', array('value or list')) ?>

  <p>
  Returns <i>e</i> to the power of <code>value</code>.
  </p>

  <?= formatCode("println 
  (math.exp 10);") ?>
  
  <?= formatOutput("22026.465794806718") ?>
  
  <br/>



<? displayFunctionHead('floor', array('value or list')) ?>

  <p>
  Returns the next integer that is lower than or equal to <code>value</code>.
  </p>

  <?= formatCode("println 
  (math.floor 1.3);") ?>
  
  <?= formatOutput("1") ?>
  
  <br/>



<? displayVariableHead('e') ?>

  <p>
  Contains Euler's number e.
  </p>

  <?= formatCode("println math.e;") ?>
  
  <?= formatOutput("2.718281828459045") ?>
  
  <br/>




<? displayFunctionHead('exponent', array('value or list')) ?>

  <p>
  Returns the unbiased exponent used in the internal representation of the number.
  </p>

  <?= formatCode("println 
  (math.exponent 2);") ?>
  
  <?= formatOutput("1") ?>
  
  <br/>



<? displayFunctionHead('fib', array('value')) ?>

  <p>
  Returns the last member of the Fibonacci sequence with a length of <code>value</code>.
  </p>

  <?= formatCode("println (math.fib 10000);") ?>
  
  <?= formatOutput("1242044891") ?>
  
  <br/>



<? displayFunctionHead('hypotenuse', array('x', 'y')) ?>

  <p>
  Returns the hypotenuse of x and y;
  </p>

  <?= formatCode("println 
  (math.hypotenuse 1 1);") ?>
  
  <?= formatOutput("1.4142135623730951") ?>
  
  <br/>



<? displayFunctionHead('logn', array('value or list')) ?>

  <p>
  Returns the natural logarithm of the value.
  </p>

  <?= formatCode("println 
  (math.logn 1);") ?>
  
  <?= formatOutput("0") ?>
  
  <br/>



<? displayFunctionHead('log10', array('value or list')) ?>

  <p>
  Returns the base 10 logarithm of the value.
  </p>

  <?= formatCode("println 
  (math.log10 1);") ?>
  
  <?= formatOutput("0") ?>
  
  <br/>



<? displayFunctionHead('max', array('v1', 'v2', '…')) ?>

  <p>
  Returns the highest of all the parameters given.
  </p>

  <?= formatCode("println 
  (math.max 1 2 3 3 2 1);") ?>
  
  <?= formatOutput("3") ?>
  
  <br/>



<? displayFunctionHead('min', array('v1', 'v2', '…')) ?>

  <p>
  Returns the lowest of all the parameters given.
  </p>

  <?= formatCode("println 
  (math.max 1 2 3 3 2 1);") ?>
  
  <?= formatOutput("1") ?>
  
  <br/>
  
  

<? displayVariableHead('pi') ?>

  <p>
  Contains the number &pi;.
  </p>

  <?= formatCode("println math.pi;") ?>
  
  <?= formatOutput("3.141592653589793") ?>
  
  <br/>


<? displayFunctionHead('power', array('value exponent')) ?>

  <p>
  Returns <code>value</code> to the power of <code>exponent</code>.
  </p>

  <?= formatCode("println 
  (math.power 10 3);") ?>
  
  <?= formatOutput("1000") ?>
  
  <br/>
 

<? displayFunctionHead('rad', array('value or list')) ?>

  <p>
  Converts <code>value</code> into radians.
  </p>

  <?= formatCode("println 
  (math.rad 180);") ?>
  
  <?= formatOutput("3.141592653589793") ?>
  
  <br/>



<? displayFunctionHead('random', array('from', 'to')) ?>

  <p>
  <code>(random)</code> generates an integer random number between the <i>from</i> and <i>to</i>
  parameters.
  </p>

  <?= formatCode("println (math.random 1 20);") ?>
  
  <?= formatOutput("20") ?>
  
  <br/>



<? displayFunctionHead('round', array('value or list')) ?>

  <p>
  Rounds <code>value</code> to the nearest integer.
  </p>

  <?= formatCode("println 
  (math.round 0.49999);") ?>
  
  <?= formatOutput("0") ?>
  
  <br/>



<? displayFunctionHead('signum', array('value or list')) ?>

  <p>
  Returns -1 if <code>value</code> is below zero, 0 if it's exactly zero, and 1 if it's above zero.
  </p>

  <?= formatCode("println 
  (math.signum 1230);") ?>
  
  <?= formatOutput("1") ?>
  
  <br/>



<? displayFunctionHead('sin', array('value or list')) ?>

  <p>
  Returns the sine of the value.
  </p>

  <?= formatCode("println 
  (math.sin 0);") ?>
  
  <?= formatOutput("0") ?>
  
  <br/>



<? displayFunctionHead('sinh', array('value or list')) ?>

  <p>
  Returns the hyperbolic sine of the value.
  </p>

  <?= formatCode("println 
  (math.sinh 0);") ?>
  
  <?= formatOutput("0") ?>
  
  <br/>



<? displayFunctionHead('sqrt', array('value or list')) ?>

  <p>
  Returns the square root of the value.
  </p>

  <?= formatCode("println 
  (math.sqrt 100);") ?>
  
  <?= formatOutput("10") ?>
  
  <br/>



<? displayFunctionHead('tan', array('value or list')) ?>

  <p>
  Returns the tangent of the value.
  </p>

  <?= formatCode("println 
  (math.tan 0);") ?>
  
  <?= formatOutput("0") ?>
  
  <br/>



<? displayFunctionHead('tanh', array('value or list')) ?>

  <p>
  Returns the hyperbolic tangent of the value.
  </p>

  <?= formatCode("println 
  (math.tanh 0);") ?>
  
  <?= formatOutput("0") ?>
  
  <br/>




