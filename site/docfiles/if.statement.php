<?php

  $info = array(
    'title' => 'if',
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
  <code>if</code> statements are used to express conditionality in code.
  If the given condition evaluates to <code>true</code>, the next statement
  or block gets executed. 
</p>

<h2>Simple If</h2>

<p>
  Here's how you can use the <code>if</code> statement in its shortest-possible form:
</p>
 
<?= formatCode("if 1==1 print('just checking!')") ?>

<p>
  The condition part can also be followed by a block of statements:
</p>

<?= formatCode("if 2==2 {
  print('alright, we need to talk about the creativity of these examples.')
  }") ?>

<p>
  You can use <code>elseif</code> statements to test for other conditions
  in case the previous ones did not come true:
</p>

<?= formatCode("new a = 10

if a==1 {
  print('one')
  }
elseif a==10 {
  print(10)
  }
else
  print('else')") ?>

