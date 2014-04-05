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
new myNumber = 2001
new myString = 'this is a string'
new myBool = true
new myTable = (: 1 2 3 4 5 option = 'hello')

-- multiple return values from a function call
result, error = doSomething()
```

### Function Declarations and Usage

```Lua
-- declaring a function
new myFunc = {(argument1) print(argument1) }

-- declaring a function with named parameters
new myFuncNamed = {(n) print(n[1] n[2] n.out) }

-- calling a function with named parameters 
myFuncNamed(: 'param1' 'param2' out = 'hello world!' )
```

Remark: like the whole thing, this is a work in progress. I'm not happy with the idea that there are two ways to write and use a function. Ideally, I'd use the old np pattern of tacking on named parameters like this: `myFunc('param1' 'param2' | out = 'hello world' )`. However, that requires injecting the named parameter list into the function environment - something I have not figured out a simple solution for with the Lua codebase (if it was simple, it seems the Lua designers would have done it already instead of going with that ugly :/self hack).

#### Return 

Functions can use the `return` statement at any time (not just at the end of the function body). 

```Lua
new fooFunc = {(bar)
	if bar > 0 {
		return('greater than zero')
	else bar < 0
	  return('less than zero')
	else
	  return('equal to zero')
	}
	return(bar)
}
```

The `return` statement is function-like, meaning its arguments have to be enclosed in parens.

#### Multiple Return Values

You can return multiple values from a function. If the receiving end of those values contains less variables, the extra values are discarded. On the receiving side, multiple variables must be separated by commata.

```Lua
-- declaring the function
new switchFunc = {(a b) return(b a) }

new a = 10
new b = 20

a, b = switchFunc(a b)
```

#### Scope

Variables are scoped to the block they're declared in.

```Lua
-- make a new variable a
new a = 5
print('just declared' a) -- 5

{
	print('inside the block' a) -- still 5
	new a = 10
	print('redeclared inside block' a) -- 10
}

print('after exiting the block' a) -- 5 again
```

### Event binding

As I'm letting go of the idea that everything needs to be a full object, I'm embracing and extending Lua's metatable idea instead - only by another name. Metamethods are now event handlers (and in some cases, event policies), because that's what they do. One thing I definitely wanted to avoid is the cross-polution of (potentially serializable) data and meta methods that plagues JavaScript. The initial np did this by providing a different scope operator when accessing hashmap data. A similar but cleaner idea comes in the form of the `events` binding in np:L. 

Any object can have an events table that specifies its behavior in certain situations. Right now, the following events are supported, most of them are from mainline Lua:

````
index    {(table key) ... }         is called when a non-existent entry is called
index    table2                     the entry is looked up in table2
newindex {(table key value) ...}    when a new entry is created
update   {(table key value) ...}    when an existing entry is updated
weak     'values', 'keys' or both   specifies weak referencing in values and/or keys
call     {(table parameter) ...}    allows the table name to be used like a function
events   value                      when events(table) is called, this value is returned
tostring {(table) ... }             overrides the default tostring(table) output
concat   {(operand1 operand2) ...}  concatenation

Arithmetics
unm      {(operand1) ...}           unary minus
add      {(operand1 operand2) ...}  addition
sub      {(operand1 operand2) ...}  subtraction
mul      {(operand1 operand2) ...}  multiplication
div      {(operand1 operand2) ...}  division
mod      {(operand1 operand2) ...}  modulo
pow      {(operand1 operand2) ...}  exponentiation

Logical Operations
eq       {(operand1 operand2) ...}  equality
lt       {(operand1 operand2) ...}  less than
gt       {(operand1 operand2) ...}  greater than
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
if a == 1 {
  doThis()
else a == 2
  doThat()
else
  doTheOther()
}
```

## Operators

Relational operators are: `<` (less than) `>` (greater than) `<=` (less-or-equal) `>=` (greater-or-equal) `==` (equal) `!=` (not equal)

Note that these operators do not cast variables into other types, so `'123' != 123`.

Logical operators are: `and` `or` `not`

Arithmetic operators are: `^` (to-the-power-of) `-` (unary sign) `*` (multiply) `/` (divide) `+` (add) `-` (subtract) 

There is an operator for string concatenation: `..`

## Reserved Keywords

The following word can't be used for variable names, since they are reserved keywords within the language.

```
break     - breaks out of the current block
else      - conditional statement that can follow if
for       - for loops
if        - conditional statement
(in)      - (will be removed)
return    - return value from a function
while     - while (condition) { ... } loop

new       - make new variable

nil       - null value
true      - boolean true
false     - boolean false

and       - boolean and
not       - boolean not
or        - boolean or
```

Keywords can, however, be used as table keys when using the dot notation, for example `myTable.if.foo` is valid.






