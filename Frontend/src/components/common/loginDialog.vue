<template>
    <el-dialog width="32%" top="20vh" draggable :append-to-body="true">
        <div class="login" v-if="dialogStatus === 1">
            <div class="box">
                <p class="table">登录</p>
                <br />
                <input class="input-content" type="text" placeholder="请输入邮箱号" v-model="email" />
                <input class="input-content" type="password" placeholder="请输入密码" v-model="password" />
                <div>
                    <span class="s1" @click="dialogStatus = 3">忘记密码</span>
                    <span class="s2" @click="dialogStatus = 2">注册</span>
                </div>
                <br />
                <a href="#" class="go" @click="login">登录</a>
            </div>
        </div>
        <div class="register" v-if="dialogStatus === 2">
            <div class="box">
                <p class="table">注册</p>
                <br />
                <input class="input-content" type="text" placeholder="请输入邮箱号" v-model="email" />
                <input class="input-content" type="text" placeholder="请输入用户名" v-model="username" />
                <input class="input-content" type="password" placeholder="请输入密码" v-model="password" />
                <div class="verify-container">
                    <input class="verify-content" type="text" placeholder="请输入验证码" v-model="verifyCode" />
                    <img src="/req1/user/get-verify-code/" ref="verifyImgRef" @click="refresh_verify_code" />
                </div>
                <div>
                    <span class="s2" @click="dialogStatus = 1">返回登录页</span>
                </div>
                <br />
                <a href="#" class="go" @click="register">注册账号</a>
            </div>
        </div>
        <div class="forget-password" v-if="dialogStatus === 3">
            <div class="box">
                <p class="table">忘记密码</p>
                <br />
                <input class="input-content" type="text" placeholder="请输入注册邮箱号" v-model="email" />
                <div class="verify-container">
                    <input class="verify-content" type="text" placeholder="请输入验证码" v-model="verifyCode" />
                    <img src="/req1/user/get-verify-code/" ref="verifyImgRef" @click="refresh_verify_code" />
                </div>
                <div>
                    <span class="s2" @click="dialogStatus = 1">返回登录页</span>
                </div>
                <br />
                <a href="#" class="go" @click="handleForgetPassword">确认</a>
            </div>
        </div>
    </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'
import { InfoStore } from '@/stores/InfoStore'
import { SHA256Encrypt } from '@/utils/encrypt'
import { userApi } from '@/api'
import { ElNotification } from 'element-plus'

// 状态1显示登录 状态2显示注册 状态3显示忘记密码
const dialogStatus = ref(1)
const email = ref('')
const username = ref('')
const password = ref('')
const verifyCode = ref('')

watch(
    () => dialogStatus.value,
    () => {
        email.value = ''
        username.value = ''
        password.value = ''
        verifyCode.value = ''
    }
)

const infoStore = InfoStore()

const updateData = () => {
    userApi.getUserInfo().then(() => {
        infoStore.updateInfo()
        emit('finishLogin')
    })
}

// 如果登录 传消息给NavBar 关闭此组件 | 需要登录则弹出窗口
const emit = defineEmits(['finishLogin', 'handleLogin', 'submitEmail'])
const login = () => {
    userApi.login({ email: email.value, password: SHA256Encrypt(password.value) }).then(res => {
        infoStore.token = res.data
        updateData()
    })
}

const register = () => {
    userApi
        .register({
            email: email.value,
            username: username.value,
            password: SHA256Encrypt(password.value),
            verify_code: verifyCode.value
        })
        .then(res => {
            infoStore.token = res.data
            updateData()
        })
}

// 刷新验证码
const verifyImgRef = ref()
const refresh_verify_code = () => {
    verifyImgRef.value.src = '/req1/user/get-verify-code/?time' + new Date().getTime()
}

// 这将会是一个全局方法
const showLoginDialog = () => {
    emit('handleLogin')
}

const handleForgetPassword = () => {
    userApi
        .forgetPassword({
            email: email.value,
            verify_code: verifyCode.value
        })
        .then(() => {
            ElNotification({
                title: '请查看邮箱以重置您的密码',
                type: 'success'
            })
            emit('submitEmail')
        })
}

window.showLogin = showLoginDialog
</script>

<style lang="less" scoped>
* {
    padding: 0;
    margin: 0;
    text-decoration: none;
}

.forget-password,
.register,
.login {
    display: flex;
    justify-content: center;
    align-items: center;
    background: linear-gradient(to right bottom, rgba(255, 255, 255, 0.7), rgba(255, 255, 255, 0.5), rgba(255, 255, 255, 0.4));
}

.table {
    font: 900 37px '';
    text-align: center;
    letter-spacing: 5px;
    color: #3d3d3d;
}

.box {
    overflow: hidden;
    display: flex;
    flex-direction: column;
}

.input-content {
    width: 400px;
    height: 100%;
    margin-bottom: 16px;
    outline: none;
    border: 0;
    padding: 20px;
    background-color: transparent;
    border-bottom: 3px solid rgb(150, 150, 240);
    font: 900 16px '';
}

.verify-container {
    display: flex;
    align-items: center;
}

.verify-content {
    width: 65%;
    height: 100%;
    margin-bottom: 16px;
    outline: none;
    border: 0;
    padding: 20px;
    background-color: transparent;
    border-bottom: 3px solid rgb(150, 150, 240);
    font: 900 16px '';
}

img {
    width: 35%;
    height: 100%;
    margin-bottom: 16px;
}

.go {
    width: 95%;
    margin: 0 auto;
    text-align: center;
    font-weight: 700;
    display: block;
    height: 24px;
    padding: 12px;
    border-radius: 10px;
    color: #fff;
    letter-spacing: 3px;
    background-image: linear-gradient(to left, #fd79a8, #a29bf6);
}

.s1 {
    padding: 10px 8px;
    float: left;
    cursor: pointer;
}

.s2 {
    padding: 10px;
    float: right;
    cursor: pointer;
}
</style>
