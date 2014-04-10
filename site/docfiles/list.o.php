<?php

  $info = array(
    'title' => 'List',
    'type' => 'object',
    'tags' => array('prototype', 'list'),
    'params' => array(
      ),
    'nparams' => array(
      ),
    'see' => array('map.o', 'list')
    );

?>
<h2>Description</h2>  

<p>
  <code>List</code> is the prototype object for all lists.
</p>

<h2>List Object Members</h2>

<? displayFunctionHead('add', array('object')) ?>

  <p>
  Appends an object to the list.
  </p>

  <?= formatCode("l = (list);
l.add 1337;
println l;") ?>
  
  <?= formatOutput("(list '1337')") ?>
  
  <br/>

<? displayFunctionHead('count', array()) ?>

  <p>
  Counts the number of elements in the list.
  </p>

  <?= formatCode("println (list 1 2 3 4).(count);") ?>
  
  <?= formatOutput("4") ?>
  
  <br/>

<? displayFunctionHead('each', array('{ object index | … }')) ?>

  <p>
  <code>each</code> iterates over all of the elements within the list, yielding both the 
  object itself and its index.
  </p>

  <?= formatCode("(list #a #b #c).each { obj | println obj };") ?>
  
  <?= formatOutput("a
b
c") ?>
  
  <br/>

<? displayFunctionHead('insert', array('index', 'object')) ?>

  <p>
  Inserts an element before the specified index into the list.
  </p>

  <?= formatCode("myList = (list #a #c);
myList.insert 1 #b;
println myList;") ?>
  
  <?= formatOutput("(list 'a' 'b' 'c')") ?>
  
  <br/>

<? displayFunctionHead('item', array('index')) ?>

  <p>
  Returns the item at the specified index.
  </p>

  <?= formatCode("println (list 1 2 3).(item 0);") ?>
  
  <?= formatOutput("1") ?>
  
  <br/>

<? displayFunctionHead('popFirst', array()) ?>

  <p>
  Removes and returns the first element of the list.
  </p>

  <?= formatCode("println (list 'cat' 'dog' 'turtle').(popFirst);") ?>
  
  <?= formatOutput("cat") ?>
  
  <br/>

<? displayFunctionHead('popList', array()) ?>

  <p>
  Removes and returns the last element of the list.
  </p>

  <?= formatCode("println (list 'cat' 'dog' 'turtle').(popLast);") ?>
  
  <?= formatOutput("turtle") ?>
  
  <br/>

<? displayFunctionHead('remove', array('index')) ?>

  <p>
  Removes and returns a specified element of the list.
  </p>

  <?= formatCode("println (list 'cat' 'dog' 'turtle').(remove 1);") ?>
  
  <?= formatOutput("dog") ?>
  
  <br/>

<? displayFunctionHead('removeObject', array('object')) ?>

  <p>
  Removes a specified object from the list.
  </p>

  <?= formatCode("obj = 'cat';
l = (list obj);
println l;
l.removeObject obj;
println l;") ?>
  
  <?= formatOutput("(list 'cat')
(list )") ?>
  
  <br/>

<? displayFunctionHead('set', array('index object')) ?>

  <p>
  Places an object at the specified index (replacing the old entry).
  </p>

  <?= formatCode("l = (list 1 2 3);
l.set 1 1337;
println l;") ?>
  
  <?= formatOutput("(list '1' '1337' '3')") ?>
  
  <br/>


<? displayFunctionHead('sort', array("#mode:m", "#dir:d")) ?>

  <p>
  Sorts the array based on the immediate values of the objects contained within it.
  Every object is interpreted as either a number (#mode:#numeric) or a string (#mode:#alpha)
  and the sorting direction can either be ascending (#dir:#asc) or descending (#dir:#desc).
  </p>

  <?= formatCode("l = (list 4 3 7 2).(sort);
println l;") ?>
  
  <?= formatOutput("(list '2' '3' '4' '7')") ?>
  
  <br/>

