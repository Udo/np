<?php

  $info = array(
    'title' => 'function',
    'type' => 'type',
    'tags' => array(),
    'params' => array(
      ),
    'nparams' => array(
      ),
    );

?>
<h2>Description</h2>  

<p>
  Functions are a basic types of np. They are blocks of code that can take
  data in and pass data back out. Contrary to normal code blocks, functions
  can be assigned to variables.
</p>

<h2>Function Literals</h2>

<p>
  This is how you make a new function and assign it to a variable:
</p>

<?= formatCode("-- define a function f that returns its input plus one
new f = { a | => a+1 }
print( f(1) )") ?>

<h2>Calling Functions</h2>

<p>
  Since functions are just values, there is a special syntax that
  tells np to execute a function: it consists of the variable
  containing the function followed immediately by a parenthesis.
  No whitespace is allowed between the function name and the
  opening parenthesis.
</p>

<?= formatCode("-- correct: 
f( 123 )
-- wrong:
f ( 123 )") ?>

<p>
  np will generally throw a warning if you try to do this wrong, except
  in cases where it looks correct (for example in lists). 
  This is necessary to keep the syntax of np unambiguous and easy to
  parse for both humans and computers.
</p>

<p>
  If the only function parameter is a list or a function, you can omit
  the parentheses - but keep the no-whitespace rule in mind:
</p>

<?= formatCode("-- a function literal as the sole parameter
myFunc{ x | print(x) }
-- a list as the sole parameter
myFunc(: 1 2 3 )") ?>

<h2>Using Functions</h2>

<p>
  Like any other value, functions can be passed around and returned by other
  functions. 
</p>

<?= formatCode("-- generic memoization function
new memoize = { fn | 
  new memList = (:)
  => { n | 
    if memList[n] => memList[n]
    else => fn(n this)
    }
  }
  
-- fast fibonacci generator
new memFib = memoize{ n f |
  if (n < 3) => n
  else => f(n-1 f) + f(n-2 f)
  }
  
print( memFib(20) )
") ?>
<?= formatOutput('10946') ?>

<h2>This Keyword</h2>

<p>
  Inside any function, the reserved keyword <code>this</code> refers to the function
  itself. 
</p>

<?= formatCode("-- counting to 10 with recursion
new fCount = { i |
  print(i)
  if i < 10 this(i+1)
  }
fCount()
") ?>

<h2>Returning Values</h2>

<p>
  Functions can return values with the return operator <code>=></code>.
  If a function terminates without explicitly returning anything,
  the return value is considered to be <code>nil</code>. 
</p>

<?= formatCode("new f = { x | => x*x } -- a function that squares x") ?>

<p>
  You can return multiple values at once, seperated by commata:
</p>

<?= formatCode("-- a function with two return values
new f = { x | => x*x, 1/x }
-- using the values
new square, inv = f(10)
print(#square square #inv inv)
") ?>
<?= formatOutput('square	100	inv	0.1') ?>

<p>
  If a function returns more values than are used by the caller,
  these values are silently discarded.
</p>

<h2>Variable Parameters</h2>

<p>
  By default, a function takes only parameters that have been explicitly
  defined in the parameter list. However, with the variable parameters
  operator <code>...</code> you can reserve room for an arbitrary amount
  of incoming values.
</p>

<?= formatCode("new f = { ... |
  -- variable parameters can be cast into a list:
  print( #Parameters (:...) ) 
  -- or even returned 
  => ...
  }

f( 1 2 3 4 5 )") ?>

<?= formatOutput('Parameters	(: 1 2 3 4 5 )') ?>

<p>
  Using the <code>expand</code> behavior of lists, you can formulate
  functions that mutate and then return all parameters in order: 
</p>

<?= formatCode("new f = { mul ... |
  -- fill all params into a list, multiply the values, return back as single values 
  => ( (:...) ):map{ v | => v*mul }:expand()
  }

print( f( 10 2 3 4 5 ) )") ?>

<?= formatOutput('1	4	9	16	25') ?>

<h2>Short Implicit Parameters</h2>

<p>
  If you leave the parameter list of a function empty, np generates
  the following short names for you that automatically refer to the
  first three parameters (if present): <code>_1</code>, <code>_2</code>,
  and <code>_3</code>.
</p>

<?= formatCode("new f = {| => _1 * _2 }
print( f( 2 4 ) )") ?>

<?= formatOutput('8') ?>