echo "Node:"
time node bench.js
echo "Lua:"
time lua bench.lua
echo "LuaJIT:"
time luajit bench.lua
echo "np:"
time ../bin/np bench.np
