const mammoth = require('mammoth')
const { parse } = require('node-html-parser')

async function renderDocx(docxPath) {
    const result = await mammoth.convertToHtml({ path: docxPath })
    // 生成对应的html代码
    const html = result.value
    const root = parse(html) // 解析HTML为根节点对象
    // const topLevelElements = root.childNodes.filter(node => node.nodeType === 1) // 选择顶层元素节点

    // const text = topLevelElements.map(node => node.text).join('\n'); // 将所有文本内容拼接为一个字符串，并用换行符分隔

    // console.log(text)
    const pattern = /<.*?>(.*?)<\/.*?>/g
    const matches = html.match(pattern)

    // 脱去html标签并换行，然后生成一个数组并去除全为空格的元素
    text = matches.map(match => match.replace(/<\/?[^>]+>/g, '')).join('\n').split('\n').filter(str => str.trim().length > 0)
    // 把字符串中所有长度大于1的连续空格缩短为长度为1的空格
    text = text.map(e => e.replace(/ {2,}/g, ' '))

    console.log(text)
}

path = '279.docx'

renderDocx(path)