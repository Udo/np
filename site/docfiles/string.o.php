<?php

  $info = array(
    'title' => 'String',
    'type' => 'object',
    'tags' => array('prototype'),
    'params' => array(
      ),
    'nparams' => array(
      ),
    'see' => array('string', 'cat')
    );

?>
<h2>Description</h2>  

<p>
  The <code>String</code> object is the prototype for all strings in np. It contains 
  a few useful methods for handling string data. All strings in np are interpreted as
  UTF-8. 
</p>

<h2>Examples</h2>

<?= formatCode("println 'Yo, String here.';") ?>

will output:

<?= formatOutput("Yo, String here.") ?>

<h2>Object Members</h2>

<? displayFunctionHead('count', array()) ?>

  <p>
  The <code>(count)</code> function returns the length of the String object.
  </p>

  <?= formatCode("println 'some string'.(count);") ?>
  
  <?= formatOutput("11") ?>
  
  <br/>


<? displayFunctionHead('each', array('#sep', '{ fragment idx | … }')) ?>

  <p>
    The <code>(each)</code> function splits the String into single characters
    (or, if the optional <code>#sep</code> argument is used: into other pieces
    separated by #sep) and then iterates over the fragments.
  </p>
  
  <?= formatCode("'some string'.
  (each { c | 
    print c ' ' });") ?>
  
  <?= formatOutput(" s o m e   s t r i n g ") ?>
  
  <br/>


<? displayFunctionHead('endsWith', array('match')) ?>

  <p>
    Returns true if the string ends with the match string.
  </p>

  <?= formatCode("println 'a b c d'.(endsWith 'a');") ?>
  
  <?= formatOutput("false") ?>
  
  <br/>


<? displayFunctionHead('item', array('position')) ?>

  <p>
    The <code>(item)</code> function returns a single character out of a String at the given position.
  </p>

  <?= formatCode("println 'some string'.(item 0);") ?>
  
  <?= formatOutput("s") ?>
  
  <br/>


<? displayFunctionHead('lower', array('')) ?>

  <p>
    Returns a lower case version of the string.
  </p>

  <?= formatCode("println 'The QUICK brown FOX'.(lower);") ?>
  
  <?= formatOutput("the quick brown fox") ?>
  
  <br/>


<? displayFunctionHead('nibble', array('match')) ?>

  <p>
    With <code>(nibble)</code> you can look for the next instance of the matched string, return
    everything before that string, and shorten the original string by the same amount. 
  </p>

  <?= formatCode("s = 'the quick brown fox';
println (s.nibble 'brown ');
println s;") ?>
  
  <?= formatOutput("the quick 
fox") ?>
  
  <br/>

<? displayFunctionHead('pad', array('length', '#at:opt', '#with:char')) ?>

  <p>
    The <code>(pad)</code> function returns the string plus extra padding until it reaches the given
    <code>length</code>. The optional <code>#at</code> parameter can either be <code>'end'</code> or
    <code>'start'</code> to indicate where the padding should occur. The optional parameter <code>#with</code>
    allows to choose a custom padding character instead of the default single space.
  </p>

  <?= formatCode("println '1'.(pad 10 #with:'-') ':';
println '12'.(pad 10) ':';
println '134'.(pad 10) ':';") ?>
  
  <?= formatOutput("1---------:
12        :
134       :") ?>
  
  <br/>

<? displayFunctionHead('part', array('from length')) ?>

  <p>
    The <code>(part)</code> function returns a part of the string, starting from a certain position,
    having a certain length. If the <code>length</code> argument is omitted, it will return the entire
    remaining length of the string. If <code>length</code> is negative, this is interpreted as counting
    backwards from the end of the string. if <code>from</code> is negative, it will return as many characters
    from the right side of the string. 
  </p>

  <?= formatCode("println 'the quick brown fox'.(part 0 -10);") ?>
  
  <?= formatOutput("the quick") ?>
  
  <br/>


<? displayFunctionHead('pos', array('match')) ?>

  <p>
    The <code>(pos)</code> function looks for the position of the string <code>match</code> and returns it.
  </p>

  <?= formatCode("println 'the quick brown fox'.(pos 'quick');") ?>
  
  <?= formatOutput("4") ?>
  
  <br/>
  
<? displayFunctionHead('rxEach', array('#sep', '{ fragment idx | … }')) ?>

  <p>
    The <code>(rxEach)</code> function splits the String into single characters
    (or, if the optional <code>#sep</code> argument is used: into other pieces
    separated by #sep) and then iterates over the fragments.
  </p>
  
  <p>
    The optional parameter <code>#sep</code> is interpreted as a regular expression.
  </p>

  <?= formatCode("'some string'.
  (rxEach #sep:' ' { c | 
    print c '-' });") ?>
  
  <?= formatOutput("some-string-") ?>
  
  <br/>

<? displayFunctionHead('rxReplace', array('match:new')) ?>

  <p>
  The <code>(rxReplace)</code> function returns a string where all occurrences of
  <code>match</code> have been replaced with <code>new</code>. Instead of using named
  parameters, you can also give <code>(rxReplace)</code> a Map object containing
  the strings that need to be replaced. 
  </p>
  
  <p>
    <code>match</code> is a regular expression.
  </p>

  <?= formatCode("println 'the quick brown fox'.
  (rxReplace 'quick':'slow' 'brown':'fire');") ?>
  
  <?= formatOutput("the slow fire fox") ?>
  
  <br/>

<? displayFunctionHead('rxSplit', array('separator')) ?>

  <p>
    The <code>(rxSplit)</code> function explodes a string into parts divided by the separator argument.
    The result is a list.
  </p>

  <p>
    <code>separator</code> is a regular expression.
  </p>

  <?= formatCode("println 'a b c d'.(rxSplit ' ');") ?>
  
  <?= formatOutput("(list 'a' 'b' 'c' 'd')") ?>
  
  <br/>


<? displayFunctionHead('replace', array('match:new')) ?>

  <p>
  The <code>(replace)</code> function returns a string where all occurrences of
  <code>match</code> have been replaced with <code>new</code>. Instead of using named
  parameters, you can also give <code>(replace)</code> a Map object containing
  the strings that need to be replaced. 
  </p>
  
  <?= formatCode("println 'the quick brown fox'.
  (replace 'quick':'slow' 'brown':'fire');") ?>
  
  <?= formatOutput("the slow fire fox") ?>
  
  <br/>


<? displayFunctionHead('split', array('separator')) ?>

  <p>
    The <code>(split)</code> function explodes a string into parts divided by the separator argument.
    The result is a list.
  </p>

  <?= formatCode("println 'a b c d'.(split ' ');") ?>
  
  <?= formatOutput("(list 'a' 'b' 'c' 'd')") ?>
  
  <br/>


<? displayFunctionHead('startsWith', array('match')) ?>

  <p>
  Returns true if the string starts with the match string.
  </p>

  <?= formatCode("println 'a b c d'.(startsWith 'a');") ?>
  
  <?= formatOutput("true") ?>
  
  <br/>


<? displayFunctionHead('trim', array('')) ?>

  <p>
  The <code>(trim)</code> removes any whitespace characters from the beginning and end of the string 
  and returns that new string.
  </p>

  <?= formatCode("println '---' ' cat-dog   '.(trim) '---';") ?>
  
  <?= formatOutput("---cat-dog---") ?>
  
  <br/>


<? displayFunctionHead('upper', array('')) ?>

  <p>
  Returns an upper case version of the string.
  </p>

  <?= formatCode("println 'the quick brown fox'.(upper);") ?>
  
  <?= formatOutput("THE QUICK BROWN FOX") ?>
  
  <br/>

