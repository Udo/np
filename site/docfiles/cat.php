<?php

  $info = array(
    'title' => 'cat',
    'type' => 'function',
    'tags' => array('i/o'),
    'params' => array(
      'args' => 'any number of arguments'
      ),
    'nparams' => array(
      'sep' => 'separate entries by this string',
      'first' => 'append this directly after the first entry',
      'last' => 'prefix the last entry with this',
      'pre' => 'prefix all entries with this',
      'post' => 'postfix all entries with this',
      ),
    'see' => array('print', 'println')
    );

?>
<h2>Description</h2>  

The <code>(cat)</code> function concatenates all of the arguments given to it as strings and returns the
result as a large string. You can use the <code>#sep</code> parameter to define a separator string between
the entries. In the absence of <code>#sep</code>, elements are simply joined together directly.

<h2>Examples</h2>

<?= formatCode("alist = cat 'cat' 'dog' 'turtle' #sep:', ' #pre:'a ' #last:'and ';
println alist ' walk into a bar';") ?>

will output:

<?= formatOutput("a cat, a dog, and a turtle walk into a bar") ?>