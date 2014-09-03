<?php

  $info = array(
    'title' => 'new',
    'type' => 'statement',
    'tags' => array(),
    'params' => array(
      ),
    'nparams' => array(
      ),
    );

?>
<h2>Description</h2>  

<p>
  <code>new</code> statements initialize new variables into the current context.
  You cannot use a variable that has not been declared previously.
</p>

<h2>Declaring Variables</h2>

<p>
  By using the <code>new</code> statement you create a new variable with a
  certain name. Variable names can be any combination of alphanumerical 
  characters, but it may not begin with a number. For improved readability, the
  underscore character <code>_</code> may also be used at any place in
  a variable name.
</p>
 
<?= formatCode("
-- declaring a new variable:
new myVar
-- declaring a variable and assigning a value to it right away:
new myNumber = 2
") ?>

<p>
  
</p>

<?= formatCode("
new fls = (:)

for a = 1,10,2
  fls += a
  
print(fls)
") ?>

<?= formatOutput('(: 1 3 5 7 9 )') ?>

<h2>Extracting from a List</h2>

<p>
  Optionally, you can extract a keyed entry from a list and
  use its value as a new local variable:
</p>

<?= formatCode("new myList = (: a=10 b=20 c=30)
new a, b, c in myList
print(a b c)") ?>

<?= formatOutput('10	20	30') ?>