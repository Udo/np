<?php
/**
 * Author: udo.schroeter@gmail.com
 * Project: Hubbub2
 * Description: some general convenience functions and wrappers
 */
$GLOBALS['config']['service']['url_rewrite'] = true;
define('URL_CA_SEPARATOR', '/');
 
function interpretQueryString()
{
  $uri = parse_url('http://'.$_SERVER['HTTP_HOST'].$_SERVER['REQUEST_URI']);
  $path = '';
  if($uri['query'] != '') 
  {
    parse_str($uri['query'], $_REQUEST_new);
    $_REQUEST = array_merge($_REQUEST, $_REQUEST_new);
    $firstPart = CutSegment('&', $uri['query']);
    if(!$GLOBALS['config']['service']['url_rewrite'] && !inStr($firstPart, '=')) $path = $firstPart;
  }
  if($GLOBALS['config']['service']['url_rewrite'])
  {
    $path = $uri['path'];  
  
    if(substr($path, 0, 1) == '/') $path = substr($path, 1);
    $call = explode(URL_CA_SEPARATOR, $path);
  
    $controllerPart = $call[0];
    $docFilePart = $call[1];
  
    $_REQUEST['p'] = first($controllerPart, 'default');
    $_REQUEST['df'] = first($docFilePart, '');
  
    $_REQUEST['parts'] = $call;
    $_REQUEST['uri'] = $uri;
  }
}

/* inits the profiler that allows performance measurement */
error_reporting(E_ALL ^ E_NOTICE);
// init the profiler timestamp
$GLOBALS['profiler_last'] = first($GLOBALS['profiler_start'], microtime());

/* retrieve a config value (don't use $GLOBALS['config'] directly if possible) */
function cfg($name, $default = null, $set = false)
{
	$vr = &$GLOBALS['config'];
	foreach(explode('/', $name) as $ni) 
	  if(is_array($vr)) $vr = &$vr[$ni]; else $vr = '';
	if($set)
	  $vr = $default; 
	return(first($vr, $default));
}


function formatCode($src)
{
  $src = htmlspecialchars($src);
  $nsrc = '';
  $isInString = false;  
  $isInFunctionHead = false;
  for($a = 0; $a < strlen($src); $a++)
  {
    $c = $src[$a];
    if(!$isInString) switch($c)
    {
      case("'"): 
      {
        $nsrc .= '\'<i style="color:green;font-weight:normal;">';
        $isInString = true; 
        break;
      }
      case('{'):
      {
        $nsrc .= $c;
        $pipePos = strpos($src, '|', $a);
        $closePos = strpos($src, '}', $a);
        if($pipePos < $closePos)
        {
          $isInFunctionHead = true;
          $nsrc .= '<span style="color:blue">';
        }
        break;
      }
      case('|'): 
      {
        $nsrc .= $c;
        if($isInFunctionHead)
          $nsrc .= '</span>';
        break;
      }
      default: 
      {
        $nsrc .= $c; break;
      }
    }
    else
    {
      if($c == "'")
      {
        $nsrc .= '</i>';
        $isInString = false; 
      } 
      $nsrc .= $c;
    }
  } 
  $b = function($c) { return('<b style="color: gray;">'.$c.'</b>'); };
  $a = function($c) { return('<b style="color: blue;">'.$c.'</b>'); };
  ?><pre class="sh_np"><?= str_replace(
    array('(', ')', '{', '}'),
    array($b('('), $b(')'), $b('{'), $b('}'), ),
    $nsrc) ?></pre><?
}

function formatOutput($src)
{
  ?><pre class="op"><?= htmlspecialchars($src) ?></pre><?
}

