<template>
    <div class="navbar">
        <div class="navbar-content">
            <div class="block-a">
                <router-link :to="{ name: 'home' }" class="resume-link" href="#">简历解析系统</router-link>
            </div>
            <div class="block-b">
                <router-link :class="router_name === 'loadview' ? 'highlight' : null" :to="{ name: 'loadview' }"
                    >简历解析</router-link
                >
                <router-link
                    :class="router_name === 'uploadview' ? 'highlight' : null"
                    :to="{ name: 'uploadview' }"
                    v-if="permission != 1"
                    >简历上传</router-link
                >
                <router-link
                    :class="router_name === 'resumesview' ? 'highlight' : null"
                    :to="{ name: 'resumesview' }"
                    v-if="permission == 1"
                    >简历数据</router-link
                >
                <router-link :class="router_name === 'profileview' ? 'highlight' : null" :to="{ name: 'profileview' }"
                    >个人中心</router-link
                >
            </div>
            <div class="block-c">
                <el-button class="login-btn" color="#6f59f7" @click="to_login" v-if="!show_user_info" size="large">
                    登录
                </el-button>
                <div class="login-btn" v-if="show_user_info">
                    <span class="el-dropdown-link">
                        <div class="avatar">
                            <router-link :to="{ name: 'profileview' }">
                                <img :src="url" alt="avatar" />
                            </router-link>
                        </div>
                    </span>
                </div>
            </div>
            <loginDialog v-model="dialog_visible" @is_login="is_login" @submit_email="submit_email" @must_login="must_login" />
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import loginDialog from '@/components/common/loginDialog.vue'
import { InfoStore } from '@/stores/InfoStore'
import { convert_to_url } from '@/utils/base64ToUrl'
import { userApi } from '@/api'
import { useRoute } from 'vue-router'

const route = useRoute()
const show_user_info = ref(false)
const infoStore = InfoStore()
const permission = infoStore.type

const tokenCheck = async () => {
    try {
        const res = await userApi.tokenCheck({ token: infoStore.token })
        if (res.data === true) {
            show_user_info.value = true
            get_avatar()
        } else {
            show_user_info.value = false
            infoStore.clear_info()
        }
    } catch (err) {
        //
    }
}

// 首先判断token是否过期
onMounted(() => {
    tokenCheck()
})

watch(
    () => infoStore.token,
    newVal => {
        if (newVal === null) {
            show_user_info.value = false
        }
    }
)

const router_name = ref(route.name)
watch(
    () => route.name,
    newV => {
        router_name.value = newV
    }
)

const dialog_visible = ref(false)
const to_login = () => {
    dialog_visible.value = true
}

// 登录后的方法
const is_login = () => {
    dialog_visible.value = false
    show_user_info.value = true
    get_avatar()
    window.location.reload()
}

// 忘记密码后的方法
const submit_email = () => {
    dialog_visible.value = false
}
// 弹登录窗
const must_login = () => {
    dialog_visible.value = true
}

const url = ref('')
const get_avatar = async () => {
    try {
        const res = await userApi.getAvatar()
        if (res.data !== '') {
            url.value = convert_to_url(res.data)
        } else {
            url.value = require('../../assets/avatar.webp')
        }
    } catch (err) {
        //
    }
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

.el-dropdown-link {
    cursor: pointer;

    &:focus {
        outline: none;
    }
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
