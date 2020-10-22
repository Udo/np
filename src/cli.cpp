#include "language/types.h"
#include "language/io.h"
#include "language/tokenizer.h"
#include "language/parser.h"
#include "language/bytecode_emitter.h"
#include "language/resolver.h"

string src;
std::map<string, string> args;

void get_args(int argc, char** argv)
{
	for(int i = 0; i < argc; i++)
	{
		args[argv[i]] = "true";
	}
}

int main(int argc, char** argv)
{
	get_args(argc, argv);
	if(argc >= 2)
	{
		src = read_text_file(argv[1]);
		auto token_list = tokenize(src);
		//token_list->print(true);
		//printf("\n");
		Parser p;
		p.parse(token_list);
		Resolver r;
		r.init_scope();
		r.default_scope();
		r.resolve_all(p.ast_root);
		if(args["debug_ast"] != "")
		{
			p.ast_root->print();
		}
		else
		{
			BytecodeEmitter b;
			b.compile(p.ast_root);
		}
	}
	printf("\n");
	return 0;
}
