<?

$GLOBALS['title'] = 'Differences to Lua';

?>

<p>
  Since np is based off the Lua runtime, there are
  more than a couple of similarities. Despite the syntactical
  differences, np is clearly a Lua dialect. However, there
  are some significant differences:  
</p>

<h3>Optional Semicolons and Commas</h3>
<p>
  Statements in np do not need to be separated by semicolons.
  Commas are also optional in function parameter lists, as well
  as list literals. However, commas are still needed to return
  and receive multiple parameters.
</p>

<h3>Deleted Stuff</h3>
<p>
  np does not support the <code>goto</code> statement or code labels.
  String literals can have line breaks in them. Language keywords
  are allowed when using the dot notation to address list keys (like myList.new = 1).
  The unary object length operator has been removed, as has the <code>repeat...until</code>
  statement.
</p>

<h3>Metatables and Metamethods</h3>
<p>
  The entirety of an object's behavior is stored in its metatable -
  which I have renamed to "event lists" to make the distinction clearer.
  In conjunction with that, the <code>self</code> operator : is used
  in np to invoke functions from the event list, operating on the data
  object. The types number, string, and list are automatically instantiated
  with the corresponding library functions as their event list, so for instance
  <code>myList:copy()</code> will invoke the copy() function from the list
  library to work on the myList object. Lua-style system metamethods, such as
  for example __index, have been renamed to omit the leading underscores and
  be generally more legible.
</p>

<h3>Standard Libraries</h3>
<p>
  The standard Lua libraries have been extended with a lot of additional
  functionality.
</p>

<h3>Mutable Operations</h3>
<p>
  np sports compound assignments like <code>a+=1</code>. To better support that functionality,
  many library functions now also support both mutable and immutable operations.
</p>

<h3>Loading Modules</h3>
<p>
  With the __file__ and the __dir__ keywords, source code files are now
  aware of their own filename and path, and can use this to make better
  decisions about loading other code from the filesystem.
</p>

<h3>Local by Default</h3>
<p>
  All variables are local and have to be declared before use with the <code>new</code>
  keyword which replaces Lua's <code>local</code> statement. The current module's
  root context can be accessed and modified by addressing the <code>root</code>
  container object only.
</p>

<h3>Functions</h3>
<p>
  There is only one syntax for declaring functions now: <code>new f = { a | => a*2 }</code>.
  This means function declarations are essentially normal variable assignments using
  function literals. To help with recursion, the <code>this</code> keyword was intrduced,
  which always refers to the current function.
</p>

<h3>Iterators</h3>

<p>
  Lua-style iterators are deprecated and may be removed in the future. In their place,
  people should use the functional iterator pattern as exemplified in the new standard
  libraries. The np iterator pattern is simpler to code and easier to read, and it
  requires no syntactical sugar.
  For an example, refer to the <code>items()</code> iterator function of the 
  list library.
</p>

<h3>Block Statements</h3>

<p>
  In Lua, you often have to use block statements such as <code>do..end</code> or
  <code>then..end</code> even if the block contains only a single statement.
  In np, there is no need to use code blocks in these cases.
</p>

