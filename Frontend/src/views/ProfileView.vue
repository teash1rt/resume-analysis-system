<template>
    <el-card class="box-card">
        <div class="common-layout">
            <el-container class="container">
                <el-aside width="300px" class="aside">
                    <div class="avatar">
                        <img :src="url" alt="avatar" class="avatar-img" />
                    </div>
                    <div class="name">
                        {{ infoStore.username }}
                    </div>
                </el-aside>
                <el-main class="main">
                    <el-tabs v-model="activeTab" class="tabs">
                        <el-tab-pane label="个人信息" name="View1" class="tab">
                            <userInfo />
                        </el-tab-pane>
                        <el-tab-pane :label="permission == 0 ? '上传记录' : '简历收藏'" name="View2" class="tab">
                            <resumeList />
                        </el-tab-pane>
                        <el-tab-pane label="个性化设置" name="View4" class="tab">
                            <userProfile />
                        </el-tab-pane>
                    </el-tabs>
                </el-main>
            </el-container>
        </div>
    </el-card>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import userInfo from '@/components/settings/userInfo.vue'
import userProfile from '@/components/settings/userProfile.vue'
import resumeList from '@/components/common/resumeList.vue'
import { InfoStore } from '@/stores/InfoStore'
import { convertToUrl } from '@/utils/base64ToUrl'
import { userApi } from '@/api'

const activeTab = ref('View1')
const infoStore = InfoStore()
const permission = infoStore.type
const url = ref('')

onMounted(() => {
    userApi.getAvatar().then(res => {
        url.value = res.data !== '' ? convertToUrl(res.data) : require('../assets/avatar.webp')
    })
})
</script>

<style lang="less" scoped>
.box-card {
    height: 600px;
    width: 1500px;
    margin: 35px auto;
}

.common-layout {
    text-align: center;
}

.aside {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding-top: 50px;
}

.avatar {
    width: 85%;
    padding-bottom: 90%;
    margin-top: 5vh;
    margin-left: 10%;
    border-radius: 50%;
    margin-bottom: 10px;
    position: relative;
    overflow: hidden;
}

.avatar-img {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.name {
    font-weight: bold;
    font-size: 3rem;
}

.main {
    margin-top: 2vh;
    width: 100%;
}

:deep(.tabs) {
    .el-tabs__item {
        font-size: 2.3rem;
        margin-left: 2vw;
    }
}
</style>
