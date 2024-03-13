<template>
    <el-dialog width="32%" top="20vh" draggable :append-to-body="true">
        <div class="login" v-if="dialog_status === 1">
            <div class="box">
                <p class="table">登录</p>
                <br />
                <input class="input-content" type="text" placeholder="请输入邮箱号" v-model="email" />
                <input class="input-content" type="password" placeholder="请输入密码" v-model="password" />
                <div>
                    <span class="s1" @click="dialog_status = 3">忘记密码</span>
                    <span class="s2" @click="dialog_status = 2">注册</span>
                </div>
                <br />
                <a href="#" class="go" @click="login">登录</a>
            </div>
        </div>
        <div class="register" v-if="dialog_status === 2">
            <div class="box">
                <p class="table">注册</p>
                <br />
                <input class="input-content" type="text" placeholder="请输入邮箱号" v-model="email" />
                <input class="input-content" type="text" placeholder="请输入用户名" v-model="username" />
                <input class="input-content" type="password" placeholder="请输入密码" v-model="password" />
                <div class="verify-container">
                    <input class="verify-content" type="text" placeholder="请输入验证码" v-model="verify_code" />
                    <img src="/req1/user/get-verify-code/" ref="verify_img" @click="refresh_verify_code" />
                </div>
                <div>
                    <span class="s2" @click="dialog_status = 1">返回登录页</span>
                </div>
                <br />
                <a href="#" class="go" @click="register">注册账号</a>
            </div>
        </div>
        <div class="forget-password" v-if="dialog_status === 3">
            <div class="box">
                <p class="table">忘记密码</p>
                <br />
                <input class="input-content" type="text" placeholder="请输入注册邮箱号" v-model="email" />
                <div class="verify-container">
                    <input class="verify-content" type="text" placeholder="请输入验证码" v-model="verify_code" />
                    <img src="/req1/user/get-verify-code/" ref="verify_img" @click="refresh_verify_code" />
                </div>
                <div>
                    <span class="s2" @click="dialog_status = 1">返回登录页</span>
                </div>
                <br />
                <a href="#" class="go" @click="forget_password">确认</a>
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
const dialog_status = ref(1)
const email = ref('')
const username = ref('')
const password = ref('')
const verify_code = ref('')

watch(
    () => dialog_status.value,
    () => {
        email.value = ''
        username.value = ''
        password.value = ''
        verify_code.value = ''
    }
)

const infoStore = InfoStore()

const update_data = () => {
    infoStore.update_info()
    emit('is_login')
}

// 如果登录 传消息给NavBar 关闭此组件 | 需要登录则弹出窗口
const emit = defineEmits(['is_login', 'must_login', 'submit_email'])
const login = async () => {
    try {
        const res = await userApi.login({ email: email.value, password: SHA256Encrypt(password.value) })
        infoStore.token = res.data
        await userApi.getUserInfo()
        update_data()
    } catch (err) {
        //
    }
}

const register = async () => {
    try {
        const res = await userApi.register({
            email: email.value,
            username: username.value,
            password: SHA256Encrypt(password.value),
            verify_code: verify_code.value
        })
        infoStore.token = res.data
        await userApi.getUserInfo()
        update_data()
    } catch (err) {
        //
    }
}

// 刷新验证码
const verify_img = ref()
const refresh_verify_code = () => {
    verify_img.value.src = '/req1/user/get-verify-code/?time' + new Date().getTime()
}

// 这将会是一个全局方法
const show_login_dialog = () => {
    emit('must_login')
}

const forget_password = async () => {
    try {
        await userApi.forgetPassword({
            email: email.value,
            verify_code: verify_code.value
        })
        ElNotification({
            title: '请查看邮箱以重置您的密码',
            type: 'success'
        })
        emit('submit_email')
    } catch (err) {
        //
    }
}

window.show_login = show_login_dialog
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
