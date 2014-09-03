<?php

header('content-type: text/html; charset=utf8');

if(!isset($_REQUEST['code'])) $_REQUEST['code'] = '';
if(!isset($out)) $out = '[no output]';

function cmd_exec($cmd, &$stdout, &$stderr)
{
    $outfile = tempnam(".", "cmd");
    $errfile = tempnam(".", "cmd");
    $descriptorspec = array(
        0 => array("pipe", "r"),
        1 => array("file", $outfile, "w"),
        2 => array("file", $errfile, "w")
    );
    $proc = proc_open($cmd, $descriptorspec, $pipes);
   
    if (!is_resource($proc)) return 255;

    fclose($pipes[0]);    //Don't really want to give any input

    $exit = proc_close($proc);
    $stdout = file_get_contents($outfile);
    $stderr = file_get_contents($errfile);

    unlink($outfile);
    unlink($errfile);
    return $exit;
}

?>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>np</title>
        <meta name="description" content="">
        <meta name="viewport" content="width=1024">
        <base href="http://np-lang.org/">
        <link rel="stylesheet" href="/css/">
        <link rel="icon" type="image/png" href="favicon.png">
        <script src="/js/"></script>
    </head>
    <body><article style="text-align: left;">
<form method="post" action="/invoke/cgidemo.php">

  <textarea class="pre" name="code" id="code" 
    autocapitalize="off" autocorrect="off" autocomplete="off"  spellcheck="false"
    placeholder="Your np code here"><?= htmlspecialchars($_REQUEST['code']) ?></textarea>
  <input type="submit" value="⇲ Run"/>
</form>
<h3>Output:</h3>


<?php

if($_REQUEST['code'])
{
  $tmpFile = '../tmp/'.md5(time().mt_rand(1, 1000000)).'.np';
  file_put_contents($tmpFile, '
root.sys = nil
root.lib = nil
root.io = nil
root.os = nil
root.require = nil

'.file_get_contents('cgidemo-examplestate.np').chr(10).$_REQUEST['code']);
  $err = '';
  cmd_exec('timeout --preserve-status 0.01s /srv/www/htdocs/openfu.com/dev/np/bin/np '.$tmpFile, $out, $err);
  if($err != '')
    $out = substr($err, strpos($err, '.np:')+4);
  #$out = shell_exec('whoami');
  unlink($tmpFile);
}

$b1 = '#fcfcfc';
$b2 = '#f8f8f8';

?>

<pre id="out"><?= htmlspecialchars($out) ?></pre>

<style>

  pre {
    margin-top:-12px;
    padding:0;
  }

  textarea, input[type=submit] {
    border: 1px solid rgba(0,0,0,0.1);
    background: rgba(0,0,50,0.03);
  }

  textarea {
    background: -webkit-linear-gradient(<?= $b1 ?>, <?= $b2 ?>); /* For Safari 5.1 to 6.0 */
    background: -o-linear-gradient(<?= $b1 ?>, <?= $b2 ?>); /* For Opera 11.1 to 12.0 */
    background: -moz-linear-gradient(<?= $b1 ?>, <?= $b2 ?>); /* For Firefox 3.6 to 15 */
    background: linear-gradient(<?= $b1 ?>, <?= $b2 ?>); /* Standard syntax */
    box-sizing: border-box;
    width: 100%;
    padding: 4px;
    font-size: 12px;
    min-height: 200px;
    padding-top: 12px;
    padding-bottom: 12px;
  }
  
  input[type=submit] {
    padding: 4px;
    color: black;
    min-width: 80px;
    cursor: pointer;
    margin-top: -1px;
  }

  input[type=submit]:hover {
    background: #8ea;
  }

</style>
    </article></body></html>
