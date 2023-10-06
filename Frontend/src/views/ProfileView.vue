<template>
    <div class="box">
        <el-card class="box-card">
            <div class="common-layout">
                <el-container class="container">
                    <el-aside width="14vw" class="aside">
                        <div class="avatar">
                            <img :src="url" alt="avatar" class="avatar_img" />
                        </div>
                        <div class="name">
                            {{ infoStore.username }}
                        </div>
                    </el-aside>
                    <el-main class="main">
                        <el-tabs v-model="active_name" class="tabs">
                            <el-tab-pane label="个人信息" name="View1" class="tab">
                                <userInfo />
                            </el-tab-pane>
                            <el-tab-pane label="上传记录" name="View3" class="tab" v-if="permission == 0">
                                <uploadResume />
                            </el-tab-pane>
                            <el-tab-pane label="简历收藏" name="View3" class="tab" v-else>
                                <favoriteResume />
                            </el-tab-pane>
                            <el-tab-pane label="个性化设置" name="View4" class="tab">
                                <userProfile />
                            </el-tab-pane>
                        </el-tabs>
                    </el-main>
                </el-container>
            </div>
        </el-card>
    </div>
</template>

<script setup>
import { ref } from 'vue'
import userInfo from '@/components/settings/userInfo.vue'
import userProfile from '@/components/settings/userProfile.vue'
import uploadResume from '@/components/user/uploadResume.vue'
import favoriteResume from '@/components/admin/favoriteResume.vue'
import { InfoStore } from '@/stores/InfoStore'
import { req1 } from '@/utils/request'
import { convert_to_url } from '@/utils/base64ToUrl'

const active_name = ref('View1')
const infoStore = InfoStore()
const permission = infoStore.type

const url = ref('')
req1.get(`/req1/user/get-avatar/`)
    .then(res => {
        if (res.data !== '') {
            url.value = convert_to_url(res.data)
        } else {
            url.value =
                'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202103%2F23%2F20210323142845_bf6a9.thumb.1000_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1688290726&t=9d91c8a22f032305954b6b556b7dadc7'
        }
    })
    .catch(() => {})
</script>

<style lang="less" scoped>
.box-card {
    height: 59vh;
    width: 62vw;
    margin: 15vh auto;
    min-width: 800px;
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
    height: 0;
    padding-bottom: 90%;
    margin-top: 5vh;
    margin-left: 10%;
    border-radius: 50%;
    margin-bottom: 10px;
    position: relative;
    overflow: hidden;
}

.avatar_img {
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
