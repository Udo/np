<?php

$docFiles = json_decode(file_get_contents('docs.json'), true);

$typeDisplay = array(
  'function' => '&#131;',
  'object' => 'lib',
  );

function displayDocs($docFiles, $type, $title)
{
if(!is_array($type)) $type = array($type);
?><h3><?= $title ?></h3>

  <ul>
  <?
  
  foreach($docFiles as $f) if(array_search($f['info']['type'], $type) !== false)
  {
    $url = '/docs/'.urlencode($f['file']);
    ?><li>
    
      <div>
        <a href="<?= $url ?>"><?= $f['info']['title'] ?> 
        <span style="color:rgba(0,0,0,0.5);"><?= $f['info']['type'] ?></span></a>
      </div>
      
      <div class="col-5">
      <? if(sizeof($f['members']) > 0) foreach($f['members'] as $m) {
      
        ?><div><a class="dea" href="<?= $url ?>#<?= $m ?>"><?= $m ?></a></div><?
      
      } ?>
      </div>
      
    </li><?
  }
  
  ?>
  </ul><?
}


        
displayDocs($docFiles, array('type', 'concept', 'statement'), 'Language');

displayDocs($docFiles, 'lib', 'Libraries and Behaviors');
        

