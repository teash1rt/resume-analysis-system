import io
from pdfminer.high_level import extract_text
# import httpx
import docx2txt

# 获取 docx 文件内容
# async def get_docx_content(file):
#     # url = 'http://127.0.0.1:3010/analysis-docx-file/'
#     async with httpx.AsyncClient() as client:
#         response = await client.post(url, files={"file": file.file})
#         if response.status_code != 400:
#             return eval(response.text)
#         else:
#             raise Exception


async def get_docx_content(file):
    with io.BytesIO(await file.read()) as stream:
        text = docx2txt.process(stream)
    lines = text.splitlines()
    stripped_lines = [line.strip('\t').replace('\t', ' ') for line in lines]
    new_list = [x for x in stripped_lines if x.strip() != '']
    return new_list


# 获取 pdf 文件内容
async def get_pdf_content(file):
    with io.BytesIO(await file.read()) as stream:
        text = extract_text(stream)
    result = [line.strip() for line in text.split('\n') if line.strip()]
    return [text.strip() for text in result]


# 获取 txt 文件内容
async def get_txt_content(file):
    text = await file.read()
    lines = text.decode().splitlines()
    stripped_lines = [line.strip('\t').replace('\t', ' ') for line in lines]
    new_list = [x for x in stripped_lines if x.strip() != '']
    return new_list