function displayFunctionHead($fname, $params = array(), $retVals = array())
{
  $GLOBALS['hds'][] = $fname;
  $p = array();
  foreach($params as $prm)
  {
    $p[] = str_ireplace(' or ', ' <span style="color:gray">or</span> ', $prm);
  }
?><a class="nameda" name="<?= $fname ?>"></a><div class="functionhead">
      
      <span class="fname"><?= $fname ?></span>( <span class="farg"><?= implode('&nbsp;&nbsp; ', $p) ?></span> )
      
      <? if(sizeof($retVals) > 0) { ?>
      → <?= implode(', ', $retVals) ?>
      <? } ?>
    
    </div><?
}

function displayVariableHead($fname)
{
  $GLOBALS['hds'][] = $fname;
  ?><a class="nameda" name="<?= $fname ?>"></a><div class="functionhead">
    
      <span class="fname" style="font-style: italic;">&nbsp; <?= $fname ?></span></span> 
    
    </div><?
}

// shorthand function that overcomes a …specialness in the PHP parser 
// -> use this one (it's not quite the same as object() which should go the way of the dodo soon) 
function o($o1 = null, $o2 = null)
{
  if(is_string($o1))
  {
    if($o2 == null)
      return($GLOBALS['obj'][$o1]);
    else
    {
      $GLOBALS['obj'][$o1] = $o2;
      return($o2);
    }
  }
  if(is_object($o1))
  {
    if($o2 == null)
      return($o1);
    else
    {
      $GLOBALS['obj'][$o2] = $o1;
      return($o1);
    }
  }
  return($GLOBALS['obj']['o']);
}

class oEnvironment
{
  function value($key, $value = null)
  {
    if($value)
    {
      $GLOBALS['oval'][$key] = $value;
      return($this);
    }
    else
      return($GLOBALS['oval'][$key]);
  }
}

$GLOBALS['obj']['o'] = o(new oEnvironment());

function sanitizeRequestVariable()
{
  # preventing the "mark of the beast" and other bugs
  foreach($_REQUEST as $k => $v)
  {
    if(is_array($v)) $_REQUEST[$k] = '';
    else if(substr(str_replace('.', '', $v), 0, 8) == '22250738') $_REQUEST[$k] = '('.$v.')';
  }
			
	# forcing magic quotes off for legacy environments, there are still some servers that use it
  if (get_magic_quotes_gpc()) {
    $process = array(&$_GET, &$_POST, &$_COOKIE, &$_REQUEST);
    while (list($key, $val) = each($process)) {
        foreach ($val as $k => $v) {
            unset($process[$key][$k]);
            if (is_array($v)) {
                $process[$key][stripslashes($k)] = $v;
                $process[] = &$process[$key][stripslashes($k)];
            } else {
                $process[$key][stripslashes($k)] = stripslashes($v);
            }
        }
    }
    unset($process);
  }
}

function getFiles($path, $directories = false)
{
  $result = array();
  if ($handle = opendir($path)) {
      while (false !== ($entry = readdir($handle))) {
          if ($entry != "." && $entry != ".." && (!is_dir($path.$entry) || $directories)) {
            $result[] = $entry;
          }
      }
      closedir($handle);
  }
  return($result);
}


function getArrayDump($a)
{
  ob_start();
  print_r($a);
  return(ob_get_clean());
}

/* connect with a memcache server if enabled */
function cache_connect()
{
  if(!cfg('memcache/enabled')) return(false);
  if(o('memcache')) return(true);
  profile_point('cache_connect()');
  // parsing the access URL
  $mcUrl = explode(':', cfg('memcache/server'));
  // connect to server  
  $GLOBALS['errorhandler_ignore'] = true;
  $mc = @memcache_pconnect($mcUrl[0], $mcUrl[1]+0);
  $GLOBALS['errorhandler_ignore'] = false;
  // if this didn't work out:
  if($mc === false)
  {
    $GLOBALS['config']['memcache']['enabled'] = false;
    logError('memcache: could not connect to server '.cfg('memcache/server'));
    return(false);
  }  
  o($mc, 'memcache');  
  return(true);
}

