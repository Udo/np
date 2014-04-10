<?php

  $info = array(
    'title' => 'enc',
    'type' => 'object',
    'tags' => array('library'),
    'params' => array(
      ),
    'nparams' => array(
      ),
    'see' => array('string.o', 'map.o', 'list.o')
    );

?>
<h2>Description</h2>  

<p>
  The <code>enc</code> object contains a bunch of members for encoding and decoding.
</p>

<h2>Object Members</h2>

<? displayFunctionHead('base64Decode', array('value')) ?>

  <p>
  Decodes a given base64-encoded string and returns it.
  </p>

  <?= formatCode("println (enc.base64Decode 'bG9vaywgaSdtIGEgbWVzc2FnZSE=');") ?>
  
  <?= formatOutput("look, i'm a message!") ?>
  
  <br/>

<? displayFunctionHead('base64Encode', array('value')) ?>

  <p>
  Encodes a string in the base64 data format.
  </p>

  <?= formatCode("println (enc.base64Encode \"look, i'm a message!\");") ?>
  
  <?= formatOutput("bG9vaywgaSdtIGEgbWVzc2FnZSE=") ?>
  
  <br/>


<? displayFunctionHead('csvValueDecode', array('value')) ?>

  <p>
  Decodes a single element of a CSV encoded cell.
  </p>

  <?= formatCode("println (enc.csvValueDecode '\"look, decoded!\"');") ?>
  
  <?= formatOutput("look, decoded!") ?>
  
  <br/>


<? displayFunctionHead('csvValueEncode', array('value')) ?>

  <p>
  Encodes a value for inclusion into a CSV by escaping the necessary characters.
  </p>

  <?= formatCode("println (enc.csvValueEncode 'look, encoded!');") ?>
  
  <?= formatOutput("\"look, encoded!\"") ?>
  
  <br/>


<? displayFunctionHead('date', array('[timestamp] #format:f #timezone:t')) ?>

  <p>
  Returns a string with a human-readable date. If the optional variable <code>timestamp</code>
  containing a Unix timestamp is provided, the date corresponding to that timestamp is returned,
  otherwise the current time is used.
  </p>
  
  <p>
  The optional named parameter <code>#format</code> is used to specify what the date should look like.
  As a default the string <code>yyyy-MM-dd HH:mm:ss</code> is used. You can define your own, using
  these characters as placeholders within the format string:
    <div><code>y</code>: year</div>
    <div><code>M</code>: month</div>
    <div><code>w</code>: week of the year</div>
    <div><code>W</code>: week of the month</div>
    <div><code>D</code>: day of the year</div>
    <div><code>d</code>: day of the month</div>
    <div><code>F</code>: week of the month</div>
    <div><code>E</code>: day of the week (as english weekday name)</div>
    <div><code>a</code>: either AM or PM</div>
    <div><code>H</code>: hour of the day (0-23)</div>
    <div><code>K</code>: hour of the day (0-11)</div>
    <div><code>m</code>: minute of the hour</div>
    <div><code>s</code>: second of the minute</div>
    <div><code>S</code>: millisecond</div>
    <div><code>z</code>: time zone name</div>
    <div><code>Z</code>: RFC 822 time zone</div>
  </p>

  <p>
  The optional named parameter allows you to specify the time zone that is to be used.
  The default for this is UTC. You can specify your timezone relative to GMT, like e.g.
  GMT+1 - or you can use your regional abbreviation (note, however, that these are always
  unique).
  </p>
  
  <?= formatCode("println (enc.date (sys.time) #timezone:'GMT');") ?>
  
  which will output (for example):
  
  <?= formatOutput(gmdate('Y-m-d H:i:s')) ?>
  
  <br/>


<? displayFunctionHead('htmlDecode', array('value')) ?>

  <p>
  Decodes a string where special HTML characters have been replaced by HTML entities.
  </p>

  <?= formatCode("println (enc.htmlDecode '&lt;tag &oslash;!&gt;');") ?>
  
  <?= formatOutput("<tag ø!>") ?>
  
  <br/>


<? displayFunctionHead('htmlEncode', array('value')) ?>

  <p>
  Escapes special HTML characters by converting them into HTML entities.
  </p>

  <?= formatCode("println (enc.htmlEncode '<tag ø!>');") ?>
  
  <?= formatOutput("&lt;tag &oslash;!&gt;") ?>
  
  <br/>


<? displayFunctionHead('jsonDecode', array('value')) ?>

  <p>
  (not implemented yet)
  </p>
  
  <br/>


