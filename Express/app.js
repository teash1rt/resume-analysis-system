const mammoth = require('mammoth')
const express = require('express')
const multer = require('multer')
const path = require('path')
const fs = require('fs')

const app = express()
const upload = multer({ dest: 'uploads/' })
const cors = require('cors')
// 跨域
app.use(cors())

let text = []

app.post('/analysis-docx-file/', upload.single('file'), (req, res) => {
    const { path: filePath } = req.file
    const fullPath = path.resolve(filePath)
    // 将上传的文件重命名为 .docx 格式
    const newFilePath = path.join(`${fullPath.replace(/\d+/g, '')}.docx`)

    fs.renameSync(fullPath, newFilePath)

    renderDocx(newFilePath, res)
        .then(() => {
            res.status(200).send(text)
            del_file(newFilePath)
        })
        .catch(err => {
            console.error(err)
            res.status(400).send('文件格式错误')
        })
})

const port = 3010
app.listen(port, () => {
    console.log(`deployment successfully on port ${port}`)
})

async function renderDocx(docxPath, res) {
    const result = await mammoth.convertToHtml({ path: docxPath })
    // 生成对应的html代码
    const html = result.value

    const pattern = /<.*?>(.*?)<\/.*?>/g
    const matches = html.match(pattern)

    // 脱去html标签并换行，然后生成一个数组并去除全为空格的元素
    text = matches
        .map(match => match.replace(/<\/?[^>]+>/g, ''))
        .join('\n')
        .split('\n')
        .filter(str => str.trim().length > 0)
    // 把字符串中所有长度大于1的连续空格缩短为长度为1的空格
    text = text.map(e => e.replace(/ {2,}/g, ' '))

    // const root = parse(html) // 解析HTML为根节点对象
    // const topLevelElements = root.childNodes.filter(node => node.nodeType === 1) // 选择顶层元素节点

    // const text = topLevelElements.map(node => node.text).join('\n'); // 将所有文本内容拼接为一个字符串，并用换行符分隔
}

const del_file = FilePath => {
    fs.unlink(FilePath, err => {
        if (err) {
            console.error(err)
        }
    })
}
