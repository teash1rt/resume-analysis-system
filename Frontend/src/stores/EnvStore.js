import { defineStore } from 'pinia'

export const EnvStore = defineStore('EnvStore', {
    state: () => {
        return {
            isMobile: false,
            version: ''
        }
    },
    actions: {
        getEnv() {
            if (
                navigator.userAgent.match(
                    /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i
                )
            ) {
                this.isMobile = true
            }
            this.version = require('../../package.json').version
        }
    }
})
