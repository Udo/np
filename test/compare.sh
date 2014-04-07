cd "$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
echo "Ruby Interpreter:"
time ruby compare-loop.rb
echo "Ruby times():"
time ruby compare-loop.2.rb
echo "PHP Interpreter:"
time php -f compare-loop.php
echo "JavaScript JIT:"
time node compare-loop.js
echo "Lua Interpreter:"
time lua compare-loop.lua
echo "Lua JIT:"
time luajit compare-loop.lua
echo "np Interpreter:"
time ../bin/np compare-loop.np
