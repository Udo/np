<?

$GLOBALS['title'] = '::Tutorial';

ob_start();

?>

<div id="side"><iframe id="srcwnd" src="http://rpgp.org/np/test/remoteexec.np?" style="width: 100%; border: none; height: 100%;"></iframe></div>

<div class="tutcol">

<h2>Obligatory</h2>

<div>

  <p>
  Let's start by seeing if everything works. If your webserver
  is ready to process np, create a text file inside a public
  folder that ends with <code>.np</code> - this lets np know
  that it should execute it. For this tutorial you could create 
  a file called <code>hello.np</code> in your directory
  and then navigate to it with a browser. 
  </p>
  
  <p>
  For quick experimentation though, you can execute np programs directly
  in your browser window! To the right of every code example,
  you'll find a yellow button with three little arrows. Click
  on the button to load the example code into the demo sandbox
  to the right. You can also freely edit that code in there.
  Click on "Run!" to run your code.
  <div align="right">This is your sandbox ---------------&gt;</div>
  </p>
  
  <p>
  A minimal np program would print something out into
  your browser document. So let's see:
  </p>

<pre class="sh_np">println 'hello world!';</pre>

  <p>
  …which, after you refresh your browser view, should display
  <code>hello world!</code>. So far no surprises. Now let's dive
  into some actual functionality!
  </p>
  
  <p>
  Fundamentally, everything in np is a function call. Functions
  are called, with or without parameters, and then they usually return
  something. In our example, <code>println</code> is one of the
  built-in functions that ship with np, and the string <code>'hello world'</code>
  is the parameter that gets fed into the function. The
  semicolon at the end indicates that the statement ends there.
  Whitespace and formatting generally don't matter to np, so 
  you're free to make your code as pretty as you like. Pretend
  it's poetry!
  </p>
  
  <p style="border: 1px solid gray; background: #eef; padding: 8px;">
    <b>Quick notice for people used to C-like languages (C, C++, Java, PHP):</b>
    In np, a function call begins with parentheses, like this: <code>(function a);</code>,
    as opposed to <code style="color: gray">function(a);</code>. You may omit the
    parentheses if the syntax is straight-forward - and in fact many examples here
    work without parentheses at all - but always keep this single rule in mind.
    Otherwise, you'll be fine. 
  </p>
  
</div>

<h2>Variables</h2>

<div>

  <p>
  Variables begin to exist the moment they are filled with a value.
  This value can be different things, such as a number, a string,
  a list, or any other kind of object.
  </p>
  
<pre class="sh_np">thingsThatAreAwesome = 'cats';
magicNumber = 40 + 2;

println magicNumber ' ' thingsThatAreAwesome;</pre>

  <p>
  will print out:
  </p>

<pre class="op">42 cats</pre>

  <p>
  And already np has shown you some trivial things about itself:
  There is a semicolon at the end of each statement and things like
  parameters to function calls are separated by whitespace, not
  commas. Identifiers in np are case-sensitive and must start with
  an alphabetic character. np requires at least one whitespace to
  separate arguments, but otherwise does not care about formatting
  such as line breaks or tabs. Again, so far no surprises.
  </p>

  <h3>np Variables Summary</h3>  
  <ul>
    <li>case sensitive</li>
    <li>must start with alphabetic character</li>
    <li>don't need to be declared before use</li>
    <li>can be overwritten</li>
    <li>identifiers separated by space</li>
  </ul>

</div>

<h2>Functions</h2>

<div>

  <p>
  Programming wouldn't be much fun without being able to define your
  own stuff. Making your own functions really ties the whole thing
  together. Let's do that:
  </p>
  
<pre class="sh_np">myFunction = { n |
  n*n
  };
  
