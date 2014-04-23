<?

  $GLOBALS['title'] = 'Welcome';

?>
<div>

  Hi and welcome to the np reference site. np is an attempt
  at making a small and minimal programming language for web
  development. 
  
</div>

<div>

  <table width="280" align="left" style="margin-right: 12px; margin-top: 12px;">
    <tr><td><div style="text-align: center;">
         
         <h3>Curious?</h3>
         
         <a class="megabutton" href="/tutorial">Try it out now &gt;</a><br/>
         
         pain free, in your browser 
         
         <br/><br/>
    
    </div></td></tr>
  </table>

  <h3>What is it like?</h3>
  
  <p>
    When writing code in np, you only have to think about each
    current request, as opposed to more heavy-weight application
    server frameworks such as Ruby on Rails or Tomcat where the
    app is running the whole time. In this respect, np
    is a stateless runtime environment. This allows for a very 
    simple development process. 
  </p>
  
  <h3>It's just an experiment</h3>

  <p>
    At this point, np is nothing but an experiment that popped into my head,
    something I liked to try. It's not complete. You can't really do useful
    things with it yet, but it's mature enough to at least head on over to the
    tutorial and get a feel for the language.  
  </p>
  
  <h3>Why didn't you use XYZ framework?</h3>

  <p>
    That was not the point of the exercise. I wanted to explore what it feels
    like to write a parser, a lexer, and an interpreter without using a framework
    or some other ready-made solution - and I didn't really consult any literature
    either. While that may sound foolhardy (and I'm sure the implementation pays the
    price for it), the point was to discover and think about a new set of problems
    I never experienced before. While this is clearly a learning project, I don't
    think it turned out so bad. Hopefully. Maybe.
  </p>

  <h3>So what are the dependencies?</h3>

  <p>
    I tried to use as little external frameworks and libraries as possible, and this
    is actually the second iteration of the language. The first one was written in Java,
    and while it was interesting and kind of worked, it became clear to me that I wanted
    to switch to a more minimal environment. So this second incarnation is based on the
    Lua runtime which is fast, small, and easy to modify. 
  </p>

  <h3>Does the world need another language?</h3>

  <p>
    Probably not, but it's fun anyway. There are some things I wanted to explore with
    np, many of them have to do with dynamic typing and scope. 
  </p>

</div>