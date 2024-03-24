<template>
    <div class="list">
        <div class="item1">
            <div class="field">用户名</div>
            <div class="info" v-if="!to_change_username">{{ infoStore.username }}</div>
            <div class="info" v-if="to_change_username">
                <el-input v-model="username_data" placeholder="请输入新用户名" class="input" />
            </div>
            <div class="edit" @click="edit_username" v-if="!to_change_username">
                <svg viewBox="0 0 1024 1024" width="20" height="20">
                    <path
                        fill="currentColor"
                        d="M832 512a32 32 0 1 1 64 0v352a32 32 0 0 1-32 32H160a32 32 0 0 1-32-32V160a32 32 0 0 1 32-32h352a32 32 0 0 1 0 64H192v640h640V512z"></path>
                    <path
                        fill="currentColor"
                        d="m469.952 554.24 52.8-7.552L847.104 222.4a32 32 0 1 0-45.248-45.248L477.44 501.44l-7.552 52.8zm422.4-422.4a96 96 0 0 1 0 135.808l-331.84 331.84a32 32 0 0 1-18.112 9.088L436.8 623.68a32 32 0 0 1-36.224-36.224l15.104-105.6a32 32 0 0 1 9.024-18.112l331.904-331.84a96 96 0 0 1 135.744 0z"></path>
                </svg>
                编辑
            </div>
            <div class="edit" v-if="to_change_username">
                <svg @click="change_username" viewBox="0 0 1024 1024" width="20" height="20">
                    <path
                        fill="currentColor"
                        d="M406.656 706.944 195.84 496.256a32 32 0 1 0-45.248 45.248l256 256 512-512a32 32 0 0 0-45.248-45.248L406.592 706.944z"></path>
                </svg>
                <svg @click="to_change_username = false" viewBox="0 0 1024 1024" width="20" height="20">
                    <path
                        fill="currentColor"
                        d="M764.288 214.592 512 466.88 259.712 214.592a31.936 31.936 0 0 0-45.12 45.12L466.752 512 214.528 764.224a31.936 31.936 0 1 0 45.12 45.184L512 557.184l252.288 252.288a31.936 31.936 0 0 0 45.12-45.12L557.12 512.064l252.288-252.352a31.936 31.936 0 1 0-45.12-45.184z"></path>
                </svg>
            </div>
        </div>
        <div class="item2">
            <div class="field">注册邮箱</div>
            <div class="info" v-if="!to_change_email">{{ infoStore.email }}</div>
            <div class="info" v-if="to_change_email">
                <el-input v-model="email_data" placeholder="请输入新邮箱" class="input" />
            </div>
            <div class="edit" @click="edit_email" v-if="!to_change_email">
                <svg viewBox="0 0 1024 1024" width="20" height="20">
                    <path
                        fill="currentColor"
                        d="M832 512a32 32 0 1 1 64 0v352a32 32 0 0 1-32 32H160a32 32 0 0 1-32-32V160a32 32 0 0 1 32-32h352a32 32 0 0 1 0 64H192v640h640V512z"></path>
                    <path
                        fill="currentColor"
                        d="m469.952 554.24 52.8-7.552L847.104 222.4a32 32 0 1 0-45.248-45.248L477.44 501.44l-7.552 52.8zm422.4-422.4a96 96 0 0 1 0 135.808l-331.84 331.84a32 32 0 0 1-18.112 9.088L436.8 623.68a32 32 0 0 1-36.224-36.224l15.104-105.6a32 32 0 0 1 9.024-18.112l331.904-331.84a96 96 0 0 1 135.744 0z"></path>
                </svg>
                编辑
            </div>
            <div class="edit" v-if="to_change_email">
                <svg @click="change_email" viewBox="0 0 1024 1024" width="20" height="20">
                    <path
                        fill="currentColor"
                        d="M406.656 706.944 195.84 496.256a32 32 0 1 0-45.248 45.248l256 256 512-512a32 32 0 0 0-45.248-45.248L406.592 706.944z"></path>
                </svg>
                <svg @click="to_change_email = false" viewBox="0 0 1024 1024" width="20" height="20">
                    <path
                        fill="currentColor"
                        d="M764.288 214.592 512 466.88 259.712 214.592a31.936 31.936 0 0 0-45.12 45.12L466.752 512 214.528 764.224a31.936 31.936 0 1 0 45.12 45.184L512 557.184l252.288 252.288a31.936 31.936 0 0 0 45.12-45.12L557.12 512.064l252.288-252.352a31.936 31.936 0 1 0-45.12-45.184z"></path>
                </svg>
            </div>
        </div>
        <div class="item3">
            <div class="field">用户权限</div>
            <div class="info">{{ permission }}</div>
            <div class="edit" v-if="permission === '普通用户'" @click="apply_permission">
                <svg viewBox="0 0 1024 1024" width="20" height="20">
                    <path
                        fill="currentColor"
                        d="M512 64h64v192h-64V64zm0 576h64v192h-64V640zM160 480v-64h192v64H160zm576 0v-64h192v64H736zM249.856 199.04l45.248-45.184L430.848 289.6 385.6 334.848 249.856 199.104zM657.152 606.4l45.248-45.248 135.744 135.744-45.248 45.248L657.152 606.4zM114.048 923.2 68.8 877.952l316.8-316.8 45.248 45.248-316.8 316.8zM702.4 334.848 657.152 289.6l135.744-135.744 45.248 45.248L702.4 334.848z"></path>
                </svg>
                申请高级权限
            </div>
            <div class="edit" v-else>
                <svg viewBox="0 0 1024 1024" width="20" height="20">
                    <path
                        fill="currentColor"
                        d="M624 475.968V640h144a128 128 0 0 1 128 128H128a128 128 0 0 1 128-128h144V475.968a192 192 0 1 1 224 0zM128 896v-64h768v64H128z"></path>
                </svg>
                高级权限
            </div>
        </div>
    </div>
    <div class="button-wrapper">
        <el-button type="info" plain @click="to_change_password = true" size="large" class="btn">更改密码</el-button>
        <el-button type="danger" plain @click="logout" size="large" class="btn">退出登录</el-button>
    </div>
    <el-dialog title="更改密码" width="30%" top="18vh" draggable v-model="to_change_password">
        <el-form :label-position="left" label-width="100px" :model="formLabelAlign" style="max-width: 460px">
            <el-form-item label="原密码">
                <el-input v-model="old_password" />
            </el-form-item>
            <el-form-item label="新密码">
                <el-input v-model="new_password" />
            </el-form-item>
        </el-form>
        <el-button type="primary" @click="change_password">确认更改</el-button>
    </el-dialog>
    <el-dialog v-model="to_apply_permission" title="申请信息" width="50%" top="20vh">
        <el-form :model="form" label-width="120px">
            <el-form-item label="申请性质">
                <el-radio-group v-model="form.purpose">
                    <el-radio label="个人" />
                    <el-radio label="企业" />
                </el-radio-group>
            </el-form-item>
            <el-form-item label="申请信息">
                <el-input
                    v-model="form.description"
                    type="textarea"
                    placeholder="请填写申请信息（如个人介绍，申请用途等）"
                    :autosize="{ minRows: 4, maxRows: 7 }" />
            </el-form-item>
            <el-form-item label="邮箱验证">
                <el-input v-model="form.verify_code" placeholder="请输入邮箱验证码" style="width: 80%" />
                <el-button style="margin-left: 1vw" @click="get_email_verify_code" v-if="!waiting_verify_code"
                    >发送验证码</el-button
                >
                <el-button style="margin-left: 1vw" v-if="waiting_verify_code" type="info" class="waiting-btn"
                    >&nbsp;&nbsp;&nbsp;{{ countdown }}s&nbsp;&nbsp;&nbsp;</el-button
                >
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="submit_apply">提交</el-button>
                <el-button @click="to_apply_permission = false">取消</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
