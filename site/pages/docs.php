<?

$GLOBALS['title'] = '::Documentation';


function displayHeader($info)
{
  if(!is_array($info['params'])) $info['params'] = array();
  if(!is_array($info['nparams'])) $info['nparams'] = array();
  asort($info['nparams']);
  if(!is_array($info['tags'])) $info['tags'] = array();
  $GLOBALS['title'] = ''.$info['title'];
  ?><div class="info"><?= $info['type'] ?> | <?= implode(' &middot; ', $info['tags']) ?></div><?
  if($info['type'] == 'function')
  {
    displayFunctionHead($info['title'], array_keys($info['params']));

    ?>    
    <table width="100%"><tr><td valign="top" width="50%">
    
    <div class="minihdr">Parameters:</div>
    <table width="100%" class="paramdescription">
    
      <? foreach($info['params'] as $k => $v)
      {
        ?><tr>
        
          <td width="12%"><span class="farg"><?= $k ?></span></td>
          <td><?= $v ?></td>
        
        </tr><?
      }     
      ?>
    
    </table></td><td valign="top">
    
    <div class="minihdr">Optional named parameters:</div>
    <table width="100%" class="paramdescription"><? 
    
      if(sizeof($info['nparams']) == 0)
        $info['nparams'][' '] = '-';
    
      foreach($info['nparams'] as $k => $v)
      {
        ?><tr>
        
          <td width="12%"><span class="farg"><?= trim($k) != '' ? '#' : '' ?><?= $k ?></span></td>
          <td><?= $v ?></td>
        
        </tr><?
      }     
      ?>
    
    </table>
    
    </td></tr></table><?
  }
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


