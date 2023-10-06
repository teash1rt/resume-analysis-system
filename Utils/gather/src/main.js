import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

createApp(App).use(ElementPlus).mount('#app')

window.addEventListener('contextmenu', (event) => {
    event.preventDefault(); // 阻止默认右键菜单弹出
});