
string read_text_file(string file_name)
{
	std::ifstream t(file_name);
	std::string str;
	printf("- loading \u001b[34m%s\u001b[0m\n", file_name.c_str());

	try
	{
		t.seekg(0, std::ios::end);   
		str.reserve(t.tellg());
		t.seekg(0, std::ios::beg);

		str.assign((std::istreambuf_iterator<char>(t)),
		            std::istreambuf_iterator<char>());	
	}
	catch(...)
	{
		printf("\u001b[31m! error loading \u001b[34m%s\u001b[0m\n", file_name.c_str());
	}
	
	return(str);
}
