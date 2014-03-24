#np:L - Udo's Mod

Following the first iteration of np, I'm now experimenting a bit with the Lua 5.2 codebase - to see what np concepts (and other stuff that comes to mind) could be imported into that platform.

So to make it perfectly clear: this is a playground where I basically mutilate a beloved programming language, step by step turning it into a grotesque monster. This is not for the faint-hearted.

## Deviations from Lua

Any and all of these are probably horrible, broken, and horribly broken.

### Event binding

`setmetatable` and `getmetatable` are now bundled in the function `events(table [, metatable])` which I believe describes better what it does. Also, the metamethod naming convention has changed to leave out the leading double underscore. The following methods are supported:

````
index, newindex, update, gc, mode, len, eq, add, sub, 
mul, div, mod, pow, unm, lt, le, concat, call
````

Consider this example where the `index` and `update` events are bound to a table:

````
events(someTable, (: 
	update = {(t key val) print('---update of key'  key val) }
	index = {(t key) print('---index of key'  key ) }
  ))
````

### Functions are {()}

Functions look a lot like blocks now, except for the parameter header: `new myFunc = {(a) ... }`

### Curly braces

The do...end, if...then...end constructs have been replaced with curly-braced alternatives. For example, the while loop now reads `while(true){ doSomething() }` and there have been multiple changes to if...then:

````
if(...) {
	doThis()
cond(...)
	doThat()
else
  doTheOther()
}
````

Even though the use of `cond` (which replaces `elseif`) and `else` are not entirely consistent with the traditional curly brace paradigm, this pattern reduces the amount of noise needed to make the entire expression.

### So: do we really this many commas?

Commata are now optional in tables. Table constructors now look like this: `new sparseTable = (: 1 2 3 bla = 'blurp)`, function declarations: `function (a b c) ... end`, and function calls: `myFunc(1 2 3)`. Commas are still needed in `for` loops, `return` statements, and multiple value returns.

### Table constructors without curly braces

For now, a table constructor looks like this: `new myTable = (: 1, 2, 3)`. This enables functions with named parameters to look like `myfunc(:2, bla = 123)` instead of the awkward `myfunc{2, bla = 123}`, and it also frees up curly braces syntactically. 

Ideally, I would like table constructors to work without the : at all, but I don't see how that could be done without a second lexer pass. That would also lead to ambiguity about what statements like `(1)` mean, but I guess we could live with that as long as we handle `()` correctly. Don't know about this yet...

The downside of doing named param functions like this at all is that developers have to choose between the "normal" convention and the table-based convention. The disadvantage of the current table-based method is also that the function's parameter declaration becomes almost meaningless. In a better world, I would like to be able to mix them: `myfunc(1, 2 | option = 'a')` so any named stuff is optional and comes after the pipe symbol. This would mean injecting a table into the local function state, maybe something named "options".

### Length operator "#" becomes base functions size() and asize()

For now, the length operator # still works, and will return the true size of the array. However, there is now the equivalent `size()` function. If you really want to count only the numerical keys in a table, there's now the `asize()` function for that. 

### pairs() iterator => each()

Again, for readability. I'm thinking about altering the syntax for invoking iterators altogether, maybe something like this:
``
with each(myTable) key,value do
...
end
`` instead of `for key,value in each(myTable) do ... end`

### Replaced the not-equal operator with !=

It's got better readability for pretty much everyone coming from any other language, so I replaced the ~= with !=. 

### "Short" strings can be multiline 

Strings starting with either of the quotation mark characters can now be multi-line.

### Table sizes are reported correctly

Table sizes reported by the # operator in Lua are weird. The # operator now reports the actual table size in entries, no matter which ones are keyed and in what order they are. 

### "Local" keyword replaced with "new"

This was done to emphasize the variable-declaration function of the keyword, to delineate the code visually from Lua code, and to hint at the fact that this should be the default way to make new variables. I'm considering throwing a warning every time a global variable is initiated.

