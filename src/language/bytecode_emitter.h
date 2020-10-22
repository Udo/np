struct BytecodeEmitter
{

	void emit(string OP, string p1 = "")
	{
		printf("%s\t%s\n", OP.c_str(), p1.c_str());
	}

	void compile(ASTNode* node)
	{
		switch(node->tags[0])
		{
			case(TUNIT):
			{
				compile(node->child);
			} break;
			case(TBLOCK):
			{
				auto n = node->child;
				while(n)
				{
					compile(n);
					n = n->next;
				}
			} break;
			case(TDECLARATION):
			{
				emit("DECLARE", node->child->literal);
			} break;
			case(TASSIGNMENT):
			{
				emit("ASSIGN", node->child->literal);
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

			} break;
			case(TCALL):
			{

			} break;
		}
	}

};
