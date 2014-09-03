<?php

  $info = array(
    'title' => 'string',
    'type' => 'type',
    'tags' => array(),
    'params' => array(
      ),
    'nparams' => array(
      ),
    'see' => array('string.lib')
    );

?>
<h2>Description</h2>  

<p>
  Strings are one of the basic types of np, together with numbers, booleans, nil, and lists.
  Since strings in np are binary-safe, they can contain any combination of bytes, including
  Unicode characters. However, string behaviors currently don't respect the byte boundaries
  of Unicode code points.
</p>

<h2>String Literals</h2>

<p>
  There are multiple options to express a string literal in np.
</p>

<h3>String Symbols</h3>

<p>The string symbol notation is for short alphanumeric strings without any other characters.</p>

<?= formatCode("print( #shortString )") ?>

<h3>Quoted String Literals</h3>

<p>You can use either single or double quotes to make a string:</p>

<?= formatCode("new string1 = 'look at me'") ?>

<p>String literals can have line breaks in them:</p>

<?= formatCode("new string2 = 'at the age old pond
a frog leaps into water
a deep resonance'") ?>

<h3>Bracketed</h3>

<p>Another way to build a string is to use double square brackets:</p>

<?= formatCode("[[this is also a string]]") ?>

<p>Double squares can also be extended by inserting one or more equals signs (=) in between them, for example to allow for nested brackets inside the string itself:</p>

<pre class="sh_np">[=[<i style="color:green;font-weight:normal;">this is a string with [[brackets]] inside</i>]=]</pre>

<h2>Concatenating Strings</h2>

<p>To chain two or more strings together, use the concat operator <code>&lt;&lt;</code> or its mutable variant <code>&lt;&lt;=</code>:</p>

<?= formatCode("print( 'Somewhere' << 'Over' << 'The' << 'Rainbow' )") ?>

<p>Numbers and strings can be concatenated together into strings:</p>

<?= formatCode("print( 99 << ' bottles of beer on the wall' )") ?>

<h2>String Behavior</h2>

<p>String objects come with their own built-in behaviors as defined in the <a href="http://np-lang.org/docs/string.lib"><code>string</code> library</a>.</p>