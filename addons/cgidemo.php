<?php

header('content-type: text/html; charset=utf8');

if(!isset($_REQUEST['code'])) $_REQUEST['code'] = '';
if(!isset($out)) $out = '[no output]';

function exec_timeout($cmd, $timeout) {
  // File descriptors passed to the process.
  $descriptors = array(
    0 => array('pipe', 'r'),  // stdin
    1 => array('pipe', 'w'),  // stdout
    2 => array('pipe', 'w')   // stderr
  );

  // Start the process.
  $process = proc_open('exec ' . $cmd, $descriptors, $pipes);

  if (!is_resource($process)) {
    throw new \Exception('Could not execute process');
  }

  // Set the stdout stream to none-blocking.
  stream_set_blocking($pipes[1], 0);

  // Turn the timeout into microseconds.
  $timeout = $timeout * 1000000;

  // Output buffer.
  $buffer = '';

  // While we have time to wait.
  while ($timeout > 0) {
    $start = microtime(true);

    // Wait until we have output or the timer expired.
    $read  = array($pipes[1]);
    $other = array();
    stream_select($read, $other, $other, 0, $timeout);

    // Get the status of the process.
    // Do this before we read from the stream,
    // this way we can't lose the last bit of output if the process dies between these functions.
    $status = proc_get_status($process);

    // Read the contents from the buffer.
    // This function will always return immediately as the stream is none-blocking.
    $buffer .= stream_get_contents($pipes[1]);

    if (!$status['running']) {
      // Break from this loop if the process exited before the timeout.
      break;
    }

    // Subtract the number of microseconds that we waited.
    $timeout -= (microtime(true) - $start) * 1000000;
  }

  // Check if there were any errors.
  $errors = stream_get_contents($pipes[2]);

  if (!empty($errors)) {
    $delim = '.np:';
    $dlPos = strpos($errors, $delim);
    $buffer .= substr($errors, $dlPos+4);
  }

  // Kill the process in case the timeout expired and it's still running.
  // If the process already exited this won't do anything.
  proc_terminate($process, 9);

  // Close all streams.
  fclose($pipes[0]);
  fclose($pipes[1]);
  fclose($pipes[2]);

  proc_close($process);

  return $buffer;
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
  $out = exec_timeout('timeout --preserve-status 0.01s ../bin/np '.$tmpFile, 1);
  unlink($tmpFile);
}

?>

<pre id="out"><?= htmlspecialchars($out) ?></pre>

<style>
  form {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	background: #eee;
  }

  #out {
  	margin-top: 290px;
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