function cache_delete($key)
{
  if(!cache_connect()) return(false);
  return(memcache_delete(o('memcache'), cfg('service/server').'/'.$key));
}

function cache_get($key)
{
  if(!cache_connect()) return(false);
  return(memcache_get(o('memcache'), cfg('service/server').'/'.$key));
}

function cache_set($key, $value)
{
  if(!cache_connect()) return(false);
  $key = cfg('service/server').'/'.$key;
  $op = memcache_replace(o('memcache'), $key, $value, MEMCACHE_COMPRESSED, MEMCACHE_TTL);
  if($op == false)
    memcache_add(o('memcache'), $key, $value, MEMCACHE_COMPRESSED, MEMCACHE_TTL);
}

/* higher order cache function that caches an entire output area */
function cache_region($key, $generateFunction, $ignoreCache = false)
{
  if($ignoreCache)
  {
    $generateFunction();
    return; 
  }
  $out = cache_get($key);
  if($out === false)
  {
    ob_start();
    $generateFunction();
    $out = ob_get_clean();
    cache_set($key, $out);    
  }
  print($out);
}

/* caches a block of data (should be an array) */
function cache_data($key, $generateFunction)
{
  $result = cache_get($key);
  if($result === false)
  {
    $result = $generateFunction();
    cache_set($key, json_encode($result));    
  }
  else
  {
    $result = json_decode($result, true);
  }
  return($result);
}

/* localization function. supply the ID string, it returns the localized string. */
function l10n($s, $silent = false)
{
  $lout = $GLOBALS['l10n'][$s];
  if(isset($lout)) 
    return($lout);
  else if($silent === true)
    return('');
  else
    return('['.$s.']');
}

/* loads a set of localized strings into the current container. this is also called
   automatically whenever a controller is initialized (corresponding l10n files are
   looked for in the controller's directory, having names like "l10n.<lang>.cfg") */ 
function l10n_load($filename_base)
{
  if(isset($GLOBALS['l10n_files'][$filename_base])) return;
  $lang_try = array();
  $usr = o('user');
  if($usr != null) $lang = $usr->lang; 
  if($lang != '') $lang_try[] = $lang;
  $lang_try[] = 'en';
  foreach($lang_try as $ls)
  {
    $lang_file = $filename_base.'.'.$ls.'.cfg';
    if(file_exists($lang_file))
    {
	    foreach(stringsToStringlist(file($lang_file)) as $k => $v) 
	      $GLOBALS['l10n'][$k] = trim($v);
	    $GLOBALS['l10n_files'][$filename_base] = $lang_file;
    }
  }
}

/* makes a commented profiler entry */ 
function profile_point($text)
{
  $thistime = microtime();
  $GLOBALS['profiler_log'][] = profiler_microtime_diff($thistime, $GLOBALS['profiler_start']).' | '.profiler_microtime_diff($thistime, $GLOBALS['profiler_last']).' msec | '.
    ceil(memory_get_usage()/1024).' kB | '.$text;
  $GLOBALS['profiler_last'] = $thistime;
}

/* subtracts to profiler timestamps and returns miliseconds */
function profiler_microtime_diff(&$b, &$a)
{
  list($a_dec, $a_sec) = explode(" ", $a);
  list($b_dec, $b_sec) = explode(" ", $b);
  return number_format(1000*($b_sec - $a_sec + $b_dec - $a_dec), 3);
}

function arrayModifyEntries($action, $array, $entries)
{
  $newArray = array();
  $newEntries = array();
  foreach($entries as $entry) 
    $newEntries[] = trim(strtolower($entry));

  foreach($array as $arrayItem)
  {
    if(!in_array(strtolower($arrayItem), $newEntries)) 
      $newArray[] = strtolower($arrayItem);
  }
  
  if($action == '+') foreach($newEntries as $entry)
    $newArray[] = $entry;

  return($newArray);
}

