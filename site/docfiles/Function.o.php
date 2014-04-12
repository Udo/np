<?php

  $info = array(
    'title' => 'Function',
    'type' => 'object',
    'tags' => array('prototype'),
    'params' => array(
      ),
    'nparams' => array(
      ),
    'see' => array()
    );

?>
<h2>Description</h2>  

<p>
  The <code>Function</code> prototype defines how function objects behave.
</p>

<h2>Object Members</h2>

<? displayVariableHead('argCount', array()) ?>

  <p>
  <code>argCount</code> is a variable containing the number of arguments that were passed
  into the function.
  </p>

  <br/>

<? displayVariableHead('container', array()) ?>

  <p>
  The <code>container</code> member represents the larger object the function belongs to.
  </p>

  <?= formatCode("v = 4;
v.squareme = 
  { return container * container };
n = 5;
n.s = v.squareme;
println 'v: ' (v.squareme);
println 'n: ' (n.s);") ?>
  
  <?= formatOutput("v: 16
n: 25") ?>
  
  <br/>


<? displayFunctionHead('pop', array()) ?>

  <p>
  The <code>(pop)</code> function retrieves the next pending argument
  that was passed into the current function. If there are no more arguments
  pending, an empty null object is returned. 
  </p>
  
  <p>
  Arguments that have already been declared in the function header will
  not be retrieved this way.
  </p>
  
  <p>
  Note that this gives you the option of retrieving a function object from
  the arguments list without executing it in the process. 
  </p>

  <?= formatCode("f = { (pop) 123 };
f { a | println 'function was passed as a parameter ' a };") ?>
  
  <?= formatOutput("function was passed as a parameter") ?>
  
  <br/>


<? displayVariableHead('this', array()) ?>

  <p>
  The <code>this</code> member points to the context of the function object itself.
  This is important because variables used at run time default to the current
  function call only and don't influence the function object itself. Use <code>this</code>
  to store and retrieve data that should last beyond the current function call.
  </p>

  <?= formatCode("f = { a b | this.[a] = b };
f 'test' 'hello';
println f.test;") ?>
  
  <?= formatOutput("hello") ?>
  
  <br/>


<? displayFunctionHead('return', array('value')) ?>

  <p>
    <code>(return)</code> terminates execution of the function immediately and 
    delivers the return value to the caller of the function.
  </p>
  
  <p>
    If no <code>return</code> statement takes place, the function terminates at its
    natural end and delivers the value of the statement that was executed last.
  </p>

  <?= formatCode("f = { a | return (math.sqrt a+40) };
println (f 24);") ?>
  
  <?= formatOutput("8") ?>
  
  <br/>
