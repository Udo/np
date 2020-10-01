#!/bin/bash
cd "$(dirname "$0")"

BUILDMODE=${2:-"debug"}
OPT_FLAG="O2"

if [ "$BUILDMODE" == "debug" ]; then
	OPT_FLAG="O0"
fi
echo "Build mode: $BUILDMODE"

BINF=${1:-"cli"}

mkdir bin > /dev/null 2>&1
mkdir bin/tmp > /dev/null 2>&1

COMPILER="clang++"
FLAGS="-w -Wall -$OPT_FLAG -std=c++14 -fpermissive -ffast-math"

LIBS="-ldl -lm -lpthread "
SRCFLAGS="-D EXEC_NAME=\"$BINF\" -D PLATFORM_NAME=\"linux\""

time $COMPILER src/$BINF.cpp $SRCFLAGS $FLAGS $LIBS -o bin/$BINF.$BUILDMODE.linux.bin

if [ $? -eq 0 ]
then
	ls -lh bin/ | grep $BINF
	exit 0
else
	exit 1
fi



