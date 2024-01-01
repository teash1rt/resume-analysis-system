import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import pinia from '@/stores/store'
import { InfoStore } from '@/stores/InfoStore'
import * as echarts from 'echarts'
import 'element-plus/es/components/notification/style/css'
import { debounce } from '@/utils/functions'

const resize_observer = window.ResizeObserver
window.ResizeObserver = class ResizeObserver extends resize_observer {
    constructor(callback) {
        callback = debounce(callback, 16)
        super(callback)
    }
}

const app = createApp(App).use(router).use(pinia)

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
