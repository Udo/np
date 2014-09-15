<?php

  $info = array(
    'title' => 'convert',
    'type' => 'lib',
    'tags' => array(),
    'params' => array(
      ),
    'nparams' => array(
      ),
    'see' => array('string.type')
    );

?>
<h2>Description</h2>  

<p>
  The <code>convert</code> library consists of functions intended for the conversion and
  the formatting of data.
</p>

<h2>Library Functions</h2>

<? displayFunctionHead('escapeHTML', array('raw'), array('escapedString')) ?>

  <p>
  Escapes a value such that HTML-relevant characters are transformed into their
  corresponding entities. 
  </p>

  <?= formatCode("print( convert.escapeHTML('<script>alert(\"foo!\");</script>') )") ?>
  
  <?= formatOutput(htmlspecialchars('<script>alert(\"foo!\");</script>')) ?>
  
  <br/>
  
<? displayFunctionHead('escapeURI', array('raw'), array('escapedString')) ?>

  <p>
  Encodes a string so it becomes a validly escaped URL component. 
  </p>

  <?= formatCode("print( '?b=' << convert.escapeURI('look at that!') )") ?>
  
  <?= formatOutput('?b=look%20at%20that%21') ?>
  
  <br/>

<? displayFunctionHead('format', array('formatString', 'value1', '...'), array('string')) ?>

  <p>
  Returns a formatted string according to the rules defined in <code>formatString</code>.
  </p>

  <?= formatCode("
print( convert.format('byte value to character: %c', 76) )
print( convert.format('exponential notation: %e', math.pi) )
print( convert.format('float: %f', math.pi) )
print( convert.format('float, expanded to 12 digits: %12f', math.pi) )
print( convert.format('float, with 12 decimal digits: %.12f', math.pi) )
print( convert.format('signed integer: %i', math.pi) )
print( convert.format('integer with leading zeros: %02i', math.pi) )
print( convert.format('octal: %o', 100) ) 
print( convert.format('quoted string: %q', 'world') )
print( convert.format('string: %s', 'hello') ) 
print( convert.format('string with padding: %10s', 'hello') ) 
print( convert.format('unsigned integer: %u', 100) )
print( convert.format('hex: %x %X', 100, 100) ) 
") ?>
  
  <?= formatOutput('byte value to character: L
exponential notation: 3.141593e+00
float: 3.141593
float, expanded to 12 digits:     3.141593
float, with 12 decimal digits: 3.141592653590
signed integer: 3
integer with leading zeros: 03
octal: 144
quoted string: "world"
string: hello
string with padding:      hello
unsigned integer: 100
hex: 64 64') ?>
  
  <br/>
  
<? displayFunctionHead('quote', array('value'), array('string')) ?>

  <p>
  Functionally identical to <code>convert.format('%q', someString)</code>,
  the quote function quote-escapes a value.
  </p>

  <?= formatCode("print( convert.quote('\"Look at me!') )") ?>
  
  <?= formatOutput('"\\"Look at me!"') ?>
  
  <br/>

<? displayFunctionHead('toNumber', array('value'), array('number')) ?>

  <p>
  Returns the numerical value of an object. If defined, this function uses the <code>toNumber</code> behavior of the object.
  </p>

  <?= formatCode("-- converting any object to a number
new ns = '2001'
print( convert.toNumber(ns)+3 )
-- using the toNumber behavior
new l = create(: toNumber = { |=> 1234 })
print( convert.toNumber(l) )") ?>
  
  <?= formatOutput("2004
1234") ?>
  
  <br/>

<? displayFunctionHead('toString', array('value'), array('string')) ?>

  <p>
  Returns the string representation of an object. If defined, this function uses the <code>toString</code> behavior of the object.
  </p>

  <?= formatCode("-- converting any object to a number
new ns = .101
print( convert.toString(ns) )
-- using the toString behavior
new l = create(: toString = { |=> 'Yo!' })
print( convert.toString(l) )") ?>
  
  <?= formatOutput("0.101
Yo!") ?>
  
  <br/>


<? displayFunctionHead('unquote', array('string'), array('string')) ?>

  <p>
  Reverses the escaping performed by <code>format.quote</code>.
  </p>

  <?= formatCode("print( convert.unquote('\"\\\"Look at me!\"') )") ?>
  
  <?= formatOutput('"Look at me!') ?>
  
  <br/>


