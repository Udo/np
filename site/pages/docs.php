<?

$GLOBALS['title'] = '::Documentation';


function displayHeader($info)
{
  if(!is_array($info['params'])) $info['params'] = array();
  if(!is_array($info['nparams'])) $info['nparams'] = array();
  asort($info['nparams']);
  if(!is_array($info['tags'])) $info['tags'] = array();
  $GLOBALS['title'] = ''.$info['title'].' ('.$info['type'].') ';
  ?><div class="info"><?= implode(' &middot; ', $info['tags']) ?></div><?
}

$article = first($_REQUEST['df'], 'default.page');

ob_start();
@include('docfiles/'.$article.'.php');
$description = ob_get_clean();

if(sizeof($info) > 0)
  displayHeader($info);
  
?><div class="seealso">
<? if(sizeof($info['see']) > 0) {

  print('See also: ');
  
  asort($info['see']);
  
  foreach($info['see'] as $se)
  {
    $tse = str_replace(array('.o'), array(' object'), $se);
    $see[] = '<a href="/docs/'.urlencode($se).'">'.$tse.'</a>';
  }
    
  print(implode(' &middot; ', $see));

} ?>
</div>

<div class="docbody"><?= $description ?></div>


