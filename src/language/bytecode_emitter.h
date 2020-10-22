struct BytecodeEmitter
{

	void emit(string OP, string p1 = "")
	{
		printf("%s\t%s\n", OP.c_str(), p1.c_str());
	}

	void compile(ASTNode* node)
	{

	}

};
