# prepare inline code files
xxd -c 24 -i src/inline/list_tostring.np > src/inline/list_tostring.h
xxd -c 24 -i src/inline/json_encode.np > src/inline/json_encode.h
xxd -c 24 -i src/inline/json_decode.np > src/inline/json_decode.h
xxd -c 24 -i src/inline/to_querystring.np > src/inline/to_querystring.h
# edit inline code
perl -p -i -e 's/unsigned/const/g' src/inline/*.h
perl -p -i -e 's/}/\,0x00}/g' src/inline/*.h
# start build
OS=`uname`
if [[ "$OS" == 'Darwin' ]]; then
    OS="macosx"
elif [[ "$OS" == 'Linux' ]]; then
    OS="linux"
fi
echo "compiling for $OS"
make clean;
make $OS;
mv src/np bin/
mv src/npc bin/
