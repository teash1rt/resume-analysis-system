<template>
    <div class="box">
        <h1>更多功能开发中</h1>
        <el-dialog title="上传头像" width="30%" top="15vh" draggable v-model="to_change_avatar">
            <updateAvatar @change="upload_avatar_success" />
        </el-dialog>
        <el-button type="warning" @click="to_change_avatar = true">更改头像</el-button>
    </div>
</template>

<script setup>
import { ref } from 'vue'
import updateAvatar from './updateAvatar.vue'

const to_change_avatar = ref(false)

const upload_avatar_success = () => {
    window.location.reload()
}
</script>
