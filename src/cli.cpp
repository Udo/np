#include "language/types.h"
#include "language/io.h"
#include "language/tokenizer.h"

string src;

int main(int argc, char** argv) 
{
	if(argc >= 1)
	{
		src = read_text_file(argv[1]);
		auto token_list = tokenize(src);
		token_list->print(true);
	}
	return 0;
}