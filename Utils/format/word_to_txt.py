import docx2txt

# 读取Word文档
text = docx2txt.process("103.docx")

# 将文本按行分割成字符串列表
lines = text.splitlines()


# 去掉每行开头和结尾的空格和制表符
stripped_lines = [line.strip() for line in lines]

# 去除空字符串
new_list = [x for x in stripped_lines if x]

# 输出新列表
print(new_list)