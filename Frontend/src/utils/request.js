import axios from 'axios'
import { ElNotification } from 'element-plus'
import { InfoStore } from '@/stores/InfoStore'
import pinia from '@/stores/store'
import { show_login } from '@/utils/showLogin'

const infoStore = InfoStore(pinia)

const req1 = axios.create({
    timeout: 10000,
    withCredentials: true
})

req1.interceptors.request.use(config => {
    config.headers.Authorization = `Bearer ${infoStore.token}`
    return config
})

req1.interceptors.response.use(res => {
    if (res.data.code === 400) {
        ElNotification({
            title: res.data.msg,
            type: 'warning'
        })
        return Promise.reject(res.data)
    } else if (res.data.code === 401) {
        // 自定义报错
        return Promise.reject(res.data)
    } else if (res.data.code === 402 || res.data.code === 510) {
        // token正常过期 || CustomException越权异常
        infoStore.clear_info()
        show_login()
    }
    return res.data
})

const req2 = axios.create({
    timeout: 5000,
    withCredentials: true
})

req2.interceptors.response.use(res => {
    if (res.data.code === 400) {
        ElNotification({
            title: res.msg,
            type: 'warning'
        })
        return Promise.reject(res.data)
    }
    return res.data
})
export { req1, req2 }
