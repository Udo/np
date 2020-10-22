struct BytecodeEmitter
{

	void emit(string OP, string p1 = "")
	{
		printf("%s\t%s\n", OP.c_str(), p1.c_str());
	}

	void compile(ASTNode* node)
	{
		if(node->scope)
		{
			for(auto si : node->scope->items)
				emit("SCOP", si.first);
		}
		switch(node->tags[0])
		{
			case(TUNIT):
			{
				compile(node->child);
			} break;
			case(TBLOCK):
			{
				emit("BLOC");
				auto n = node->child;
				while(n)
				{
					compile(n);
					n = n->next;
				}
			} break;
			case(TDECLARATION):
			{
				emit("DECL", node->child->literal);
			} break;
			case(TASSIGNMENT):
			{
				emit("ASSI", node->child->literal);
			} break;
			case(TEXPRESSION):
			{
				auto n = node->child;
				while(n)
				{
					compile(n);
					n = n->next;
				}
			} break;
			case(TIF):
			{
				emit("IFCL");
			} break;
			case(TCALL):
			{
				emit("CALL");
			} break;
		}
	}

};