/* converts a list of config strings into an associative array */
function stringsToStringlist($stringArray)
{
  $result = array();  
  if (is_array($stringArray))
    foreach ($stringArray as $line)
    {
      $key = CutSegment('=', $line);
      if($key == '') 
        $result[$lastKey] .= ($line);
      else if(substr($key, -1) == '+')
        $result[substr($key, 0, -1)] .= ($line);
      else
      {
        $result[$key] = ($line);
        $lastKey = $key;
      }
    }
  return($result);
}

/* this should be part of PHP actually, tests whether one string is inside of another */
function inStr($haystack, $needle)
{
  if(is_array($haystack)) 
  {
    $hs = array();
    foreach($haystack as $k => $v) $hs[] = $v;
    $haystack = implode(' ', $hs);
  }
  return(!(stripos(trim($haystack), trim($needle)) === false));
}

/* another convenience function, test if a string starts with another */
function strStartsWith($haystack, $needle)
{
  return(substr(strtolower($haystack), 0, strlen($needle)) == strtolower($needle));
}

/* test whether a string ends with another */
function strEndsWith($haystack, $needle)
{
  return(substr(strtolower($haystack), -strlen($needle)) == strtolower($needle));
}

/* open new file (overwrite if it already exists) */
function newFile($filename, $content)
{
  if(file_exists($filename)) unlink($filename);
  WriteToFile($filename, $content); 
}

/* append any string to the given file */
function WriteToFile($filename, $content)
{
  $open = fopen($filename, 'a+');
  fwrite($open, $content);
  fclose($open);
}

// standard logging function (please log only to the log/ folder)
// - error logs should begin with the prefix "err."
// - warning logs should begin with the prefix "warn."
// - notice logs should begin with the prefix "notice."
function logToFile($filename, $content)
{
  global $profiler_report, $profiler_time_start, $profiler_last;
  if (!is_array($content)) $content = array('text' => $content);

	$uri = $_SERVER['REQUEST_URI'];
	if(stristr($uri, 'password') != '') $uri = '***';

  $content['remote'] = $_SERVER['REMOTE_ADDR'];
  $content['host'] = $_SERVER['HTTP_HOST'];
  $content['uri'] = $uri;
  $content['session'] = $_SESSION['uid'].'/'.session_id();
  $content['time'] = gmdate('Y-m-d H:i:s');
  $content['exec'] = profiler_microtime_diff(microtime(), $GLOBALS['profiler_start']).'ms';

  @WriteToFile($filename,
    str_replace('\/', '/', json_encode($content))."\r\n\r\n");
}	

/* logs an error, duh */
function logError($msg, $msg2 = '')
{
  if($GLOBALS['nolog']) return;
  $bt = debug_backtrace();
  unset($bt[0]);
  unset($bt[0]);
  $bts['error'] = $msg.' '.$msg2;
  foreach($bt as $s)
    $bts['trace'][] = $s['file'].' line '.$s['line'].' '.$s['function'].'('.(is_string($s['args'][0]) ? '"'.$s['args'][0].'"' : '['.gettype($s['args'][0]).']').')';
  
  logToFile('log/error.log', $bts);
}

$GLOBALS['config']['service']['url_rewrite'] = true;

// fixme: obviously...
function getCurrentProtocol()
{
  return('http'); 
}

/* use this to make a hyperlink that calls a controller with an action */
function hyperlink($caption, $controllerAction, $params = array())
{
  $controller = CutSegment('/', $controllerAction);
  return('<a href="'.actionUrl($controllerAction, $controller, $params).'">'.htmlspecialchars(first(l10n($caption, true), $caption)).'</a>'); 
}

