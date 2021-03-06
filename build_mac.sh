#!/bin/bash
cd "$(dirname "$0")"

BUILDMODE=${2:-"debug"}
OPT_FLAG="O3"

if [ "$BUILDMODE" == "debug" ]; then
	OPT_FLAG="O0"
fi
echo "Build mode: $BUILDMODE"

BINF=${1:-"cli"}

mkdir bin > /dev/null 2>&1
mkdir bin/tmp > /dev/null 2>&1

COMPILER="clang++"
FLAGS="-w -Wall -$OPT_FLAG -std=c++14 -fpermissive -ffast-math"

LIBS="-ldl -lm -lpthread"
SRCFLAGS="-D EXEC_NAME=\"$BINF\" -D PLATFORM_NAME=\"linux\""

time -p $COMPILER src/$BINF.cpp $SRCFLAGS $FLAGS $LIBS -o bin/$BINF.$BUILDMODE.linux.bin 

ls -lh bin/ | grep $BINF

