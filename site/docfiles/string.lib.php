<?php

  $info = array(
    'title' => 'string',
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
  The <code>string</code> library contains functions intended for the manipulation of strings.
  This library is also the default event list for <a href="http://np-lang.org/docs/string.type">strings</a>.
</p>

<h2>Behaviors</h2>

<? displayFunctionHead(':byte', array('fromIndex', '[toIndex]'), array('number')) ?>

  <p>
  Returns the numerical value of a given byte from the string. 
  </p>

  <?= formatCode("print( ('abcdef'):byte(1) )") ?>
  
  <?= formatOutput("97") ?>
  
  <br/>

<? displayFunctionHead(':chomp', array('stringToken'), array('string', 'string')) ?>

  <p>
  If found, returns the part of the string that came before the <code>stringToken</code>,
  returns the rest of the string as a second result. 
  </p>

  <?= formatCode("print( ('abcdefghijklmnopqrs'):chomp('def') )") ?>
  
  <?= formatOutput("abc	ghijklmnopqrs") ?>
  
  <br/>

<? displayFunctionHead(':copy', array('[fromIndex] [toIndex]'), array('string')) ?>

  <p>
  Returns a copy of the string, optionally a sub-string <code>fromIndex</code> to <code>toIndex</code>.
  If negative values are used for the indexes, the position is counted from the end of the string.
  Currently, :copy is not Unicode-aware.
  </p>

  <?= formatCode("print( ('abcdefghijklmnopqrs'):copy(3,-2) )") ?>
  
  <?= formatOutput("cdefghijklmnopq") ?>
  
  <br/>

<? displayFunctionHead(':characters', array('ƒ⇠char')) ?>

  <p>
  Iterates through all the character bytes of the string and calls ƒ on them. Currently, :characters is not Unicode-aware.
  </p>

  <?= formatCode("('abc'):characters{ c | print(c) }") ?>
  
  <?= formatOutput("a, b, c") ?>
  
  <br/>

<? displayFunctionHead(':find', array('needlePattern', '[fromIndex]', '[plain]'), array('number', 'number')) ?>

  <p>
  Finds the first occurence of <code>needlePattern</code> and returns the position of its start and end (or nil if it's not found).
  </p>

  <?= formatCode("print( ('abc def'):find('%s') )") ?>
  
  <?= formatOutput('4, 4') ?>
  
  <br/>

<? displayFunctionHead(':length', array(), array('number')) ?>

  <p>
  Returns the length of the string (in byte characters, currently). Currently, :length is not Unicode-aware.
  </p>

  <?= formatCode("print( ('abc def'):length() )") ?>
  
  <?= formatOutput('7') ?>
  
  <br/>

<? displayFunctionHead(':lower', array(), array('string')) ?>

  <p>
  Returns a lower case version of the string. Currently, :lower is not Unicode-aware.
  </p>

  <?= formatCode("print( ('abc DEF'):lower() )") ?>
  
  <?= formatOutput('abc def') ?>
  
  <br/>

<? displayFunctionHead(':match', array('pattern', '[fromIndex]'), 'string') ?>

  <p>
  Returns the first matching substring as specified by the <code>pattern</code>.
  </p>

  <?= formatCode("print( ('I have 2 questions for you.'):match('%d+ %a+') )") ?>
  
  <?= formatOutput('2 questions') ?>
  
  <br/>

<? displayFunctionHead(':parse', array('regex', 'ƒ⇠stringMatch,startPos,endPos')) ?>

  <p>
  Matches the string against the regular (POSIX) expression <code>regex</code>,
  and calls ƒ for every match.
  </p>

  <?= formatCode("('a very simple sipple is simxle string'):parse('s..[a-z]le' { s start end | print(s) })") ?>
  
  <?= formatOutput('simple
sipple
simxle') ?>
  
  <br/>

<? displayFunctionHead(':repeat', array('count', '[seperator]')) ?>

  <p>
  Repeats the string <code>count</code> times and returns it.
  </p>

  <?= formatCode("print( ('Biggie Smalls'):repeat(3 ', ') )") ?>
  
  <?= formatOutput('Biggie Smalls, Biggie Smalls, Biggie Smalls') ?>
  
  <br/>

<? displayFunctionHead(':replace', array('replaceAll', 'withString')) ?>

  <p>
  Replaces all occurrences of <code>replaceAll</code> with <code>withString</code>
  and returns the result. No Lua patterns or regular expressions are used.
  </p>

  <?= formatCode("print(  
  ('bananananana banana bananana'):replace('nana', 'NoNa')
  )") ?>
  
  <?= formatOutput('baNoNaNoNana baNoNa baNoNana') ?>
  
  <br/>


<? displayFunctionHead(':reverse', array()) ?>

  <p>
    Reverses the string and returns it. Currently, :reverse is not Unicode-aware.
  </p>

  <?= formatCode("print( ('abcdefg'):reverse() )") ?>
  
  <?= formatOutput('gfedcba') ?>
  
  <br/>

<? displayFunctionHead(':split', array('seperator')) ?>

  <p>
    Splits the string into an array, returning the array.
  </p>

  <?= formatCode("print( ('a,b,c,d'):split(',') )") ?>
  
  <?= formatOutput("(: 'a', 'b', 'c', 'd')") ?>
  
  <br/>

<? displayFunctionHead(':substitute', array('patternString', 'replaceWith', '[max]'), array('string', 'replacementCount')) ?>

  <p>
    Finds all occurrences matching the <code>patternString</code> and replaces them with 
    <code>replaceWith</code>. <code>patternString</code> can be a Lua pattern.
  </p>

  <?= formatCode("print( ('hello world'):substitute('world', 'np-%1') )") ?>
  
  <?= formatOutput("hello np-world	1") ?>
  
  <br/>


<? displayFunctionHead(':trim', array(), array('string')) ?>

  <p>
    Returns a version of the string with all leading and trailing whitespace removed.
  </p>

  <?= formatCode("print( '-' << ('    np np np  '):trim() << '-' )") ?>
  
  <?= formatOutput("-np np np-") ?>
  
  <br/>

<? displayFunctionHead(':upper', array(), array('string')) ?>

  <p>
    Returns an upper case version of the string.
  </p>

  <?= formatCode("print( ('make me shout'):upper() )") ?>
  
  <?= formatOutput("MAKE ME SHOUT") ?>
  
  <br/>




<!---------------------------------------------------------------->
<h2>Library Functions</h2>

<? displayFunctionHead('string.char', array('asciiValue'), array('string')) ?>

  <p>
  Given an ASCII code, returns the appropriate character.
  </p>

  <?= formatCode("print( string.char(65) )") ?>
  
  <?= formatOutput("A") ?>
  
  <br/>


