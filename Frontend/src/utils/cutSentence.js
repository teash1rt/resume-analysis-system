export const cutSentence = sentence => {
    const arr = sentence
    for (let i = 0; i < sentence.length; i++) {
        if (arr[i].length > 60) {
            // 将字符串分为长度为60的块
            const chunks = arr[i].match(/.{1,60}/g)
            arr.splice(i, 1, ...chunks)
            i += chunks.length - 1
        }
    }
    return arr
}
