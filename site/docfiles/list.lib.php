<?php

  $info = array(
    'title' => 'list',
    'type' => 'lib',
    'tags' => array(),
    'params' => array(
      ),
    'nparams' => array(
      ),
    'see' => array('list.type')
    );

?>
<h2>Description</h2>  

<p>
  The <code>list</code> library contains functions and behaviors that operate on lists.
</p>

<h2>Behaviors</h2>

<? displayFunctionHead(':add', array('value'), array('list')) ?>

  <p>
  Appends value to the list. This is equivalent to the in-place plus (+=) operator being used on a list.  
  </p>

  <?= formatCode("new l = (: 1 2)
print( l:add(3) )") ?>
  
  <?= formatOutput("(: 1 2 3 )") ?>
  
  <p>
    You can also add lists as items to other lists:   
  </p>

  <?= formatCode("new l = (: 1 2)
l:add(: 3 4)
print( l )") ?>
  
  <?= formatOutput("(: 1 2 (: 3 4 ) )") ?>
  
  <br/>

<? displayFunctionHead(':bind', array('[eventList]'), array('list')) ?>

  <p>
  Returns the event list for this list:
  </p>

  <?= formatCode("new l1 = (: 1 2 3)
new ListEvents = l1:bind()") ?>
  
  <p>
    If <code>eventList</code> is given, it is bound to the list as its new event list.   
  </p>

  <?= formatCode("new l1 = (: 1 2 3)
-- bind an event list, containing one function named 'first':
l1:bind(: first = { l | => l[1] } )
print(l1:first())") ?>
  
  <?= formatOutput("1") ?>
  
  <br/>

<? displayFunctionHead(':clear', array(), array('list')) ?>

  <p>
   Removes all items from the list before returning it. 
  </p>

  <?= formatCode("new l = (: 1 2)
l:clear()
print( l )") ?>
  
  <?= formatOutput("(: )") ?>
  
  <br/>

<? displayFunctionHead(':concat', array('list2'), array('list')) ?>

  <p>
  Appends the entries of <code>list2</code> to the list. This is equivalent to the in-place concatenation operator being used on a list.
  </p>

  <?= formatCode("new l = (: 1 2)
print( l:concat(:3 4) )") ?>
  
  <?= formatOutput("(: 1 2 3 4 )") ?>
  
  <br/>

<? displayFunctionHead(':copy', array(), array('list')) ?>

  <p>
  Returns a shallow copy of the list.
  </p>

  <?= formatCode("new l1 = (: 100 200)
new l2 = l1:copy()
l2 += 300
print('l1:' l1)
print('l2:' l2)") ?>
  
  <?= formatOutput("l1:	(: 100 200 )
l2:	(: 100 200 300 )") ?>
  
  <br/>

<? displayFunctionHead(':create', array('[dataList]'), array('list')) ?>

  <p>
  Creates a new empty list, and binds the current list as its event list. 
  If dataList is given, a copy of it will be used as a data source for the new list. 
  </p>

  <?= formatCode("-- make an event list that inherits from the generic list events
new SayListBehavior = list << (: 
  say = { l | print('I say: ' l:toString()) } 
  ) 
-- make a list bound to this behavior
new myList = SayListBehavior:create(: 1 2 3)
myList:say() ") ?>

  <?= formatOutput('I say: 	(: 1 2 3 )') ?>
  
  <br/>

<? displayFunctionHead(':each', array('ƒ⇠item, key'), array('list')) ?>

  <p>
  Calls ƒ on every value in the list.
  </p>

  <?= formatCode("new l1 = (: 100 200 myKey = 300)
l1:each{ v k | print(k '=' v) }") ?>
  
  <?= formatOutput("1	=	100
2	=	200
myKey	=	300") ?>
  
  <br/>

<? displayFunctionHead(':expand', array(), array('tuple')) ?>

  <p>
  Expands a list into a tuple / multiple return value.
  </p>

  <?= formatCode("new l1 = (: 1 2 3)
new f = { a b c | print('a:' a 'b:' b 'c:' c) }
f( l1:expand() )") ?>
  
  <?= formatOutput("a:	1	b:	2	c:	3") ?>
  
  <br/>

<? displayFunctionHead(':find', array('searchValue or: ⇠ƒ⇠item,key'), array('value')) ?>

  <p>
  Finds an entry for searchValue in the list and returns its key if it was found, returns nil if no match was found. 
  </p>

  <?= formatCode("new l = (: 10 20 300)
print( l:find(20) )") ?>
  
  <?= formatOutput("2") ?>
  
  <p>
  You can use a function ƒ in place of searchValue to provide your own matching code. :find(ƒ) will then return the first non-nil value returned by ƒ: 
  </p>

  <?= formatCode("new l = (: 10 20 300)
print(l:find{ v | if(v == 20) => 'found it, yeah' })") ?>
  
  <?= formatOutput("found it, yeah") ?>
  
  <br/>

<? displayFunctionHead(':iAdd', array('value'), array('list')) ?>

  <p>
  Returns a new list with the value appended to it. This is equivalent to the plus (+) operator being used on a list. 
  </p>

  <?= formatCode("new l = (: 1 2)
print( l:iAdd(3) )") ?>
  
  <?= formatOutput("(: 1 2 3 )") ?>
  
  <br/>

<? displayFunctionHead(':iConcat', array('list2'), array('list')) ?>

  <p>
  Returns a new list that contains the entries of both original lists. This is equivalent to the concatenation operator being used on a list. The mutable version of :iConcat() is called :concat().  
  </p>

  <?= formatCode("new l = (: 1 2)
print( l:iConcat(:3 4) )") ?>
  
  <?= formatOutput("(: 1 2 3 4 )") ?>
  
  <br/>

<? displayFunctionHead(':insert', array('position', 'value'), array('list')) ?>

  <p>
  Mutably inserts the <code>value</code> at <code>position</code> into the list. Position counting starts from 1.
  A position of 0 means the value gets appended to the end of the list. Negative values count backwards
  from the end of the list.
  </p>

  <?= formatCode("new l1 = (: 10 20 30)
l1:insert(-1 25)
print(l1)") ?>
  
  <?= formatOutput("(: 10 20 25 30 )") ?>
  
  <br/>

<? displayFunctionHead(':iReverse', array(), array('list')) ?>

  <p>
   Returns a copy of the list with the elements reversed. 
  </p>

  <?= formatCode("new l = (: 10 20 300)
print(l:iReverse())") ?>
  
  <?= formatOutput("(: 300 20 10 )") ?>
  
  <br/>

<? displayFunctionHead(':iSort', array('sortMode or: ƒ⇠val1, val2'), array('list')) ?>

  <p>
   Sorts a copy of the list and returns it. list items are compared to each other according to the chosen sortMode, or a custom comparison function ƒ. Valid sort modes are: number (items are interpreted as numerical), string (items are compared as strings), or strict (all items have to be of the same type).  
  </p>

  <?= formatCode("new l = (: 10 2 3 )
print( l:iSort('number') )
print( l:iSort('string') )") ?>
  
  <?= formatOutput('(: 2 3 10 )
(: "10" "2" "3" )') ?>
  
  <br/>

<? displayFunctionHead(':items', array('ƒ⇠item'), array('list')) ?>

  <p>
  Calls ƒ on every ordered (non-keyed) item in the list.
  </p>

  <?= formatCode("new l = (: 1 2 myKey=3)
l:items{ v | print(v) }") ?>
  
  <?= formatOutput("1
2") ?>
  
  <br/>

<? displayFunctionHead(':join', array('seperator'), array('string')) ?>

  <p>
  Returns a string where all ordered items of the list have been joined together,
  using the <code>seperator</code> in between them.
  </p>

  <?= formatCode("new l = (: 1 2 myKey=3)
print( l:join(', ') )") ?>
  
  <?= formatOutput("1, 2") ?>
  
  <br/>

<? displayFunctionHead(':map', array('ƒ⇠item, key'), array('list')) ?>

  <p>
  Calls ƒ on every entry in the list, adding the return values of ƒ into a new list, which is then returned.
  Whenever ƒ returns nothing, no entry is added to the new list. If two values are returned, the first one
  is assumed to be the value and the second one the key. 
  </p>

  <?= formatCode("new l = (: 1 2 myKey=3)
print(l:map{ v k | => v+1,k })") ?>
  
  <?= formatOutput("(: 2 3 myKey=4 )") ?>
  
  <br/>
  
<? displayFunctionHead(':pop', array('index'), array('list')) ?>

  <p>
  Removes the item at the position <code>index</code> from the list. Positions start with index 1. 
  Values less than zero can be used to address positions counting backward from the end of the list.
  </p>

  <?= formatCode("new l = (: 1 2 3 )
print(l:pop(-1))
print(l)") ?>
  
  <?= formatOutput("3
(: 1 2 )") ?>
  
  <br/>

<? displayFunctionHead(':reduce', array('ƒ⇠item, value'), array('value')) ?>

  <p>
  Calls ƒ on every <code>item</code> of the list. The return value of the previous call is passed as the <code>value</code> parameter.
  The return value of the last call in the chain is also the return value of the <code>:reduce()</code> call.
  </p>

  <?= formatCode("new l = (: 1 2 3)
-- using reduce to sum all the items in the list
print( l:reduce{ itm val | => itm+val } )") ?>
  
  <?= formatOutput("6") ?>
  
  <br/>

<? displayFunctionHead(':reverse', array(), array('value')) ?>

  <p>
   Reverses the order of all non-keyed list items. 
  </p>

  <?= formatCode("new l = (: 1 2 3 )
l:reverse()
print(l)") ?>
  
  <?= formatOutput("(: 3 2 1 )") ?>
  
  <br/>

<? displayFunctionHead(':size', array(), array('value')) ?>

  <p>
    Returns the size of the list.   
  </p>

  <?= formatCode("new l = (: 1 2 3 namedEntry = 100 )
print(#items l:size())
print(#named l:keyCount())
") ?>
  
  <?= formatOutput("items	3
named	1") ?>
  
  <br/>

<? displayFunctionHead(':sort', array('sortMode | ƒ⇠val1, val2'), array('list')) ?>

  <p>
  Sorts the list, where list items are compared to each other according to the chosen <code>sortMode</code>,
  or a custom comparison function ƒ. Valid sort modes are: number (items are interpreted as numerical), string (items
  are compared as strings), or strict (all items have to be of the same type).
  </p>

  <?= formatCode("new l = (: 10 2 3 )
print( l:sort('number') )
print( l:sort('string') )") ?>
  
  <?= formatOutput('(: 2 3 10 )
(: "10" "2" "3" )') ?>
  
  <br/>

<? displayFunctionHead(':toString', array(), array('string')) ?>

  <p>
    Returns a string representation of the list. However, this is not intended as a serialization function.
  </p>

  <?= formatCode("new l = (: 10 2 3 )
print( l:toString() )") ?>
  
  <?= formatOutput('(: 10 2 3)') ?>
  
  <br/>




<!---------------------------------------------------------------->
<h2>Library Functions</h2>

<? displayFunctionHead('list.condense', array('value1', '[value2]', '[...]'), array('list')) ?>

  <p>
  Condenses the parameters passed to the function into a list. This works with the variable argument list operator (...) as well.
  </p>

  <?= formatCode("new f = { ... | print( list.condense(...) ) }
f( 10 20 30 )") ?>
  
  <?= formatOutput("(: 10 20 30 n=3 )") ?>
  
  <br/>


