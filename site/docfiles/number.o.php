<?php

  $info = array(
    'title' => 'Number',
    'type' => 'object',
    'tags' => array('prototype'),
    'params' => array(
      ),
    'nparams' => array(
      ),
    'see' => array('number', 'math.o')
    );

?>
<h2>Description</h2>  

<p>
  The <code>Number</code> object is the prototype for all numbers in np. 
</p>

<h2>Object Members</h2>

<? displayFunctionHead('ceil', array('')) ?>

  <p>
  Returns the next integer that is higher than or equal to the number.
  </p>

  <?= formatCode("println (20.1).(ceil);") ?>
  
  <?= formatOutput("21") ?>
  
  <br/>

<? displayFunctionHead('downTo', array('value', '{ … }')) ?>

  <p>
  Counts down to <code>value</code>, executing the function argument every time.
  </p>

  <?= formatCode("(20).downTo 10 { a | 
  println a ': ' (a.even) };") ?>
  
  <?= formatOutput("20: true
19: false
18: true
17: false
16: true
15: false
14: true
13: false
12: true
11: false
10: true") ?>
  
  <br/>


<? displayFunctionHead('even', array('')) ?>

  <p>
  Returns <code>true</code> if the number is even, <code>false</code> if it is not.
  </p>

  <?= formatCode("print (3).(even);") ?>
  
  <?= formatOutput("false") ?>
  
  <br/>


<? displayFunctionHead('floor', array('')) ?>

  <p>
  Returns the previous integer that is equal to or lower than the number.
  </p>

  <?= formatCode("println (36.2).(floor);") ?>
  
  <?= formatOutput("36") ?>
  
  <br/>


<? displayFunctionHead('mod', array('value')) ?>

  <p>
  Returns the modulo of the number with <code>value</code>;
  </p>

  <?= formatCode("println (23).(mod 4);") ?>
  
  <?= formatOutput("3") ?>
  
  <br/>


<? displayFunctionHead('round', array('')) ?>

  <p>
  Rounds the number to the next integer and returns that.
  </p>

  <?= formatCode("println (34.3234).(round);") ?>
  
  <?= formatOutput("34") ?>
  
  <br/>


v
<? displayFunctionHead('signum', array('')) ?>

  <p>
  Returns -1 if the number is less than zero, 0 if it is zero, and 1 if it is greater than zero.
  </p>

  <?= formatCode("println (34).(signum);") ?>
  
  <?= formatOutput("1") ?>
  
  <br/>


<? displayFunctionHead('times', array('{ … }')) ?>

  <p>
  Executes the function argument as many times as the number indicates.
  </p>

  <?= formatCode("(5).times { a | println a };") ?>
  
  <?= formatOutput("1
2
3
4
5") ?>
  
  <br/>


<? displayFunctionHead('upTo', array('value', '{ … }')) ?>

  <p>
  Executes the function argument for every integer step on the way
  between the number and the <code>value</code> argument.
  </p>

  <?= formatCode("(10).upTo 15 { a | println a };") ?>
  
  <?= formatOutput("10
11
12
13
14
15") ?>
  
  <br/>

