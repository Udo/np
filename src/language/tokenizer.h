#include <cctype>

enum TokenType {
	TNONE, TIDENTIFIER, TPUNCT, TCOMMENT,
	TSTRINGLITERAL, TEXPRESSION, TSTATEMENT, TBLOCK,
	TDECLARATION, TASSIGNMENT, TTYPE,
};

char* TokenTypeNames[] = {
	"None", "Ident", "Punct", "Comment",
	"String", "Expr", "Stmt", "Block",
	"Decl", "Assign", "Type"
};

struct Token
{
	int type;
	int start;
	int col;
	int line;
	string text;
	Token* next = 0;
	Token* child = 0;
	Token* parent = 0;
	char delim = 0;
	bool is_closing = false;

	void print(bool all = false, string level = "")
	{
		if(this)
			printf("%s\u001b[32m%s \u001b[34m%i:%i \u001b[33m%s\u001b[0m\n", level.c_str(),
				TokenTypeNames[this->type],
				this->col,
				this->line,
				this->text.c_str());
		else
			return;
		if(this->child) this->child->print(true, level+"  ");
		if(all && this->next) this->next->print(true, level);
	}

	void copy_from(Token* from)
	{
		this->type = from->type;
		this->start = from->start;
		this->col = from->col;
		this->line = from->line;
		this->text = from->text;
		this->delim = from->delim;
	}

	void append_child(Token* c)
	{
		if(!this->child)
		{
			this->child = c;
		}
		else
		{
			auto tn = this->child;
			while(tn->next) tn = tn->next;
			tn->next = c;
		}
	}

};

Token* tokenize(string src)
{
	Token* token_list = new Token();
	Token* current_token = token_list;
	int len = src.length();
	int col_ctr = 0;
	int line_ctr = 1;
	for(int i = 0; i < len; i++)
	{
		char c = src[i];
		char nc = src[i+1];
		int cmode = TNONE;
		char delim = 0;
		if(isalnum(c)) cmode = TIDENTIFIER;
		if(ispunct(c)) cmode = TPUNCT;
		if(c == '_') cmode = TIDENTIFIER;
		if(c == '/' && nc == '/') cmode = TCOMMENT;
		if(current_token->type == TCOMMENT)
		{
			if(c != '\n') cmode = TCOMMENT;
		}
		else if(current_token->type == TSTRINGLITERAL)
		{
			if(c != current_token->delim) cmode = TSTRINGLITERAL;
			else cmode = TNONE;
		}
		else
		{
			if(c == '"' || c == '\'')
			{
				delim = c;
				c = 0;
				cmode = TSTRINGLITERAL;
			}
		}

		col_ctr++;
		if(c == '\n')
		{
			col_ctr = 0;
			line_ctr += 1;
		}

		if((current_token->type != TNONE) && (cmode != current_token->type || cmode == TPUNCT))
		{
			Token* next_token = new Token();
			current_token->next = next_token;
			current_token = next_token;
			current_token->start = i;
		}

		if(cmode != TNONE)
		{
			current_token->type = cmode;
			if(c > 0) current_token->text += c;
			if(delim > 0) current_token->delim = delim;
			if(!current_token->line)
			{
				current_token->col = col_ctr;
				current_token->line = line_ctr;
			}
		}

		if(current_token->type == TPUNCT)
		{
			current_token->is_closing = (current_token->text == ")" ||
				current_token->text == "}" ||
				current_token->text == "]" ||
				current_token->text == ";");
		}

	}
	return(token_list);
}
