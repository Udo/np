<?php

  $info = array(
    'title' => 'sys',
    'type' => 'object',
    'tags' => array('system'),
    'params' => array(
      ), 
    'nparams' => array(
      ),
    'see' => array('enc.o')
    );

?>
<h2>Description</h2>  

<p>
  The <code>sys</code> objects deals with system calls and operations.
</p>

<h2>Object Members</h2>

<? displayFunctionHead('bench', array('expressions')) ?>

  <p>
  Executes all the expressions handed to it and returns the amount of milliseconds it took to do so.
  The actual results of these computations (if any) are stored in the <code>result</code> member
  of the return object. <code>result</code> is a list where the results are stored in the same
  order as they were when the bench function was called.
  </p>

  <?= formatCode("println (sys.bench (enc.date (sys.time) #timezone:'GMT'));") ?>
  
  will output (for example):
  
  <?= formatOutput("0.401") ?>
  
  <br/>


<? displayFunctionHead('execTime', array('')) ?>

  <p>
  Returns the number of milliseconds the program has been running for.
  </p>

  <?= formatCode("println (sys.execTime);") ?>
  
  will output (for example):
  
  <?= formatOutput("1") ?>
  
  <br/>


<? displayFunctionHead('time', array('#unit:u')) ?>

  <p>
  Returns the current Unix timestamp of the system (in UTC). If the optional
  <code>#unit</code> parameter is used, you can use the setting <code>'sec'</code>
  to get seconds (default), or alternatively <code>'msec'</code> to get milliseconds.
  The number that gets returned is also assigned an attribute named <code>granularity</code>
  which specifies whether the return value is made up of seconds or milliseconds. 
  </p>

  <?= formatCode("println (sys.time);") ?>
  
  will output (for example):
  
  <?= formatOutput("1363137417") ?>
  
  <br/>



