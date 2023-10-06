import CryptoJS from 'crypto-js'

export const SHA256Encrypt = str => {
    return CryptoJS.SHA256(str).toString()
}
