<?

  $GLOBALS['title'] = 'Welcome';

?>
<div>

  Hi and welcome to the np reference site. np is an attempt
  at making a small and minimal programming language for web
  development. 
  
</div>

<div>

  <table width="100%">
    <tr><td width="280" valign="top"><div 
      style="text-align: center; padding-right: 24px; margin-right: 24px; margin-top: 24px; min-height: 300px;
        border-right: 1px solid rgba(0,0,0,0.1);">
         
         <h3>Curious?</h3>
         
         <a class="megabutton" href="/tutorial">Try it out now &gt;
           <br/><span style="font-weight:normal;">pain free, in your browser</span>
         </a>
    
    </div></td><td valign="top">
    
  <h3>What's np?</h3>
    
  <p>
    np is a duck-typed scripting language that aims to be easy and productive to
    work with. It tries to achieve that by being as minimal as possible while still
    providing the amenities needed to write code that does a lot in a speedy manner.
    np is object-oriented, but it's neither based on classes nor on prototypes. Instead,
    objects can implement behaviors that respond to events.
  </p>
  
  <p>
    This is still a work in progress and the language as well as its standard
    libraries are not stable yet. However, there is enough substance to play 
    around with it, and you can safely try it out in the browser by visiting the
    <a href="/tutorial">tutorial</a>.
  </p>
    
  <h3>np as a Web Platform</h3>
  
  <p>
    When I'm done with the base runtime, I'd like to give np a
    FastCGI server and library support to do web development with.
    The idea here is, that you only have to think about each
    current request, as opposed to more heavy-weight application
    server frameworks such as Ruby on Rails or Tomcat where the
    app is running the whole time. In this respect, np server
    will be a stateless environment, which eliminates a lot
    of code and runtime complexity, and it should also make scaling
    out easy.
  </p>
  
  <h3>It's just an experiment</h3>

  <p>
    At this point, np is nothing but an experiment that popped into my head,
    something I liked to try. It's not complete. You can't really do useful
    things with it yet, but it's mature enough to at least head on over to the
    tutorial and get a feel for the language.  
  </p>
  
  <h3>So what are the dependencies?</h3>

  <p>
    I tried to use as little external frameworks and libraries as possible, and this
    is actually the second iteration of the language. The first one was written in Java,
    and while it was interesting and kind of worked, it became clear to me that I wanted
    to switch to a more minimal environment. So this second incarnation is based on the
    Lua runtime which is fast, small, and easy to modify. The only dependency is glibc.
  </p>

  <h3>Does the world need another language?</h3>

  <p>
    Probably not, but it's fun anyway. There are some things I wanted to explore with
    np, many of them have to do with dynamic typing and scope. 
  </p>
    
    
    </td></tr>
  </table>

  

</div>