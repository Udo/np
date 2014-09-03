<?

$GLOBALS['title'] = 'np Tutorial';

ob_start();

?>

<div id="side"><iframe id="srcwnd" src="invoke/cgidemo.php" style="width: 100%; border: none; height: 100%;"></iframe></div>

<div class="tutcol">

  <h2>Getting Started</h2>
  
  <div>
  
    <p>
    The easiest way to try out np is by simply using this handy tutorial, which allows you to
    enter and execute code right from the convenience of your own browser. This tutorial is intended
    for people who already know one or more other programming languages.
    <div align="right">try it out ⇥</div>
    </p>
    
  </div>
  
  <h2>Hello World</h2>
  
  <div>
  
  <p>
    Here's a very simple np program:    
  </p>
    
  <?= formatCode("print('Hello World!')") ?>
  
  <p>
    will print out:
  </p>
   
  <pre class="op">Hello World!</pre>
  
  <p>
  Now, that's not very interesting. Let's make use of variables:
  </p>
    
  <?= formatCode("new greeting = 'Hello from a variable'
print(greeting)") ?>
  
  <p>
  Variables are declared with the keyword <code>new</code>, at which
  time they can optionally get a value. There are different types of
  values in np, but these are the basic ones: 
  </p>

  <?= formatCode("new imAString = 'Striiiing'
new imANumber = 42
new imABoolean = true
new imAList = (: 1 2 3)
new imANothing = nil

print(imAString imANumber imABoolean imANothing)
print(imAList)") ?>

  <p>
  Let's have a closer look at the list type.
  </p>
  
  <?= formatCode("new places = (: 'earth' 'solar system' 'galaxy')
places:each { thing |
  print('Hello' thing)
  }") ?>
  
  <p>
  This already contains some juicy stuff: the <code>places</code> variable is filled with
  a list of places, and finally each of the list entries is printed out in a loop. 
  
  </p>
    
  </div>
  
  <h2>FizzBuzz</h2>
  
  <div>
  
  <p>
    Say you're faced with the following problem: count from 1 to 100, and everytime
    the number is a multiple of 3 say "Fizz", if the number is a multiple of 5 say
    "Buzz", if it's both say "FizzBuzz", otherwise just say the number. This is where
    drinking games tend to go wrong. Luckily, with np you can program a FizzBuzzBuddy
    to help you with this.
  </p>
  
  <?= formatCode("print('Commencing FizzBuzz Sequence...')
for i = 1, 100 {
  new fbResult 
  if (i % 3 == 0) fbResult = 'Fizz' 
  if (i % 5 == 0) fbResult <<= 'Buzz' 
  if (!fbResult) fbResult = i 
  print(fbResult)
}") ?>
  
  <h3>for Loops</h3>
  
  <p>
  The first interesting thing that happens here is the for loop, it
  counts from a number to another number, storing the result in a variable each time.
  We called this variable <code>i</code> in the example. The generic for of the for loop
  looks like this:
    <pre>for loopVariable = fromNumber, toNumber[, optionalStepSize] {
}</pre>
  </p>
  
  <h3>if Statements</h3>
  
  <p>
  As in other languages, <code>if</code> statements in np handle conditional code execution.
  Technically, you would not need the parens around the condition itself, however, it makes
  the code more readable. The generic form looks like this:
  </p>

  <pre>if (condition1) { 
  thenDoSomeStuff()  
else (condition2) 
  elseDoOtherStuff() 
}</pre>

  <p>
    If there is just one statement after the condition, you can omit the curly braces for terseness.
    This example shows one <code>else</code> condition, but an <code>if</code> statement can
    in fact have as many more as may be required.
  </p>
  
  <h3>Operators</h3>
  
  <p>
  The FizzBuzz example shows off some of np's operators as well. First, there are the <b>arithmetic
  operators</b> which perform math operations: <code>+</code> (add), <code>-</code> (subtract), 
  <code>/</code> (divide), <code>*</code> (multiply), <code>%</code> (modulo),
  and <code>^</code> (to-the-power-of). 
  </p>
  
  <p>
  Next are the <b>logical operators</b>: <code>and</code> (logical and), <code>or</code> (logical or),
  <code>not</code> (logical negation) and its short form <code>!</code> which can optionally be
  used as an unary negation as shown in the example.  
  </p>
  
  <p>
  In np, only the values <code>false</code> and <code>nil</code> are considered false-y. All other
  values will evaluate to <code>true</code> in logical statements.
  </p>
  
  <p>
  To chain two strings together, np employs <code>..</code> (the concatenation operator).
  </p>
  
  <p>
  Lastly, there are the <b>assignment operators</b>, with <code>=</code> being the most basic form.
  Combined with some of the other operators, compound assignments are possible where
  the right-hand side of an assignment is an operation relative to the left-hand side.
  In the FizzBuzz example, we're using the concatenation compound assignment to
  append a value to an existing string.
  </p>
  
  <p>The following <b>compound assignments</b> are supported: <code>+=</code> (add to), <code>-=</code> (subtract from), 
    <code>*=</code> (multiply by), <code>/=</code> (divide by), <code><<=</code> (append to).
  </p>
  
  </div>
  
  <h2>Functions</h2>
  
  <p>
  Functions are pieces of code you can call. They may return one or more results, and they
  may take one or more parameters in doing so. Consider for your amusement, the FizzBuzzer:
  </p>
  
  <?= formatCode("new fizzBuzzer = { i |
  new fbResult 
  if (i % 3 == 0) fbResult = 'Fizz' 
  if (i % 5 == 0) fbResult <<= 'Buzz' 
  if (!fbResult) fbResult = i 
  => fbResult, i
  }

print(fizzBuzzer(15))") ?>
  
  <p>
  What we've done here is package the FizzBuzz logic from the previous example
  into a function called fizzBuzzer(). When we call fizzBuzzer() with a number,
  it will return two things: the fizz buzz value and the original number.
  </p>
  
  <p>
  Say you wanted to store the function result in a variable for later:
  </p>
  
  <?= formatCode("new myFavoriteResult = fizzBuzzer(150)") ?>
  
  <p>
  This stores only the first value of the result. You can store both, like this:
  </p>
  
  <?= formatCode("new myFavoriteResult, original = fizzBuzzer(150)") ?>
  
  <p>
  np has some handy features that deal with multiple return values and function
  parameters. For example, let's say you only want the second result that comes
  out of the fizzBuzzer(): 
  </p>
  
  <?= formatCode("print(select(2, fizzBuzzer(150)))") ?>
  
  <p>
  Or you can condense all results into a list:
  </p>
  
  <?= formatCode("new myPreciousss = list.condense(fizzBuzzer(150))
myPreciousss:each { v |
  print(v)
  }") ?>

  <p>
  Likewise, you can expand a list into several function parameters:
  </p>
  
  <?= formatCode("-- there
new myPreciousss = list.condense(fizzBuzzer(150))
  
-- and back again  
new backAgain = { fz org n |
  print('fizz:' fz 'original:' org 'params' nr)
}

backAgain(list.expand(myPreciousss))") ?>

  <p>
  But whatever happened to "n"? Well, <code>list.expand()</code>
  works only on list entries that don't have a name. But more about list
  features later!
  </p>

  <h2>Scope</h2>

  <p>
  Every variable you use must be declared beforehand. An error is triggered if you attempt
  to assign a value to a non-existant variable.
  </p>

  <?= formatCode("bla = 1") ?>
  
  <p>
  When you declare a variable, you can assign a value to it immediately, or you can leave
  it empty for future use:
  </p>

  <?= formatCode("new someNumber = 2
new downForWhatever

print(someNumber downForWhatever)") ?>

  <p>
  Functions have access to variables declared in the parent context, thereby creating closures.
  </p>
  
  <?= formatCode("new outerVar = 100

new myFunc = {| 
  print(outerVar) 
  }

myFunc()") ?>
  
  <p>
  In np, every function is in its own little world, and local variables can overshadow outer ones.
  </p>
  
  <?= formatCode("new vegetable = 'Potato'
print(vegetable)
new f = {|
  new vegetable = 'Bacon' 
  print(vegetable 'is my vegetable')
  }
f()
print(vegetable)") ?>

  <p>
  However, if no local variable by a certain name can be found, np
  will try and look if it's available in the outer context:
  </p>

  <?= formatCode("new vegetable = 'Potato'
new f = {|
  print(vegetable 'is my vegetable')
  }
f()") ?>

  <h3>Block Scope</h3>
  
  <p>
  Let's give the <i>block</i> a formal introduction. A block is a piece of
  code enclosed by curly braces: <code>{ ... }</code>. It's like a function,
  but can't do a lot of the things that functions can. Blocks cannot:
  <ul>
    <li>have parameters</li> 
    <li>return values</li>
    <li>be passed around in variables</li>
  </ul>
  Unlike a function, a block is always executed where it's found. It is
  very useful to
  <ul>
    <li>tie multiple statements together</li>
    <li>limit the scope of variables</li>
  </ul>
  </p>
  
  <?= formatCode("if true {
  new hubris = 'superiority'
  print(\"Look at me, I'm a block.\")
  print(\"I even got my own variable.\")
  print(\"It's a sign of my \" hubris)
}") ?>
  
  <h3>Scope Shenanigans</h3>

  <p>
  Using an outer variable in a function can be very useful. Local variables
  and hierarchical contexts are not limited to functions, they can be used
  in blocks, too. For example, consider this function that wants to keep
  a secret:
  </p>  
  
  <?= formatCode("new guessMe 
{
  new secretNumber = 42
  guessMe = { guess |
    if guess == secretNumber
      => 'Darn, you guessed it'
    otherwise
      => 'Nope, fail haha'
  }
}
print(guessMe(1))
print(guessMe(13))
print(guessMe(42))") ?>
  
  <p>
  This example shows how easy it is to give functions and objects an internal
  state that can't be accessed from the outside, and all without polluting the
  variable space. Since <code>secretNumber</code> is bound to the block it
  was created in, it's not visible to anything outside that block. Since 
  <code>guessMe()</code> was created within the block, it's the only witness
  of <code>secretNumber</code>'s existence once the block terminates.
  </p>
  
  
  <h3>Events</h3>
  
  <p>
    One of the ways to make lists do things is to define
    events they can respond to. 
    There are some standard events that allow you to modify how
    a list responds to common incidents - but you can also specify
    your own. In this sense, event handlers in np are a lot like class
    methods in other languages.
  </p>
  
  <p>
  Let's make a Bugbear that can talk, by providing a <code>say</code> event:
  </p>
  
  <?= formatCode("new BugbearEvents = (:
  say = { creature text | 
    print(creature.name 'says:' text 'BURP!') }
  )
new bear = BugbearEvents:create(: name = 'Bjorn' )
bear:say('Hello everybody!')") ?>

  <p>
  np also gives you a standard event called <code>init</code> which provides a neat place to initialize lists at the moment of creation: 
  </p>

  <?= formatCode("
new BugbearEvents = (:
  say = { creature text | 
    print(creature.name #the creature.species 
      'says:' text 'BURP!') }
  init = { creature | 
    creature.species = #Bugbear }
  )
new bear = BugbearEvents:create(: name = 'Bjorn' )
bear:say('Hello everybody!')") ?>

  <p>
  In this example, we use the <code>create</code> handler to give
  Bjorn a species field. 
  </p>
	
<h3>Summary</h3>

  <p>So in this tutorial, we covered a lot and illustrated it with contrived examples! 
    You learned about basic functions, how to use functions and other syntax, how scope
    works, that lists are the basic object type in np, and how objects can respond to
    events.
  </p>
 
</div>

  <div style="height:200px"></div>
  
<?

$c = ob_get_clean();

print(str_replace('<pre class="sh_np">', '<a class="loadsrc" title="execute this code example"
  onclick="document.getElementById(\'srcwnd\').setAttribute(\'src\', 
    \'invoke/cgidemo.php?code=\'+escape($(this).next().text()));">⇥</a><pre class="sh_np">', $c));

?>