println "fair and square: " (myFunction 2);</pre>

  <p>
  Whoa, whoa, what's going on? First, the variable <code>myFunction</code>
  is filled with the definition of our function - that is everything in between
  the curly brackets. In the second step, we're calling <code>myFunction</code>
  with a parameter of 2 and it returns the square.
  </p>
  
  <p>
  In the part before the pipe symbol <b style="color:red">|</b>, 
  np learns what parameters you expect the function to receive. In this case,
  we give np the hint to expect one parameter, and it will pour that value
  into the variable <code>n</code>.
  </p>
  
  <p>
  After the pipe symbol comes the code that the function executes. In the
  example, that means one operation: calculating the square of <code>n</code>.
  If you leave the semicolon off the last statement in a function right before
  the curly bracket closes, np returns that value. However, this isn't always
  practical, especially if you want the function to return in the middle of
  some code:
  </p>
  
<pre class="sh_np">println ({ n |
  return n*n;
  println 'muhahaha!';
  } 2);</pre>

  <p>
  This example might be more familiar to people who know a C-like language.
  A call to the predefined <code>return</code> will exit the function with
  that value. In this case <code>println 'muhahaha';</code> is never executed.
  </p>
  
  <h3>np Functions Summary</h3>

  <ul>
    <li>functions are defined by a curly brace block <code>{ }</code></li>
    <li>the pipe symbol <code>|</code> is used to define expected arguments</li> 
    <li>in the absence of an argument definition, the <code>(pop)</code> command
        can be used to retrieve a parameter at run time</li> 
    <li>are anonymous, but can be assigned to variables</li>  
    <li><code>return</code> returns value and exits function, or</li>  
    <li>last value at the end returns result to the caller</li>  
    <li>functions are called with or without arguments</li>  
    <li>parentheses are used to do function calls like (println 123)</li>
    <li>parentheses are optional if an entire statement consists of a function call only</li>  
  </ul>

</div>


<h2>Scope</h2>

<div>

  <p>
  By default, variables are temporary and keep their value only during the
  current function call. Variables inside of function don't affect the outside
  even if they have the same name:
  </p>
  
<pre class="sh_np">a = "I'm a!";
f = { a = "Greetings from f!" };
f;

println "global a: " a;
println "a in (f): " f.a; // empty</pre>
    
  <p>
  As you can see, the content of <code>a</code> that was changed inside <code>f</code>
  is lost after the function call completes. You can get around that restriction by using
  the special variable <code>this</code>, which always points to the current object.
  </p>
  
<pre class="sh_np">f = { this.a = (pop) };
f "payload in f";

println "a in (f): " f.a;
</pre>

  <p>
  Notice that we did not declare any parameters in the function head! 
  Instead, I'm showing off the <code>(pop)</code> function to retrieve a parameter. <code>(pop)</code>
  always gets the next waiting parameter if there is one. This is extremely practical if you're not
  sure how many parameters your function has received or if you want to hold off on executing all
  the parameters. <code>(pop)</code> ignores named parameters, however.
  </p>
  
  <h3>Scope summary</h3>

  <ul>
    <li>variables are tightly scoped</li>  
    <li>call-time variables are ephemeral</li>  
    <li>functions (and all other objects) can have member variables</li>  
    <li>member variables can be addressed using <code>this</code></li>  
  </ul>

</div>

<h2>Named Parameters</h2>

<div>

  <p>
  Filling parameters into a function one-by-one isn't the only way to transport
  variables. You can also give them a name:
  </p>
  
<pre class="sh_np">f = { 
  println 
    _name ' And I have ' 
    argCount ' unnamed param '
    'containing ' (pop) ', too.';
  };

f 
  3.1416 
  #name:"I'm using a named param!";</pre>

  <p>
  resulting in this:
  </p>

<pre class="op">I'm using a named param! And I have 1 unnamed param containing 3.1415, too.</pre>

  <p>
  Here, two parameters are pumped into <code>f</code>, one is just the number
  <code>3.1415</code>, the other is a named variable called <code>name</code>.
  By the way, that <code>#</code> symbol is just another way of writing <code>'name'</code>,
  which would also work. 
  </p>
  <p>
  Inside the function, the named variable is introduced
  starting with an underscore <code>_</code> character, as <code>_name</code>.
  The unnamed parameter would be available as usual via <code>pop</code> or by explicitly
  declaring an expected parameter in the function head with the pipe symbol <code>|</code> (see above).
  </p>
  <p>
  Named parameters are especially useful for giving instructions or command hints to your
  function, while unnamed parameters are great for delivering raw data. Since you can mix them
  both within the same function call, you can use named parameters to tell your function more
  about what you expect it to do. In fact, np uses this technique itself in several built-in
  functions, such as <code>println</code>:
  </p>
  
