<?php

function listDir($dirpath)
{
  $result = array();
  if ($handle = opendir($dirpath)) {
    while (false !== ($entry = readdir($handle))) {
        if ($entry != "." && $entry != "..") {
            $result[] = $entry;
        }
    }
    closedir($handle);
  }
  natcasesort($result);
  return $result;
}

$docFiles = array();

foreach(listDir('docfiles/') as $fle) if(substr($fle, -9) != '.page.php')
{
  print('<div>'.$fle.'</div>');
  ob_start();
  $GLOBALS['hds'] = array();
  include('docfiles/'.$fle);
  ob_get_clean();
  sort($GLOBALS['hds']);
  $docFiles[] = array(
    'file' => substr($fle, 0, -4),
    'info' => $info,
    'members' => $GLOBALS['hds'],
    );
}

file_put_contents('docs.json', json_encode($docFiles));

?>