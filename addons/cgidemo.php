<?php

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
    throw new \Exception($errors);
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

  <textarea name="code" id="code" placeholder="Your np code here"><?= htmlspecialchars($_POST['code']) ?></textarea>
  <input type="submit" value="Run"/>

</form>

<?php

if($_POST['code'])
{
  $tmpFile = '../tmp/'.md5(time().mt_rand(1, 1000000)).'np';
  $npf = 'root.require = false
root.io = false
root.os = false
root.reflect = false

'.chr(10).$_POST['code'];
  //exec_timeout(, )
}


?>

<style>

  textarea {
    width: 99%;
    padding: 4px;
    font-family: Consolas, monospace;
    background: #9af;
    color: white;
    font-size: 12px;
    line-height: 130%;
    min-height: 200px;
  }

</style>