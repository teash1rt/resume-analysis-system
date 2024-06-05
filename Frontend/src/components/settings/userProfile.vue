<template>
    <div class="box">
        <h1>更多功能开发中</h1>
        <el-dialog title="上传头像" width="30%" top="15vh" draggable v-model="dialogVisible">
            <updateAvatar @change="reload" />
        </el-dialog>
        <el-button type="warning" @click="dialogVisible = true">更改头像</el-button>
    </div>
</template>

<script setup>
import { ref } from 'vue'
import updateAvatar from './updateAvatar.vue'

const dialogVisible = ref(false)

const reload = () => {
    window.location.reload()
}
</script>
