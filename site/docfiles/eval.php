<?php

  $info = array(
    'title' => 'eval',
    'type' => 'function',
    'tags' => array('runtime'),
    'params' => array(
      'src' => 'source code to be executed'
      ),
    'nparams' => array(
      'vars' => 'map with variables to inject',
      ),
    'see' => array()
    );

?>
<h2>Description</h2>  

<p>
  <code>(eval)</code> executes a string as np source code. Programs being executed within
  the eval statement do not have access to the environment of their parent program. If you
  want to hand over parameters to the inside of the eval statement, use the <code>#vars</code>
  parameter. 
</p>

<p>
  After execution, this function returns an object with several member variables. One is
  <code>output</code>, which contains the output stream of the eval operation. The member
  variable <code>result</code> will contain the operation's result object (if any). The
  <code>root</code> variable allows access to the eval's root variable context.
</p>

<h2>Examples</h2>

<?= formatCode("result = eval 'x = 1337; print 123;';
println 'The output was: ' result.output;
println 'The value of x: ' result.root.x;") ?>

will output:

<?= formatOutput("The output was: 123
The value of x: 1337") ?>