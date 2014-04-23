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

<form method="post" action="cgidemo.php">

  <textarea name="code" id="code" placeholder="Your np code here"><?= htmlspecialchars($_REQUEST['code']) ?></textarea>
  <input type="submit" value="⇲ Run"/>

<h3>Output:</h3>
</form>

<?php

if($_REQUEST['code'])
{
  $tmpFile = '../tmp/'.md5(time().mt_rand(1, 1000000)).'.np';
  file_put_contents($tmpFile, '
root.sys = nil
root.lib = nil
root.io = nil
root.os = nil

'.file_get_contents('cgidemo-examplestate.np').chr(10).$_REQUEST['code']);
  $err = '';
  cmd_exec('timeout --preserve-status 0.01s /srv/www/htdocs/openfu.com/dev/np/bin/np '.$tmpFile, $out, $err);
  if($err != '')
    $out = substr($err, strpos($err, '.np:')+4);
  #$out = shell_exec('whoami');
  #unlink($tmpFile);
}

?>

<pre id="out"><?= htmlspecialchars($out) ?></pre>

<style>
  form {

  }

  #out {
    width: 100%;
  }

  * {
    font-family: Tahoma, Helvetica;
  }

  textarea {
    border: 1px solid #369;
    width: 99%;
    padding: 4px;
    font-family: Consolas, monospace;
    background: #68c;
    color: white;
    font-size: 12px;
    line-height: 130%;
    min-height: 200px;
  }
  
  input[type=submit] {
    border: 1px solid #369;
    padding: 4px;
    color: black;
    background: #6c8;
    min-width: 80px;
    cursor: pointer;
  }

  input[type=submit]:hover {
    background: #8ea;
  }

</style>
