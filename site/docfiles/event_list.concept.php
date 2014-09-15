<?php

  $info = array(
    'title' => 'event list (under construction)',
    'type' => 'concept',
    'tags' => array(),
    'params' => array(
      ),
    'nparams' => array(
      ),
    'see' => array('list.lib', 'list.type')
    );

?>
<h2>Description</h2>  

<p>
  There are no classes in np. Instead, the behavior of objects is defined by their
  event lists. Event lists are ordinary lists that contain functions which respond
  to built-in and custom events. Event lists can also contain metadata, although
  you have to keep in mind that the same data will be bound to all objects that use
  the same event list. Since event lists can be modified, copied, and mixed together,
  they provide a flexible way to implement the behavior of objects.
</p>

<p>
  Only <code>list</code> objects can have custom event lists. Other types, such as
  <code>strings</code> and <code>numbers</code> each have a fixed, type-specific
  event list built in. While it is possible to modify these built-in event lists,
  the resulting modifications will automatically apply to all objects of the same
  type within the current module. 
</p>

<h2>Standard Events</h2>

<p>
  Standard events are called by the np runtime whenever a specific operation
  occurs that involves the list object. The following are standard events:
</p>

<? displayFunctionHead(':add', array(), array()) ?>

<p>

</p>

<?= formatCode("") ?>

<?= formatOutput('') ?>

<br/>

<? displayFunctionHead(':call', array('listObject', '[...]'), array()) ?>

<p>
  Objects that have a <code>call</code> function in their event list
  can be called like functions themselves. Any parameters passed when calling the
  object are passed along as function parameters to the <code>call</code> function.
</p>

<?= formatCode("new callableList = (: 1 2 3 )
-- binding the call event
callableList:bind(list << 
  (: call = { l | => size(l) } ))
-- calling the list as if it were a function
print(callableList())
") ?>

<?= formatOutput('3') ?>

<br/>

<? displayFunctionHead(':concat', array(), array()) ?>

<p>

</p>

<?= formatCode("") ?>

<?= formatOutput('') ?>

<br/>

<? displayFunctionHead(':divide', array(), array()) ?>

<p>

</p>

<?= formatCode("") ?>

<?= formatOutput('') ?>

<br/>

<? displayFunctionHead(':equal', array(), array()) ?>

<p>

</p>

<?= formatCode("") ?>

<?= formatOutput('') ?>

<br/>

<? displayFunctionHead(':event', array(), array()) ?>

<p>
  With <code>event</code>, an object can respond to arbitrary event calls.
  If you provide a function as a handler, that function will be
  executed whenever an event is called that has no defined handler.
  The first parameter this function receives will be a list containing
  the entries <code>list</code> (= the object the event was called on)
  and <code>function</code> (= the name of the event that was called):
</p>

<?= formatCode("
-- make list with generic event handler
new l = list.create(: 
  event = { info param | 
    print(info.function << '(' << param << ') was called') }
  )
-- try it out with an arbitrary function name
l:something(#bla)") ?>

<?= formatOutput('something(1) was called') ?>

<p>
  Instead of providing a handler function, you can also provide
  another event list in its place:
</p>

<?= formatCode("
new l = list.create(: 
  event = (:
    something = { lst v1 | => 'yay for something(' << v1 << ')!' }
    )
  )
print(l:something(#foo))") ?>

<?= formatOutput('yay for something(foo)!') ?>

<br/>

<? displayFunctionHead(':iAdd', array(), array()) ?>

<p>

</p>

<?= formatCode("") ?>

<?= formatOutput('') ?>

<br/>

<? displayFunctionHead('iConcat', array(), array()) ?>

<p>

</p>

<?= formatCode("") ?>

<?= formatOutput('') ?>

<br/>

<? displayFunctionHead(':index', array(), array()) ?>

<p>

</p>

<?= formatCode("") ?>

<?= formatOutput('') ?>

<br/>

<? displayFunctionHead(':iSubtract', array(), array()) ?>

<p>

</p>

<?= formatCode("") ?>

<?= formatOutput('') ?>

<br/>

<? displayFunctionHead(':lessOrEqual', array(), array()) ?>

<p>

</p>

<?= formatCode("") ?>

<?= formatOutput('') ?>

<br/>

<? displayFunctionHead(':lessThan', array(), array()) ?>

<p>

</p>

<?= formatCode("") ?>

<?= formatOutput('') ?>

<br/>

<? displayFunctionHead(':modulo', array(), array()) ?>

<p>

</p>

<?= formatCode("") ?>

<?= formatOutput('') ?>

<br/>

<? displayFunctionHead(':multiply', array(), array()) ?>

<p>

</p>

<?= formatCode("") ?>

<?= formatOutput('') ?>

<br/>

<? displayFunctionHead(':power', array(), array()) ?>

<p>

</p>

<?= formatCode("") ?>

<?= formatOutput('') ?>

<br/>

<? displayFunctionHead(':newIndex', array(), array()) ?>

<p>

</p>

<?= formatCode("") ?>

<?= formatOutput('') ?>

<br/>

<? displayFunctionHead(':size', array(), array()) ?>

<p>

</p>

<?= formatCode("") ?>

<?= formatOutput('') ?>

<br/>

<? displayFunctionHead(':subtract', array(), array()) ?>

<p>

</p>

<?= formatCode("") ?>

<?= formatOutput('') ?>

<br/>

<? displayFunctionHead(':toString', array(), array()) ?>

<p>

</p>

<?= formatCode("") ?>

<?= formatOutput('') ?>

<br/>

<? displayFunctionHead(':minus', array(), array()) ?>

<p>

</p>

<?= formatCode("") ?>

<?= formatOutput('') ?>

<br/>

<? displayFunctionHead(':update', array(), array()) ?>

<p>

</p>

<?= formatCode("") ?>

<?= formatOutput('') ?>

<br/>

<? displayFunctionHead('.weak', array(), array()) ?>

<p>

</p>

<?= formatCode("") ?>

<?= formatOutput('') ?>

<br/>

