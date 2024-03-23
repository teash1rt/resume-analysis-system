import { resumeApi } from '@/api'

export const download_resume_fn = async rid => {
    try {
        const res = await resumeApi.downloadResume({ rid })
        const binaryData = atob(res.data.data)
        const uint8Array = new Uint8Array(binaryData.length)
        for (let i = 0; i < binaryData.length; i++) {
            uint8Array[i] = binaryData.charCodeAt(i)
        }
        const blob = new Blob([uint8Array])
        // 创建 a 标签
        const a = document.createElement('a')
        a.href = URL.createObjectURL(blob)
        a.download = `简历${res.data.type}`
        // 隐藏 a 标签
        a.style.display = 'none'
        // 将a标签追加到文档对象中
        document.body.appendChild(a)
        a.click()
        a.remove()
    } catch (err) {
        //
    }
}
