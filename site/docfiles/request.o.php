<?php

  $info = array(
    'title' => 'request',
    'type' => 'object',
    'tags' => array('http'),
    'params' => array(
      ),
    'nparams' => array(
      ),
    'see' => array()
    );

?>
<h2>Description</h2>  

<p>
  The <code>request</code> object is available from the root context and is therefore
  a globally accessible variable. <code>request</code> is a map object containing all
  HTTP variables (both GET and POST) for the current request. 
</p>

<h2>Examples</h2>

<?= formatCode("println request;") ?>

will output (for example):

<?= formatOutput("(map 'someparameter':'some content')") ?>

<h2>Object Members</h2>

<? displayVariableHead('cookies', array()) ?>

  <p>
  Contains all the cookie variables handed over from the browser during the current request.
  </p>

  <?= formatCode("println (request.cookies);") ?>
  
  Will output (for example):

  <?= formatOutput("(map 'test':'COOKIEZ!')") ?>
  
  <br/>


<? displayVariableHead('env', array()) ?>

  <p>
  Contains all the environment variables of the current FastCGI request.
  </p>

  <?= formatCode("println (request.env.keys);") ?>
  
  <?= formatOutput("(list 'HTTP_ORIGIN' 'HTTP_CONTENT_TYPE' 'HTTP_USER_AGENT' 'CONTENT_TYPE' 'HTTP_ACCEPT_LANGUAGE' 'HTTP_REFERER' 'HTTP_ACCEPT' 'HTTP_CACHE_CONTROL' 'ROLE' 'DOCUMENT_URI' 'HTTP_ACCEPT_ENCODING' 'REMOTE_PORT' 'SERVER_SOFTWARE' 'SERVER_NAME' 'REDIRECT_STATUS' 'SCRIPT_FILENAME' 'SERVER_PROTOCOL' 'SERVER_ADDR' 'REQUEST_METHOD' 'SERVER_PORT' 'SCRIPT_NAME' 'REMOTE_ADDR' 'DOCUMENT_ROOT' 'HTTP_ACCEPT_CHARSET' 'HTTP_HOST' 'HTTP_CONNECTION' 'QUERY_STRING' 'GATEWAY_INTERFACE' 'HTTP_CONTENT_LENGTH' 'CONTENT_LENGTH' 'REQUEST_URI')") ?>
  
  <br/>


<? displayVariableHead('header', array()) ?>

  <p>
  Contains all the header variables that are going to be sent back to the server. By default, np
  sets only the <code>content-type</code> header automatically (it defaults to "text/html; charset=utf-8").
  </p>

  <br/>


<? displayVariableHead('get', array()) ?>

  <p>
  Contains all the HTTP variables that have been set through the request's URL parameters.
  </p>

  <br/>


<? displayVariableHead('post', array()) ?>

  <p>
  Contains all the HTTP variables that have been set through the body of the request issued by the browser.
  </p>

  <br/>

<? displayFunctionHead('setCookie', array('name', 'value', '#expires:t')) ?>

  <p>
  Sets a cookie. If you use the optional named parameter <code>expires</code>, you can
  specify a Unix timestamp when the cookie will expire. By default, all cookies last 24 hours.
  The function returns the cookie header that was sent to the browser.
  </p>

  <?= formatCode("println (request.setCookie 'cookiename' 'payload');") ?>
  
  <?= formatOutput("Set-Cookie: cookiename=payload; Expires=Mon, 18-Mar-2013 19:49:15 GMT; HttpOnly; ") ?>
  
  <br/>



