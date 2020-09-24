#include "language/types.h"
#include "language/io.h"
#include "language/tokenizer.h"
#include "language/parser.h"

string src;

int main(int argc, char** argv) 
{
	if(argc >= 1)
	{
		src = read_text_file(argv[1]);
		auto token_list = tokenize(src);
		token_list->print(true);
		printf("\n");
		auto ast = parse(token_list);
		ast->print(true);
	}
	printf("\n");
	return 0;
}