import { defineStore } from 'pinia'
import Cookies from 'js-cookie'
import { aesDecrypt } from '@/utils/decrypt'

export const InfoStore = defineStore('InfoStore', {
    state: () => {
        return {
            username: null,
            email: null,
            type: null,
            token: null
        }
    },
    actions: {
        updateInfo() {
            if (
                Cookies.get('username') &&
                Cookies.get('email') &&
                Cookies.get('type') &&
                Cookies.get('token') &&
                Cookies.get('llave')
            ) {
                const key = 'Re' + Cookies.get('llave') + 'Sume'
                this.username = aesDecrypt(Cookies.get('username'), key)
                this.email = aesDecrypt(Cookies.get('email'), key)
                this.type = aesDecrypt(Cookies.get('type'), key)
                this.token = aesDecrypt(Cookies.get('token'), key)
            }
        },
        clearInfo() {
            Cookies.remove('username')
            Cookies.remove('email')
            Cookies.remove('type')
            Cookies.remove('token')
            Cookies.remove('llave')
            this.username = null
            this.email = null
            this.type = null
            this.token = null
        }
    }
})