</template>

<script setup>
import { InfoStore } from '@/stores/InfoStore'
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElNotification } from 'element-plus'
import { SHA256Encrypt } from '@/utils/encrypt'
import { userApi } from '@/api'

const infoStore = InfoStore()
const permission = infoStore.type == 0 ? '普通用户' : '高级权限'

const router = useRouter()
const logout = () => {
    infoStore.clear_info()
    router.push({ name: 'home' })
}

const to_change_username = ref(false)
const to_change_email = ref(false)
const username_data = ref('')
const email_data = ref('')
const edit_username = () => {
    to_change_username.value = true
}

const edit_email = () => {
    to_change_email.value = true
}

const change_username = async () => {
    try {
        await userApi.changeUsername({ new_username: username_data.value })
        await userApi.getUserInfo()
        ElNotification({
            title: '用户名更改成功',
            type: 'success'
        })
        infoStore.update_info()
        to_change_username.value = false
    } catch (err) {
        //
    }
}

const change_email = async () => {
    try {
        await userApi.changeEmail({ new_email: email_data.value })
        logout()
    } catch (err) {
        //
    }
}

const old_password = ref('')
const new_password = ref('')
const to_change_password = ref(false)

const change_password = async () => {
    try {
        await userApi.changePassword({
            old_password: SHA256Encrypt(old_password.value),
            new_password: SHA256Encrypt(new_password.value)
        })
        to_change_password.value = false
        ElNotification({
            title: '密码更改成功',
            type: 'success'
        })
    } catch (err) {
        //
    }
}

