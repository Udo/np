#include "language/types.h"
#include "language/io.h"
#include "language/tokenizer.h"
#include "language/parser.h"

string src;

int main(int argc, char** argv) 
{
	if(argc >= 2)
	{
		src = read_text_file(argv[1]);
		auto token_list = tokenize(src);
		//token_list->print(true);
		//printf("\n");
		Parser p;
		p.parse(token_list);
		p.ast_root->print(true);
	}
	printf("\n");
	return 0;
}
