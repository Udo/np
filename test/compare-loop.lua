
local tbl = {}
local i = 0        -- local to the chunk
local x = 0

while i < 10000000 do
  tbl.last = i*i*i
  i = i + 1
end

