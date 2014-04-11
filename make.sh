OS=`uname`
if [[ "$OS" == 'Darwin' ]]; then
    OS="macosx"
fi
echo "compiling for $OS"
make clean;
make $OS;
mv src/np bin/
mv src/npc bin/
