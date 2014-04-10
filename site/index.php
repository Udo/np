<?php

ob_start("ob_gzhandler");

include('lib/genlib.php');
interpretQueryString();
$GLOBALS['title'] = 'np';

ob_start();

$pageFN = 'pages/'.basename(first($_REQUEST['p'], 'default')).'.php';
if(file_exists($pageFN))
  include($pageFN);
else
  include('pages/error.404.php');

$content = ob_get_clean();

include('theme/page.php');

WriteToFile('../visitor.log', date('Y-m-d').' ref='.$_SERVER['HTTP_REFERER'].' ip='.$_SERVER['REMOTE_ADDR'].' uri='.$_SERVER['DOCUMENT_URI'].chr(10).chr(13));
?>