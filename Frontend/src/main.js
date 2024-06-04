import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import pinia from '@/stores/store'
import { InfoStore } from '@/stores/InfoStore'
import * as echarts from 'echarts'
import 'element-plus/es/components/notification/style/css'
import { debounce } from '@/utils/functions'
import { EnvStore } from '@/stores/EnvStore'

const resize_observer = window.ResizeObserver
window.ResizeObserver = class ResizeObserver extends resize_observer {
    constructor(callback) {
        callback = debounce(callback, 16)
        super(callback)
    }
}

const app = createApp(App).use(router).use(pinia)

const infoStore = InfoStore()
infoStore.updateInfo()

const envStore = EnvStore()
envStore.getEnv()

app.config.globalProperties.echarts = echarts

app.mount('#app')
