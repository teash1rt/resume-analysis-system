<template>
    <div class="navbar">
        <div class="navbar-content">
            <div class="block-a">
                <router-link :to="{ name: 'home' }" class="resume-link" href="#">简历解析系统</router-link>
            </div>
            <div class="block-b">
                <router-link :class="routerName === 'loadview' ? 'highlight' : null" :to="{ name: 'loadview' }">
                    简历解析
                </router-link>
                <router-link
                    :class="routerName === 'uploadview' ? 'highlight' : null"
                    :to="{ name: 'uploadview' }"
                    v-if="permission != 1">
                    简历上传
                </router-link>
                <router-link
                    :class="routerName === 'resumesview' || routerName === 'visualizationview' ? 'highlight' : null"
                    :to="{ name: 'resumesview' }"
                    v-if="permission == 1">
                    简历数据
                </router-link>
                <router-link :class="routerName === 'profileview' ? 'highlight' : null" :to="{ name: 'profileview' }">
                    个人中心
                </router-link>
            </div>
            <div class="block-c">
                <el-button class="login-btn" color="#6f59f7" @click="toLogin" v-if="!isLogin" size="large"> 登录 </el-button>
                <div class="login-btn" v-if="isLogin">
                    <div class="avatar">
                        <router-link :to="{ name: 'profileview' }">
                            <img :src="url" alt="avatar" />
                        </router-link>
                    </div>
                </div>
            </div>
            <loginDialog
                v-model="dialogVisible"
                @finishLogin="finishLogin"
                @submitEmail="submitEmail"
                @handleLogin="handleLogin" />
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { useRoute } from 'vue-router'
import loginDialog from '@/components/common/loginDialog.vue'
import { InfoStore } from '@/stores/InfoStore'
import { convertToUrl } from '@/utils/base64ToUrl'
import { userApi } from '@/api'

const route = useRoute()
const isLogin = ref(false)
const infoStore = InfoStore()
const permission = infoStore.type

const tokenCheck = () => {
    userApi
        .tokenCheck({ token: infoStore.token })
        .then(res => {
            if (res.data === true) {
                isLogin.value = true
                getAvatar()
            } else {
                isLogin.value = false
                infoStore.clearInfo()
            }
        })
        .catch(() => {
            isLogin.value = false
            infoStore.clearInfo()
        })
}

// 首先判断token是否过期
onMounted(() => {
    tokenCheck()
})

watch(
    () => infoStore.token,
    newVal => {
        if (!newVal) {
            isLogin.value = false
        }
    }
)

const routerName = computed(() => {
    return route.name
})

const dialogVisible = ref(false)
const toLogin = () => {
    dialogVisible.value = true
}

// 登录后的方法
const finishLogin = () => {
    dialogVisible.value = false
    isLogin.value = true
    getAvatar()
    window.location.reload()
}

// 忘记密码后的方法
const submitEmail = () => {
    dialogVisible.value = false
}
// 弹登录窗
const handleLogin = () => {
    dialogVisible.value = true
}

const url = ref('')
const getAvatar = () => {
    userApi.getAvatar().then(res => {
        url.value = res.data !== '' ? convertToUrl(res.data) : require('../../assets/avatar.webp')
    })
}
</script>

<style lang="less" scoped>
.navbar {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 90px;
    background: linear-gradient(to right, rgb(249, 250, 254), rgb(249, 250, 254), rgb(208, 231, 244));
    transition: background-color 0.3s;
    min-width: 1000px;
    z-index: 1000;
}

.navbar-content {
    display: flex;
    justify-content: space-between;
    height: 100%;
    padding: 0 7vw;
}

.block-a {
    flex: 1;
    display: flex;
    justify-content: right;
    align-items: center;
}

.resume-link {
    font-weight: bold;
    color: rgb(111, 89, 247);
    font-size: 3rem;
}

.block-b {
    flex: 2;
    display: flex;
    justify-content: center;
    align-items: center;

    a {
        color: black;
        margin: 0 2vw;
        font-size: 2.2rem;
        text-decoration: none;
        height: 40px;

        &:hover {
            color: rgb(18, 42, 198);
        }
    }

    .highlight {
        color: rgb(18, 42, 198) !important;
        position: relative;
        font-weight: 600;

        &::before {
            content: '';
            position: absolute;
            bottom: 0;
            width: 100%;
            height: 3px;
            background-color: rgb(18, 42, 198);
        }
    }
}

.block-c {
    flex: 1;
    display: flex;
    justify-content: left;
    align-items: center;
}

.login-btn {
    width: 120px;
}

.resume-link,
.match-link a,
.login-btn a {
    text-decoration: none;
}

.avatar {
    margin-left: 3vw;
    width: 70px;
    height: 70px;
}

.avatar img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    border-radius: 50%;
}
</style>
