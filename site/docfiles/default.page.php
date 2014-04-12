<?php

$docFiles = json_decode(file_get_contents('docs.json'), true);

$typeDisplay = array(
  'function' => '&#131;',
  'object' => 'o',
  );

?>

<table width="100%">
  <tr>
    <td valign="top" width="50">
    
      <h3>Objects</h3>

      <ul>
      <?
      
      foreach($docFiles as $f) if($f['info']['type'] == 'object')
      {
        $url = '/docs/'.urlencode($f['file']);
        ?><li>
        
          <div>
            <a href="<?= $url ?>"><?= $f['info']['title'] ?></a> <span style="color:gray"><?= $typeDisplay[$f['info']['type']] ?></span>
          </div>
          
          <div class="col-3">
          <? if(sizeof($f['members']) > 0) foreach($f['members'] as $m) {
          
            ?><div><a class="dea" href="<?= $url ?>#<?= $m ?>">.<?= $m ?></a></div><?
          
          } ?>
          </div>
          
        </li><?
      }
      
      ?>
      </ul>
    
    </td>  
    <td valign="top" width="50">

      <h3>Functions</h3>

      <ul>
      <?
      
      foreach($docFiles as $f) if($f['info']['type'] == 'function')
      {
        ?><li><a href="/docs/<?= urlencode($f['file']) ?>"><?= $f['info']['title'] ?></a> <span style="color:gray"><?= $typeDisplay[$f['info']['type']] ?></span></li><?
      }
      
      ?>
      </ul>
    
    </td>  
  </tr>
</table>

