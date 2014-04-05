console.log("---------------- JavaScript test script --------------------");

var tbl = { };
var i = 0;

while(i < 10000000) {
  tbl.last = i
  i +=1
}

console.log("end JavaScript loop benchmark "+i+" iterations");