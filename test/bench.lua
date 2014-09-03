local cList = {}

for a = 1, 100000 do
	local newHash = { test = a }
	newHash.test2 = a*a
	table.insert(cList, newHash)
end

--print(table.getn(cList))