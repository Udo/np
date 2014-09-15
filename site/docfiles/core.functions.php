<?php

  $info = array(
    'title' => 'core functions',
    'type' => 'lib',
    'tags' => array(),
    'params' => array(
      ),
    'nparams' => array(
      ),
    );

?>
<h2>Description</h2>  

<p>
  np has a few core functions that are not part of any
  library. You can invoke those functions without any prefixes.
</p>

<? displayFunctionHead('assert', array('expression', 'message'), array()) ?>

  <p>
    The <code>assert()</code> function executes the <code>expression</code>.
    If that expression evaluates to <code>false</code>, the program is
    interrupted, producing the <code>message</code> as an error.
  </p>

  <?= formatCode("assert(false 'that was FALSE!!1!')") ?>
  
  <?= formatOutput("1: that was FALSE!!1!
stack traceback:
	[C]: in function 'assert'") ?>
  
  <br/>
  
<? displayFunctionHead('call', array('ƒ', '[params]'), array('ƒResult')) ?>

  <p>
    <code>call()</code> executes the function ƒ, optionally passing any 
    parameters that follow to it. Finally, it returns the result of the 
    function call.
  </p>

  <?= formatCode("new f = { v | => v, v*2, v^2 }
print( call(f 10) )") ?>
  
  <?= formatOutput("10 20 100") ?>
  
  <br/>
  
<? displayFunctionHead('first', array('param1', '[params...]'), array('result')) ?>

  <p>
    <code>first()</code> 
    takes a list of arbitrary many parameters and returns the first
    one that is not <code>nil</code>.
  </p>

  <?= formatCode("
new t3 = 'tadaa'
print( first( t1 t2 t3 ) )
") ?>
  
  <?= formatOutput("tadaa") ?>
  
  <br/>
  
<? displayFunctionHead('print', array('[params]'), array()) ?>

  <p>
    Prints all the values passed to it, internally using the :toString() behavior
    of the objects involved.
  </p>

  <?= formatCode("print(: 10 20 30)") ?>
  
  <?= formatOutput("(: 10 20 30 )") ?>
  
  <br/>
  
<? displayFunctionHead('raise', array('errorMessage'), array()) ?>

  <p>
    Exits the program with an <code>errorMessage</code>.
  </p>

  <?= formatCode("raise('woah! make it stop!')") ?>
  
  <?= formatOutput("1: woah! make it stop!
stack traceback:
	[C]: in function 'raise'") ?>
  
  <br/>
  
<? displayFunctionHead('select', array('fromIndex', 'ƒ⇠params'), array()) ?>

  <p>
    Gets a subset of the return values produced by ƒ, starting with the value at <code>fromIndex</code>.
  </p>

  <?= formatCode("new f = { v | => v, v*2, v^2 }
print( select(3, f(10)) )") ?>
  
  <?= formatOutput("100") ?>
  
  <br/>
  
<? displayFunctionHead('try', array('ƒ', '[failureHandler⇠reason]'), array('result')) ?>

  <p>
    Executes the function ƒ, returning its result. If ƒ raises a fatal error during
    execution, the <code>failureHandler</code> is invoked. If no failure handler is
    given and ƒ raises an error, <code>try</code> returns <code>false</code> and
    additional error information.
  </p>

  <?= formatCode("print( try{ |
  raise('fail!') 
  } )") ?>
  
  <?= formatOutput("false sourcefile.np:17: fail!") ?>
  
  <?= formatCode("try(
  { | raise('fail!') }, 
  { | print('FAILED') })") ?>
  
  <?= formatOutput("FAILED") ?>
  
  <br/>
  
<? displayFunctionHead('type', array('value'), array('typeName')) ?>

  <p>
    Returns the name of the type of <code>value</code>.
  </p>

  <?= formatCode("print( type(:) )") ?>
  
  <?= formatOutput("list") ?>
  
  <br/>
  
