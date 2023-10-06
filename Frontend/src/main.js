import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import pinia from '@/stores/store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import { InfoStore } from '@/stores/InfoStore'
import * as echarts from 'echarts'

const debounce = (fn, delay, ...value) => {
    let timer = null
    return () => {
        const context = this
        const args = value
        clearTimeout(timer)
        timer = setTimeout(function () {
            fn.apply(context, args)
        }, delay)
    }
}

const _ResizeObserver = window.ResizeObserver
window.ResizeObserver = class ResizeObserver extends _ResizeObserver {
    constructor(callback) {
        callback = debounce(callback, 16)
        super(callback)
    }
}

const app = createApp(App).use(router).use(pinia).use(ElementPlus)

const infoStore = InfoStore()
infoStore.update_info()

const flag = navigator.userAgent.match(
    /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i
)
if (flag) {
    infoStore.is_mobile = true
}

app.config.globalProperties.echarts = echarts

app.mount('#app')
