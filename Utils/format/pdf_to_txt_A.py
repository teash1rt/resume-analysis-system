from PyPDF2 import PdfReader


def pdf_to_word(pdf_path):
    pdf_file = open(pdf_path, 'rb')
    pdf_reader = PdfReader(pdf_file)
    result = []
    for page in pdf_reader.pages:
        text = page.extract_text()
        paragraphs = text.split('\n')
        for paragraph in paragraphs:
            if paragraph.strip():
                result.append(paragraph.strip())
        # result.append('\n')
    pdf_file.close()
    return result


# 调用函数获取转换后的内容
content = pdf_to_word('aa.pdf')

# 对转换后的内容进行操作
for line in content:
    # 在这里对每一行进行操作，比如打印出来
    print(line)
