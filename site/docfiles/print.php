<?php

  $info = array(
    'title' => 'print',
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
    'see' => array('cat', 'println', 'unsafeprint')
    );

?>
<h2>Description</h2>  

<p>
  The <code>(print)</code> function concatenates all of the arguments given to it as strings, writes the result
  to the output stream and also returns it as a string. You can use the <code>#sep</code> parameter to define 
  a separator string between the entries. In the absence of <code>#sep</code>, elements are simply joined 
  together directly.
</p>

<p>
  This function encodes its output to transform common HTML characters into their corresponding entities.
  For example, the angle bracket character <code>&gt;</code> becomes <code>&amp;gt;</code>. This is a safety
  feature designed to protect against unclean variable contents. If you need to output raw text, use
  <code>(unsafeprint)</code> instead.
</p>

<h2>Examples</h2>

<?= formatCode("print 'cat' 'dog' 'turtle' #sep:', ' #pre:'a ' #last:'and ';") ?>

will output:

<?= formatOutput("a cat, a dog, and a turtle") ?>