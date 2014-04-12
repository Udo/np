<?php

  $info = array(
    'title' => 'Map',
    'type' => 'object',
    'tags' => array('prototype', 'map'),
    'params' => array(
      ),
    'nparams' => array(
      ),
    'see' => array('list', 'map')
    );

?>
<h2>Description</h2>  

<p>
  The <code>Map</code> prototype defines what map objects can do in np.
</p>

<h2>Map Object Members</h2>

<? displayFunctionHead('count', array()) ?>

  <p>
  The <code>(count)</code> function returns the number of items contained in the map.
  </p>

  <?= formatCode("println (map 'a':1 'b':2 'c':3).(count);") ?>
  
  <?= formatOutput("3") ?>
  
  <br/>

<? displayFunctionHead('each', array('{ name value | … }')) ?>

  <p>
  <code>each</code> iterates over the entire map and calls the function supplied as an argument
  for each item.
  </p>
  
  <?= formatCode("(map 'a':1 'b':2 'c':3)
  .(each { key value | 
    println key '=' value });") ?>
  
  <?= formatOutput("b=2
c=3
a=1") ?>

  <p>
  Note that elements in a map are not necessarily ordered in any way.
  </p>

  <br/>

<? displayFunctionHead('item', array('name')) ?>

  <p>
  The <code>item</code> function accesses an item contained in the map by name.
  As such, it does the same thing as the double colon operator <code>::</code>
  which is a shortcut to <code>item</code> and <code>set</code> (depending on
  the context). 
  </p>
  
  <?= formatCode("print (map 'a':1 'b':2 'c':3).(item 'a');") ?>
  
  <?= formatOutput("1") ?>
  
  <br/>

<? displayFunctionHead('items', array()) ?>

  <p>
  The <code>(items)</code> function returns a list object with all the objects contained in this map.
  </p>

  <?= formatCode("println (map 'a':1 'b':2 'c':3).(items);") ?>
  
  <?= formatOutput("(list 'b' 'c' 'a')") ?>
  
  <br/>

<? displayFunctionHead('keys', array()) ?>

  <p>
  The <code>(items)</code> function returns a list object with all the key strings contained in this map.
  </p>

  <?= formatCode("println (map 'a':1 'b':2 'c':3).(keys);") ?>
  
  <?= formatOutput("(list 2 3 1)") ?>
  
  <br/>

<? displayFunctionHead('remove', array('name')) ?>

  <p>
  Removes an item from the map and returns the removed item object.
  </p>
  
  <?= formatCode("print (map 'a':1 'b':2 'c':3).(remove 'a');") ?>
  
  <?= formatOutput("1") ?>

  <br/>

<? displayFunctionHead('set', array('name', 'object')) ?>

  <p>
  The <code>set</code> function puts an object into the map, associating it with a name.
  If the map already contains an object under that name, it gets overwritten.
  </p>
  
  <?= formatCode("m = (map); 
m.set 'a' 123;
print m::'a';") ?>
  
  <?= formatOutput("123") ?>

  <br/>

