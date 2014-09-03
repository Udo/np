<?php

  $info = array(
    'title' => 'list',
    'type' => 'type',
    'tags' => array(),
    'params' => array(
      ),
    'nparams' => array(
      ),
    'see' => array('list.lib')
    );

?>
<h2>Description</h2>  

<p>
  Lists are one of the basic types of np, together with numbers, booleans, nil, and strings.
  Lists can contain two types of entries, ordered items (in an array-like fashion) and key-value
  pairs (like a hash map). A list can have either or both of these types of entries.
</p>

<h2>List Literals</h2>

<p>
  A list can be created like this:
</p>

<?= formatCode("-- an empty list
new list1 = (:)
-- a list with ordered items
new list2 = (: 4 8 15 16 23 'and' 42)
-- a list with key-value pairs
new list3 = (: someKey = 'someValue' )
-- both:
new list4 = (: 2 3 5 7 11 someThing = 'blah' )
") ?>

<h2>Using Lists</h2>

<p>
  Accessing list members can be done by using the square bracket notation or, if 
  referring to a simple alphanumerical key-value entry, with the dot notation: 
</p>

<?= formatCode("new t = (: foo = 'bar' )
t[10] = 'baz'
print('foo is:' t.foo)
print('at position 10:' t[10])

-- list items are counted starting from 1
new t2 = ( 'a' 'b' 'c' )
print(t2[1])") ?>
<?= formatOutput('a') ?>

<h2>Usage as an Array</h2>

<p>
  If used as an array, lists are ordered arrays of items without explicit names.
  Their numbering comes from the order in which they are stored:
</p>

<?= formatCode("new l = (: 678 4237 33 'abc') ") ?>

<p>
  Array items can be iterated through with the <code>items</code> iterator:
</p>

<?= formatCode("l:each{ v | print(v) }") ?>
<?= formatOutput("678
4237
33
abc") ?>

<p>
  You can pop off entries of the array at arbitrary positions with the <code>remove()</code>
  behavior:
</p>

<?= formatCode("print('removed 1st item:' l:remove(1))
print('rest of l:' l)") ?>
<?= formatOutput("removed 1st item:	678
rest of l:	(: 4237 33 \"abc\" )") ?>

<p>
  And you can <code>add()</code> or <code>mAdd()</code> (=mutable add) items: 
</p>

<?= formatCode("print(l:mAdd([[I'm new]]))
-- or, do it with an operator:
l += [[also, this]]
print(l)") ?>
<?= formatOutput('(: 4237 33 "abc" "I\'m new" )
(: 4237 33 "abc" "I\'m new" "also, this" )') ?>

<h2>Usage as a Hash Table</h2>

<p>
  Lists are also hash tables, when entries get assigned an explicit key. However, these
  key-value entries are not stored in a particular order:
</p>

<?= formatCode("new l = (: a='foo' b='bar' d='baz' )
print(l)") ?>
<?= formatOutput('(: d="baz" a="foo" b="bar" )') ?>

<p>
  To remove a key-value entry from a list, set its value to <code>nil</code>:
</p>

<?= formatCode("l.a = nil
print('l without a:' l)") ?>
<?= formatOutput('l without a:	(: b="bar" d="baz" )') ?>

<p>
  Similarly, you can add entries to the key-value store of a list by simply 
  providing values for them:
</p>

<?= formatCode("l.c = [[new entry!]]
print(l)") ?>
<?= formatOutput('(: c="new entry!" d="baz" b="bar" )') ?>

<h3>The Meaning of <code>nil</code></h3>

<p>
  If an entry is referred to but doesn't exist, <code>nil</code> is returned:
</p>

<?= formatCode("new t = (: foo = 'bar' )
print(t.baz)") ?>
<?= formatOutput("nil") ?>

<p>
  You can also set the value of a keyed entry to <code>nil</code> in order to erase it:
</p>

<?= formatCode("new t = (: foo = 'bar' )
print('t:' t) 
t.foo = nil
print('t:' t)") ?>
<?= formatOutput('t:	(: foo="bar" )
t:	(: )') ?>

<h2>(Im)Mutability</h2>

<p>
  Lists can be modified after they have been created. Some list operations are mutable,
  and some are immutable in the sense that a new list is returned after the operation.
  The <code>mAdd()</code> and <code>mConcat()</code> behaviors modify the list in place
  by adding a new entry or appending another list respectively - whereas their
  immutable counterparts <code>add()</code> and <code>concat()</code> leave the original
  list untouched.
</p>