// 控制申请弹窗
const to_apply_permission = ref(false)
const form = reactive({
    purpose: '',
    description: '',
    verify_code: ''
})

// 发送验证码后显示 30s 倒计时
const waiting_verify_code = ref(false)
const countdown = ref(30)

const apply_permission = async () => {
    try {
        const res = await userApi.checkApplicationStatus({
            email: infoStore.email
        })
        if (res.data) {
            ElNotification({
                title: '您的申请正在审核中',
                message: '我们会尽快处理您的申请，请关注您的邮箱以查看申请结果',
                type: 'warning'
            })
        } else {
            to_apply_permission.value = true
        }
    } catch (err) {
        //
    }
}

const get_email_verify_code = async () => {
    waiting_verify_code.value = true
    try {
        await userApi.getApplyVerifyCode({
            email: infoStore.email
        })
        ElNotification({
            title: '邮箱验证码发送成功',
            type: 'success'
        })
    } catch (err) {
        //
    } finally {
        const timer = setInterval(() => {
            countdown.value -= 1
            if (countdown.value <= 0) {
                clearInterval(timer)
                countdown.value = 30
                waiting_verify_code.value = false
            }
        }, 1000)
    }
}

const submit_apply = async () => {
    try {
        await userApi.applyPermission({
            email: infoStore.email,
            purpose: form.purpose,
            description: form.description,
            verify_code: form.verify_code
        })
        to_apply_permission.value = false
        ElNotification({
            title: '申请成功！请等待审核',
            message: '我们会尽快处理您的申请，请关注您的邮箱以查看申请结果',
            type: 'success'
        })
    } catch (err) {
        //
    }
}
</script>

<style lang="less" scoped>
.list {
    min-width: 800px;
}

.item1,
.item2 {
    border-bottom: 1px solid #e7e7e7;
}

.item1,
.item2,
.item3 {
    width: 100%;
    display: flex;
    align-items: center;
    margin: 10px;
    padding: 20px;
    height: 35px;
}

.field {
    flex: 0.6;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 2.3rem;
}

.info {
    flex: 1;
    text-align: center;
    font-size: 2.3rem;
}

.edit {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    text-align: center;
    font-size: 2.1rem;
    color: grey;
    cursor: pointer;
}

.button-wrapper {
    margin-top: 4vh;
    margin-right: 2vw;
}

.btn {
    margin-right: 1vw;
    font-size: 1.8rem;
}

.input {
    width: 13vw;
}

.waiting-btn {
    cursor: not-allowed;
}
</style>
