from pdfminer.high_level import extract_text

def pdf_to_text(pdf_path):
    text = extract_text(pdf_path)
    result = []
    for line in text.split('\n'):
        if line.strip():
            result.append(line.strip())
    return result

# 调用函数获取转换后的内容
print(pdf_to_text('12.pdf'))