/* makes an URL calling a specific controller with a specific action */
function actionUrl($action = '', $controller = '', $params = array(), $fullUrl = false)
{ 
  $p = '';
  $controller = first($controller, $_REQUEST['controller']);
  if(isset($GLOBALS['subcontrollers'][$controller]))
    $controller = $GLOBALS['subcontrollers'][$controller].URL_CA_SEPARATOR.$controller;
  $action = first($action, $_REQUEST['action']);
  if (!is_array($params)) $params = stringParamsToArray($params);
  if(sizeof($params) > 0) 
  {
    // prevent cookies from appearing in the server log by accident
    foreach(array('session-key', session_id()) as $k) 
      if(isset($params[$k])) unset($params[$k]);
    $pl = http_build_query($params);
    $p = '?'.$pl;
    $pn = '&'.$pl;
  }   
  if($fullUrl)
  {
    $base = cfg('service.base');
    if(trim($base) == '') $base = cfg('service/server');
    if(substr($base, -1) != '/') $base .= '/';
  }
  if(cfg('service/url_rewrite'))
  {
    $url = $controller.($action == 'index' ? '' : URL_CA_SEPARATOR.$action).$p; 
    return($base.$url);
  }
  else 
  {
    $url = '?'.$controller.($action == 'index' ? '' : URL_CA_SEPARATOR.$action).$pn; 
    return(first($base, './').$url);
  }  
}

/* internal function needed to parse parameters in the form of "p1=bla,p2=blub" into a proper array */
function stringParamsToArray($paramStr)
{
  $result = array();
  foreach(explode(',', $paramStr) as $line)
  {
    $k = CutSegment('=', $line);
    $result[$k] = $line;	
  }
  return($result);
}

/* returns the first array that is being passed to it */
function defaultArray()
{
	foreach(func_get_args() as $a)
		if(is_array($a)) return($a);
	return(array());
}

/* returns the first non-null, non-empty variable passed to it */
function first()
{
	foreach(func_get_args() as $a)
		if($a != null && $a != '') return($a);
	return('');
}

/* cut $cake at the first occurence of $segdiv, returns the slice 
   if $segdiv is an array, it will use the first occurence that matches any of its entries */
function CutSegmentEx($segdiv, &$cake, &$found, $params = array())
{
  if(!is_array($segdiv)) $segdiv = array($segdiv);
  $p = false;
  foreach($segdiv as $si)
  {  
    $pi = strpos($cake, $si);
    if($pi !== false && ($pi < $p || $p === false)) 
    {
      $p = $pi;
      $pfirst = $p;
      $slen = strlen($si);
    }
  }
  if ($p === false)
  {
    $result = $cake;
    $cake = '';
    $found = false;
  }
  else
  {
    if($params['full']) $pfirst += $slen;
    $result = substr($cake, 0, $pfirst);
    $cake = substr($cake, $p + $slen);
    $found = true;
  }
  return $result;
}

/* like CutSegmentEx(), but doesn't carry the $found result flag */
function CutSegment($segdiv, &$cake, $params = array())
{
  return(CutSegmentEx($segdiv, $cake, $found, $params));
}

/* converts an assoc array into a list of config strings */
function stringListToStrings($stringList)
{
  $result = array();
  foreach ($stringList as $k => $v)
  {
    if (trim($v) != '' && $k != '')
      $result[] = $k.'='.trim($v);
  }
  return($result);
}

/* issues a HTTP redirect immediately */
function redirect($tourl)
{
  header('X-Redirect-From: '.$_SERVER['REQUEST_URI']);
	header('Location: '.$tourl);
	die();
} 

function file_get_fromurl($url, $post = array(), $timeout = 2)
{
  $fle = cqrequest($url, $post, $timeout);
  return($fle['body']);	
}

/* parses an HTTP result and returns it in a nicely filtered array:
   'length' : the size of the response
   'result' : HTTP numeric result code
   'headers' : array with the response headers
   'data' : if any json data was in the response body
   'body' : the actual response data */
