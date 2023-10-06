import CryptoJS from 'crypto-js'

export const aesDecrypt = (str, key) => {
    const keyHex = CryptoJS.enc.Utf8.parse(key)
    const decrypted = CryptoJS.AES.decrypt(str, keyHex, {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7
    })
    return decrypted.toString(CryptoJS.enc.Utf8)
}