<pre class="sh_np">println 
  'OMG' "it's" 'full' 'of' 'cats' 
  #sep:' ';</pre>

  <p>
  Gives several strings to the <code>println</code> function and the <code>#sep</code>
  parameter then tells it to use a single space to separate them when printing.
  </p>
  
  <h3>Named Parameters Summary</h3>

  <ul>
    <li>passed as function arguments consisting of a name, a colon, and the content</li>
    <li>are always evaluated before the function is called</li>
    <li>available within the function prefixed with an underscore <code>_</code></li>
    <li>can be mixed with unnamed parameters</li>
  </ul>

</div>

<h2>Basic Objects</h2>

<div>

  Consider this variable:
  
<pre class="sh_np">v = 4;
println v;</pre>

  which obviously contains the number 4. But that's not the whole truth, because in reality
  it could hold a lot more than the numeric value. In fact, you can give it additional
  attributes and even assign functions to it.
  
<pre class="sh_np">v = 4;
v.squareme = 
  { return container * container };
println (v.squareme);</pre>
  
  And just like that, you extended the functionality of your object. This example will
  dutifully print out:
  
<pre class="op">16</pre>

  <p>
  Here is what happened: first we made a new number object, called <code>v</code>. Then
  a function was added to <code>v</code>. Inside this function, we access the higher-level
  object <code>container</code>. In this case <code>container</code> is the object v, because it's
  the container of the function <code>square me</code>.
  </p>

  <h3>Basic Objects Summary</h3>
    
  <ul>
    <li>primitive values are objects, as are functions</li>
    <li>objects can be modified at run time</li>
    <li>there are no class definitions</li>
    <li>members can access their higher-order containers using the <code>container</code> keyword</li>
  </ul>
    
  <h2>Lists</h2>

<div>

  <p>
  Lists are a very versatile data structure in np. You can stuff all sorts of things
  into lists, it's the go-to container for anything. Here's how you make a list:
  </p>
  
<pre class="sh_np">myList = list 
  'apples' 'oranges';
println myList;</pre>

  <p>
  Items in lists can be accessed in several ways. For example, by index:
  </p>

<pre class="sh_np">myList = list 
  'apples' 'oranges' 'kiwis';
println myList::0;</pre>

  <p>
  Or you can do something to each entry:
  </p>

<pre class="sh_np">(list 
  'apples' 'oranges' 'kiwis' 'dogs' 'cats')
  .each { item | println item };</pre>

  <h3>List Summary</h3>

  <ul>
    <li>lists are generated by calling the <code>(list)</code> function with parameters</li>
    <li>list members can be accessed using the double colon <code>::</code> operator</li>
    <li>lists implement methods such as <code>(each)</code> that allow for easy iteration over their content</li>
  </ul>

</div>

<h2>Maps</h2>

<div>

  <p>
  While lists are essentially just ordered stacks of objects, it's often times practical
  to have a container where you can access elements by a given name. This container type
  is called a map in np.
  </p>
  
<pre class="sh_np">myMap = map
  #UK:'London' #France:'Paris' #Italy:'Rome';
myMap.printCap = { country | 
  println 
    "The capital of " country 
    " is " container::country
  };
myMap.printCap #UK;
</pre>  

  <p>
  Let's experiment with the <code>map</code>'s <code>each</code> function as well, this time
  without defining a variable to actually hold the map in:
  </p>
  