function http_parse_request($result, $headerMode = true)
{
  $resHeaders = array();
  $resBody = array();
  
  foreach(explode("\n", $result) as $line)
  {
    if($headerMode)
    {
      if(strStartsWith($line, 'HTTP/'))
      {
        $httpInfoRecord = explode(' ', trim($line));
        if($httpInfoRecord[1] == '100') $ignoreUntilHTTP = true;
        else 
        {
          $ignoreUntilHTTP = false;
          $resHeaders['code'] = $httpInfoRecord[1];
          $resHeaders['HTTP'] = $line;
        }
      }
      else if(trim($line) == '')
      {
        if(!$ignoreUntilHTTP) $headerMode = false;
      }
      else 
      {
        $hdr_key = trim(CutSegment(':', $line));
        $resHeaders[strtolower($hdr_key)] = trim($line); 
      }
    }
    else
    {
      $resBody[] = $line; 
    }    
  }

  $body = trim(implode("\n", $resBody));
  $data = json_decode($body, true);

  return(array(
    'length' => strlen($body),
    'result' => $resHeaders['code'],
    'headers' => $resHeaders,
    'data' => $data,
    'body' => $body));
}


/* this is for making multiple simultaneous requests, takes an array of URLs instead of only one */
// fixme: should probably be unified with cqrequest()
function cqrequest($rq_array, $post = array(), $timeout = 1, $headerMode = true, $onlyHeaders = false)
{
  $rq = array();
  $content = array();
  $active = null;
  $idx = 0;
  $multi_handler = curl_multi_init();
  
  if(!is_array($rq_array)) $rq_array = array(array('url' => $rq_array));
  
  // configure each request
  foreach($rq_array as $rparam) if(trim($rparam['url']) != '')
  {
    $idx++;
    $channel = curl_init();
    curl_setopt($channel, CURLOPT_URL, $rparam['url']);
    $combinedParams = $post;
    if(is_array($rparam['params'])) $combinedParams = array_merge($rparam['params'], $post);
    if(sizeof($combinedParams)>0) 
    {
      curl_setopt($channel, CURLOPT_POST, 1); 
      curl_setopt($channel, CURLOPT_POSTFIELDS, $combinedParams);
    }
    curl_setopt($channel, CURLOPT_HEADER, 1); 
    curl_setopt($channel, CURLOPT_TIMEOUT, $timeout); 
    curl_setopt($channel, CURLOPT_RETURNTRANSFER, 1);
    curl_multi_add_handle($multi_handler, $channel);
    $rq[$idx] = array($channel, $rparam);
  }
  
  if(sizeof($rq) == 0) return(array());
  
  // execute
  do {
      $mrc = curl_multi_exec($multi_handler, $active);
  } while ($mrc == CURLM_CALL_MULTI_PERFORM);
  
  // wait for return
  while ($active && $mrc == CURLM_OK) {
    if (curl_multi_select($multi_handler) != -1) {
        do {
            $mrc = curl_multi_exec($multi_handler, $active);
        } while ($mrc == CURLM_CALL_MULTI_PERFORM);
    }
  }
  
  // cleanup
  foreach($rq as $idx => $rparam)
  {
    $result = http_parse_request(curl_multi_getcontent($rparam[0]));
    $result['param'] = $rparam[1];
    $content[$idx] = $result;
    curl_multi_remove_handle($multi_handler, $channel);
    $lastIdx = $idx;
  }
  
  curl_multi_close($multi_handler);
  
  if(sizeof($content) == 1) $content = $content[$lastIdx];
  
  return($content);  
}

/* makes a Unix timestamp human-friendly, web-trendy and supercool */
function ageToString($unixDate, $new = 'new', $ago = 'ago')
{
  if($unixDate == 0) return('-');
  $result = '';
  $oneMinute = 60;
  $oneHour = $oneMinute*60;
  $oneDay = $oneHour*24;
    $difference = time() - $unixDate;
  if ($difference < $oneMinute)
    $result = $new;
  else if ($difference < $oneHour)
    $result = round($difference/$oneMinute).' min '.$ago;
  else if ($difference < $oneDay)
    $result = floor($difference/$oneHour).' h '.$ago;
  else if ($difference < $oneDay*5)
    $result = gmdate(first(cfg('service/dateformat-week'), 'l · H:i'), $unixDate);
  else if ($difference < $oneDay*365)
    $result = gmdate(first(cfg('service/dateformat-year'), 'M dS · H:i'), $unixDate);
  else
    $result = date(first(cfg('service/dateformat'), 'd. M Y · H:i'), $unixDate);
  return($result);
}

