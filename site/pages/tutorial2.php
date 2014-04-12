<?

$GLOBALS['title'] = 'np Tutorial';

ob_start();

?>

<div id="side"><iframe id="srcwnd" src="http://openfu.com/dev/np/addons/cgidemo.php" style="width: 100%; border: none; height: 100%;"></iframe></div>

<div class="tutcol">

  <h2>Getting Started</h2>
  
  <div>
  
    <p>
    The easiest way to try out np is by simply using this handy tutorial, which allows you to
    enter and execute code right from the convenience of your own browser. This tutorial is intended
    for people who already know one or more other programming languages.
    <div align="right">try it out -----&gt;</div>
    </p>
    
  </div>
  
  <h2>Hello World</h2>
  
  <div>
  
  <p>
    Here's a very simple np program:    
  </p>
    
  <pre class="sh_np">print('Hello World!')</pre>
  
  <p>
    will print out:
  </p>
  
  <pre class="op">Hello World!</pre>
  
  <p>
  Now, that's not very interesting. Let's make use of variables:
  </p>
    
  <pre class="sh_np">new greeting = 'Hello from a variable'
print(greeting)</pre>
  
  <p>
  Variables are declared with the keyword <code>new</code>, at which
  time they can optionally get a value. There are different types of
  values in np, but these are the basic ones: 
  </p>

  <pre class="sh_np">new imAString = 'Striiiing'
new imANumber = 42
new imABoolean = true
new imAList = (: 1 2 3)
new imANothing = nil

print(imAString imANumber 
  imABoolean imAList imANothing)</pre>

  <p>
  Let's have a closer look at the list type.
  </p>
  
  <pre class="sh_np">new places = (: 'earth' 'solar system' 'galaxy')
for nr, thing in list(places) {
  print(nr 'Hello' thing)
}</pre>
  
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
  
  <pre class="sh_np">print('Commencing FizzBuzz Sequence...')
for i = 1, 100 {
  new fbResult 
  if (i % 3 == 0) fbResult = 'Fizz' 
  if (i % 5 == 0) fbResult .= 'Buzz' 
  if (!fbResult) fbResult = i 
  print(fbResult)
}</pre>
  
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
    <code>*=</code> (multiply by), <code>/=</code> (divide by), <code>.=</code> (append to).
  </p>
  
  </div>

</div>

<?

$c = ob_get_clean();

print(str_replace('<pre class="sh_np">', '<a class="loadsrc" 
  onclick="document.getElementById(\'srcwnd\').setAttribute(\'src\', 
    \'http://openfu.com/dev/np/addons/cgidemo.php?code=\'+escape($(this).next().text()));">&gt;&gt;&gt;</a><pre class="sh_np prettyprint lang-lua">', $c));

?>
<script>



</script>