<pre class="sh_np">(map 
  #UK:'London' #France:'Paris' #Italy:'Rome')
  .(each { country city | 
    println 
      "The capital of " country " is " city 
    });</pre> 
  
</div>

<h2>Control Structures</h2>

<h3>(if)</h3>

<div>

  <p>
  Until now, everything we did was pretty linear. Now it's time to control the flow of
  np programs with structures such as the good old if statement. Like a lot of things,
  control statements in np are just function calls.
  </p>
  
<pre class="sh_np">d20 = math.random 1 20;
if 
  (d20 == 1) { println 'Botched!' }
  (d20 == 20) { println 'Critical Hit!' }
  { println 'Normal Hit: ' d20 };

</pre>  

  <p>
  Similar to switch statements in other languages, in np you can cram as many conditions into
  an <code>if</code> statement as you like. They are looked at in order and the statement
  terminates after the first true condition. To emulate the "else" statement in other languages,
  you can append a last block to the if statement without any condition of its own.
  </p>
    
  
</div>

<h3>(for)</h3>

<div>

  <p>
  Sometimes, you want to perform an action a fixed number of times. In pretty much any language,
  the <code>for</code> statement does that. So here's the <code>(for)</code> statement in np:
  </p>
  
<pre class="sh_np">for 1 3 { myIndex |
  println myIndex ". profit!"
  };</pre>
  
</div>  
  
<h3>(while)</h3>

<div>

  <p>
  
  </p>
  
<pre class="sh_np">while (a < 3) { 
  a = a + 1;
  println a ". profit!"
  };</pre>
  
</div>  

<h2>Safety First</h2>

  <p>
  Whenever you use the <code>print</code> or <code>println</code> commands in np, the output is
  encoded into HTML entities. That is a suitable default for most cases where you need to
  pump your variables straight into documents. Let's try it out:
  </p>
  
<pre class="sh_np"><?= htmlspecialchars("print '<span onclick=\"doEvil()\">look here, an XSS attack!</span>';") ?></pre>

  <p>
  Thus, a large number of scripting-injection attacks against your site is thwarted without you
  having to think about whether a variable is safe or not. However, there are always cases where you
  need to output a string <em>as it is</em>. That's what <code>(unsafePrint)</code> is for:
  </p>

<pre class="sh_np"><?= htmlspecialchars("unsafePrint '<span onclick=\"doEvil()\">look here, an XSS attack!</span>';") ?></pre>

  <p>Keep in mind that the output window of the sandbox does not do HTML formatting, so it'll 
  always show you exactly what np generated.</p>


<h2>Request Variables</h2>

  <p>
  There are three sets of pre-defined <code>map</code>s that contain state information about your
  current request. Fittingly, they are part of a variable called <code>request</code>.
  </p>
  
<pre class="sh_np">println 'Your code was: ' 
  request.post::#code;</pre>

  <p>
  In the example above, we're accessing the <code>request.post</code> map object that contains
  all the members of the current POST request. Since the sandbox uses POST to transmit your example
  code, you can access that variable (which, conveniently, is called "code");
  </p>

<h2>Ident Expressions</h2>
  
  <p>
  This is a good opportunity to learn about <em>ident expressions</em> in np. In the example above,
  we're accessing the member variable "post" of an object. But what if the name of the variable is
  inside another variable? No problem, just square-bracket it:
  </p>

<pre class="sh_np">dynamicName = 'post';
println 'Your code was: '
  request.[dynamicName]::#code;</pre>

  <p>
  You can use ident expressions everywhere in place of normal identifiers.
  </p>


<h2>To be continued</h2>
  
  <p>
  This is the current extent of np, but more is coming. Among these things will be: proper OO prototyping,
  HTML templates, way more built-in functions and objects, profiling tools, json, and possibly a cool
  and concise way to define and implement services within a project. Also missing: cookies, sessions,
  file upload, loading external files, file i/o in general, system functions, command line gateway,
  database (at least MySQL), integrated caching and maybe a WebSocket server option. 
  These are not some far-off goals, I'll add a little day by day, as fast as I can.
  </p>
  
  
  
</div>
<?

$c = ob_get_clean();

print(str_replace('<pre class="sh_np">', '<a class="loadsrc" 
  onclick="document.getElementById(\'srcwnd\').setAttribute(\'src\', 
    \'http://rpgp.org/np/test/remoteexec.np?code=\'+escape($(this).next().text()));">&gt;&gt;&gt;</a><pre class="sh_np">', $c));

?>
  