/* version of strip_tags that kills attributes, since the PHP version is horribly unsafe */
function strip_tags_attributes($string,$allowtags=NULL,$allowattributes=NULL)
{
  $string = strip_tags($string,$allowtags);
  if (!is_null($allowattributes)) {
      if(!is_array($allowattributes))
          $allowattributes = explode(",",$allowattributes);
      if(is_array($allowattributes))
          $allowattributes = implode(")(?<!",$allowattributes);
      if (strlen($allowattributes) > 0)
          $allowattributes = "(?<!".$allowattributes.")";
      $string = preg_replace_callback("/<[^>]*>/i",create_function(
          '$matches',
          'return preg_replace("/ [^ =]*'.$allowattributes.'=(\"[^\"]*\"|\'[^\']*\')/i", "", $matches[0]);'   
      ),$string);
  }
  return $string;
} 
 
function extract_tags_prepare($raw)
{
  $GLOBALS['matches'] = array();
  $GLOBALS['matchprefix'] = h2_make_uid();
	$in = array(
    '`((?:\#)\S+[[:alnum:]]/?)`si',
    );
  return preg_replace_callback($in, function($matches) {
    $GLOBALS['matchcount']++;
    $matchKey = $GLOBALS['matchprefix'].'-'.$GLOBALS['matchcount'];
    $GLOBALS['matches'][$matchKey] = substr($matches[0], 1);
    return($matchKey);
    }, $raw);   
}

function getTagLink($postKey, $tagName)
{
  $tagName = htmlspecialchars($tagName);
  return('<a onclick="H2HandleTag($(this), '.$postKey.', \''.$tagName.'\', \''.getTagClass($tagName).'\');" ><span class="ptaglink">#</span>'.$tagName.'</a>'); 
}

function extract_tags_execute($url, $postKey)
{
  foreach($GLOBALS['matches'] as $k => $match)
  {
    $match = htmlspecialchars($match);
    $url = str_replace($k, getTagLink($postKey, $match), $url);
  }
  return($url); 
}

function getTagClass($tagName)
{
  return('tc_'.substr(md5($tagName), 0, 8)); 
}
 
// FIXME: horrible...
function make_clickable_prepare($url) {
  $GLOBALS['matches'] = array();
  $GLOBALS['matchprefix'] = h2_make_uid();
	$in = array(
    '`((?:https?|ftp)://\S+[[:alnum:]]/?)`si',
    '`((?<!//)(www\.\S+[[:alnum:]]/?))`si',
    );
  return preg_replace_callback($in, function($matches) {
    $GLOBALS['matchcount']++;
    $matchKey = $GLOBALS['matchprefix'].'-'.$GLOBALS['matchcount'];
    $GLOBALS['matches'][$matchKey] = $matches[0];
    return($matchKey);
    }, $url); 
}

function make_clickable_execute($url)
{
  foreach($GLOBALS['matches'] as $k => $match)
  {
    $p = parse_url($match);
    $u = $p['host'].$p['path'].($p['query'] ? '?'.$p['query'] : '');
    $v = first($p['scheme'], 'http').'://'.$p['host'].($p['path'].$p['query'] != '/' ? $p['path'] : '').($p['query'] ? '?'.$p['query'] : '');
    $url = str_replace($k, '<a href="'.$v.'" rel="nofollow" target="_blank">'.$u.'</a>', $url);
  }
  return($url); 
}

?>
