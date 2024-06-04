export const convertToUrl = str => {
    const byteCharacters = atob(str)
    const byteArrays = []
    for (let i = 0; i < byteCharacters.length; i++) {
        byteArrays.push(byteCharacters.charCodeAt(i))
    }
    const blob = new Blob([new Uint8Array(byteArrays)], { type: 'image/jpeg' })
    const imageUrl = URL.createObjectURL(blob)
    return imageUrl
}
