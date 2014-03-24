#np:L - Udo's Mod

Following the first iteration of np, I'm now experimenting a bit with the Lua 5.2 codebase - to see what np concepts (and other stuff that comes to mind) could be imported into that platform. I made this decision after thinking about a pure C port of the original np proof-of-concept, but why start from nothing if you can start from an excellent and time-proven codebase?

So to make it perfectly clear: this is a playground where I basically mutilate a beloved programming language, step by step turning it into a grotesque monster. This is not for the faint-hearted.

## Deviations from Lua

There are several objectives I want to reach that require a somewhat heavy modification of the Lua base, to a point where the language can't be recognized anymore. However, as I move some of the original (Java) np ideas into the interpreter, I'm also keeping some of the excellent Lua ideas around, hopefully to construct a beautiful chimera... it's alive... mwahaha!

## General syntax

np:L is a list-based, curly-braced, lexically-scoped, garbage-collected, interpreted language with dynamic typing. White space does not have significant meaning. Semicolons and commas are used to delimit statements and list entries respectively, but can be omitted in cases where it's semantically unambiguous.

### Variables, Statements and Assignments

```Lua
-- declaring and using a variable
new myVar = 2001
new myTable = (: 1 2 3 4 5 option = 'hello')

-- multiple return values from a function call
result, error = doSomething()
```

### Function Declarations and Usage

```Lua
-- declaring a function
new myFunc = {(argument1) print(argument1) }

-- declaring a function with named parameters
new myFuncNamed = {(n) print(n.out) }

-- calling a function with named parameters 
myFuncNamed(: 'param1' 'param2' out = 'hello world!' )
```

Remark: like the whole thing, this is a work in progress. I'm not happy with the idea that there are two ways to write and use a function. Ideally, I'd use the old np pattern of tacking on named parameters like this: `myFunc('param1' 'param2' | out = 'hello world' )`. However, that requires injecting the named parameter list into the function environment - something I have not figured out a simple solution for with the Lua codebase (if it was simple, it seems the Lua designers would have done it already instead of going with that ugly :/self hack).

### Event binding

As I'm letting go of the idea that everything needs to be a full object, I'm embracing and extending Lua's metatable idea instead. One thing I definitely wanted to avoid is the cross-polution of (potentially serializable) data and meta methods that plagues JavaScript. The initial np did this by providing a different scope operator when accessing hashmap data. A similar but cleaner idea comes in the form of the `events` binding in np:L. 

Any object can have an events table that specifies its behavior in certain situations. Right now, the following events are supported, most of them are from mainline Lua:

````
index, newindex, update, gc, mode, len, eq, add, sub, 
mul, div, mod, pow, unm, lt, le, concat, call
````

Consider this example where the `index` and `update` events are bound to a table:

```Lua
events(someTable, (: 
	update = {(t key val) print('---update of key'  key val) }
	index = {(t key) print('---index of key'  key ) }
  ))
```

### Conditional Statements

The `if` statement works a lot like you'd expect, with the little caveat that the `cond` (analogous to "else if() ...") and `else` statements do not get their own curly-braced block. Instead, the whole conditional is contained within one block:

```Lua
if(...) {
	doThis()
cond(...)
	doThat()
else
  doTheOther()
}
```

### Operators





