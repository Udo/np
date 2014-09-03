<?php

  $info = array(
    'title' => 'for',
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
  <code>for</code> statements are used to loop over a sequence.
</p>

<h2>Numeric for</h2>

<p>
  The simplest numeric for counts a sequence in order:
</p>
 
<?= formatCode("
new c = 1
for i = 1, 1000 
  c += i
print( c )") ?>

<p>
  An optional third parameter can indicate the step size:
</p>

<?= formatCode("
new fls = (:)

for a = 1,10,2
  fls += a
  
print(fls)
") ?>

<?= formatOutput('(: 1 3 5 7 9 )') ?>
