<?php

  $info = array(
    'title' => 'unsafePrint',
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
    'see' => array('cat', 'println', 'print')
    );

?>
<h2>Description</h2>  

<p>
  The <code>(unsafePrint)</code> function concatenates all of the arguments given to it as strings, writes the result
  to the output stream and also returns it as a string. You can use the <code>#sep</code> parameter to define 
  a separator string between the entries. In the absence of <code>#sep</code>, elements are simply joined 
  together directly.
</p>

<p>
  This function does not encode its output in any way. For most purposes, you should use <code>(print)</code> instead.
</p>

<h2>Examples</h2>

<?= formatCode("unsafePrint 'cat' 'dog' 'turtle' #sep:', ' #pre:'a ' #last:'and ';") ?>

will output:

<?= formatOutput("a cat, a dog, and a turtle") ?>