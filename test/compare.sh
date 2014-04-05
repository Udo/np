cd "$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
time ruby compare-loop.rb
time php -f compare-loop.php
time node compare-loop.js
time ../bin/np compare-loop.np