<? displayFunctionHead('jsonEncode', array('value')) ?>

  <p>
  Encodes an np object structure as JSON. Note that object members are not encoded,
  only object payloads are - such as list items, map tuples, and primitive types.
  </p>

  <?= formatCode("m = (map #a:1 #b:2 #c:3);
println (enc.jsonEncode m);") ?>
  
  <?= formatOutput('{"b":2.0,"c":3.0,"a":1.0}') ?>
  
  <br/>


<? displayFunctionHead('markdownToHtml', array('value')) ?>

  <p>
  Converts a string containing Markdown code into HTML.
  </p>

  <?= formatCode("println (enc.markdownToHtml 'An h2 header
------------

Here's a numbered list:

 1. first item
 2. second item
 3. third item');") ?>
  
  <?= formatOutput("<h2>An h2 header</h2>
<p>Here is a numbered list:</p>
<ol>
<li>first item</li>
<li>second item</li>
<li>third item</li>
</ol>") ?>
  
  <br/>


<? displayFunctionHead('md5', array('value', '#format:f')) ?>

  <p>
  Encodes a string value as an MD5 hash.
  </p>
  
  <p>
    The resulting hash number format can be changed with the <code>#format</code> parameter:
    <div><code>'hex'</code> means hexadecimal format (default).</div>
    <div><code>'numeric'</code> means numeric format.</div>
    <div><code>'binary'</code> means raw binary output.</div>
  </p>

  <?= formatCode("println (enc.md5 'hello np');") ?>
  
  <?= formatOutput("f01d2303534bd427235a4067e8849225") ?>
  
  <br/>



<? displayFunctionHead('nl2br', array('value')) ?>

  <p>
  Replaces line breaks in <code>value</code> with <code>&lt;br/&gt;</code> tags and returns the result.
  </p>

  <?= formatCode("println (enc.nl2br '1
2
3');") ?>
  
  <?= formatOutput("1
<br/>2
<br/>3") ?>
  
  <br/>


<? displayFunctionHead('numberFormat', array('value', '#decimals:de #thousands:th #point:pt #pad:pd #padWith:pw')) ?>

  <p>
  Formats a numeric value into a string.
  </p>
  
  <p>
    The following optional named parameters control the output of this function:
    <div><code>#decimals</code>: the number of decimal places to show (default: 2).</div>
    <div><code>#thousands</code>: the character to use for the thousands separator (default: comma).</div>
    <div><code>#point</code>: the character to use for the decimal point (default point).</div>
    <div><code>#padTo</code>: pad output to a certain length by prepending a string (default: 0).</div>
    <div><code>#padWith</code>: pad to length with this string (default: space).</div>
  </p>

  <?= formatCode("println 
  (enc.numberFormat #decimals:3 -1456.0286839419);") ?>
  
  <?= formatOutput("-1,456.029=") ?>
  
  <br/>


<? displayFunctionHead('random', array('length', '#type:t')) ?>

  <p>
  Returns a string made of random characters. If the optional named parameter
  <code>#type</code> is used, the following types of random strings are possible:
  
  <div><code>alpha</code>: only alphabetic characters are used</div>
  <div><code>numeric</code>: only digits are used</div>
  <div><code>ascii</code>: the whole ascii range is used</div>
  <div><code>alphanumeric</code> (default) only numbers and digits are used</div>
  </p>

  <?= formatCode("println (enc.random 10);") ?>
  
  <?= formatOutput("iuodOKsy3k") ?>
  
  <br/>


<? displayFunctionHead('scriptDecode', array('value')) ?>

  <p>
  Decodes a string that contains escaped characters to conform with JavaScript string rules.
  </p>

  <?= formatCode("println (enc.scriptDecode '".'\\'."\"quotez".'\\'.'”everywhere'."');") ?>
  
  <?= formatOutput('"quotez”everywhere') ?>
  
  <br/>


<? displayFunctionHead('scriptEncode', array('value')) ?>

  <p>
  Encodes a string by escaping characters in order to conform with JavaScript string rules.
  </p>

  <?= formatCode("println (enc.scriptEncode '\"quotez\"everywhere');") ?>
  
  <?= formatOutput('\"quotez\"everywhere') ?>
  
  <br/>
  

<? displayFunctionHead('sha1', array('value', '#format:f')) ?>

  <p>
  Encodes a string value as a SHA-1 hash.
  </p>
  
  <p>
    The resulting hash number format can be changed with the <code>#format</code> parameter:
    <div><code>'hex'</code> means hexadecimal format (default).</div>
    <div><code>'numeric'</code> means numeric format.</div>
    <div><code>'binary'</code> means raw binary output.</div>
  </p>

  <?= formatCode("println (enc.sha1 'hello np');") ?>
  
  <?= formatOutput("2ee515c2b9f4be92cf509fa90731aa626af7fe73") ?>
  
  <br/>





<? displayFunctionHead('urlDecode', array('value')) ?>

  <p>
  Decodes a string by un-escaping the URL-encoded characters in it.
  </p>

  <?= formatCode("println (enc.urlDecode '%26full%3Aof%2Bstuff');") ?>
  
  <?= formatOutput("&full:of+stuff") ?>
  
  <br/>


<? displayFunctionHead('urlEncode', array('value')) ?>

  <p>
  Encodes a string by escaping characters that should not be in a URL parameter.
  </p>

  <?= formatCode("println (enc.urlEncode '&full:of+stuff');") ?>
  
  <?= formatOutput("%26full%3Aof%2Bstuff") ?>
  
  <br/>



<? displayFunctionHead('urlMapDecode', array('value')) ?>

  <p>
  Decodes a string that contains URL-encoded name-value pairs into a Map object.
  </p>

  <?= formatCode("println (enc.urlMapDecode 'a=b&c=d&e=f');") ?>
  
  <?= formatOutput("{e=f, c=d, a=b}") ?>
  
  <br/>
  
  
<? displayFunctionHead('uuid', array('')) ?>

  <p>
  Generates a globally unique identifier string.
  </p>

  <?= formatCode("println (enc.uuid);") ?>
  
  Will output (for example):
  
  <?= formatOutput("db83686c-8640-427d-88db-01126889a927") ?>
  
  <br/>




<? displayFunctionHead('xmlDecode', array('value')) ?>

  <p>
  Decodes a string where special XML characters have been replaced by XML entities.
  </p>

  <?= formatCode("println (enc.xmlDecode '&lt;tag ø!&gt;');") ?>
  
  <?= formatOutput("<tag ø!>") ?>
  
  <br/>


<? displayFunctionHead('xmlEncode', array('value')) ?>

  <p>
  Escapes special XML characters by converting them into XML entities.
  </p>

  <?= formatCode("println (enc.xmlEncode '<tag ø!>');") ?>
  
  <?= formatOutput("&lt;tag ø!&gt;") ?>
  
  <br/>



