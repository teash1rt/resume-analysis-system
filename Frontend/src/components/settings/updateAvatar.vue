<template>
    <div class="box">
        <el-upload
            class="avatar-uploader"
            :show-file-list="false"
            action="/req1/user/set-avatar/"
            :on-success="handleAvatarSuccess"
            :headers="{ Authorization: `Bearer ${infoStore.token}` }"
            :before-upload="beforeAvatarUpload"
            :auto-upload="false"
            ref="upload_ref"
            :on-change="changeAvatar">
            <img v-if="imageUrl" :src="imageUrl" class="avatar" />
            <svg v-else class="avatar-uploader-icon" viewBox="0 0 1024 1024">
                <path
                    fill="currentColor"
                    d="M480 480V128a32 32 0 0 1 64 0v352h352a32 32 0 1 1 0 64H544v352a32 32 0 1 1-64 0V544H128a32 32 0 0 1 0-64h352z"></path>
            </svg>
        </el-upload>
        <br />
        <el-button type="primary" class="btn" plain @click="submitUpload">上传</el-button>
    </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElNotification } from 'element-plus'
import * as imageConversion from 'image-conversion'
import { InfoStore } from '@/stores/InfoStore'

const imageUrl = ref('')
const infoStore = InfoStore()

const upload_ref = ref()
const emit = defineEmits(['change'])

const handleAvatarSuccess = () => {
    emit('change')
}

const beforeAvatarUpload = rawFile => {
    if (rawFile.type !== 'image/jpeg' || rawFile.type !== 'image/png') {
        ElNotification({
            title: '图像仅支持jpg和png格式',
            type: 'success'
        })
        return false
    } else if (rawFile.size / 1024 / 1024 > 1) {
        ElNotification({
            title: '图像应小于1MB',
            type: 'error'
        })
        return false
    } else if (rawFile.size / 1024 / 1024 > 0.05) {
        // 图像压缩至 50KB
        const myImg = new Promise(resolve => {
            imageConversion
                .compressAccurately(rawFile, 50)
                .then(res => {
                    resolve(res)
                })
                .catch(() => {})
        })
        return myImg
    }
    return true
}

const changeAvatar = uploadFile => {
    imageUrl.value = URL.createObjectURL(uploadFile.raw)
}

const submitUpload = () => {
    upload_ref.value.submit()
}
</script>

<style lang="less" scoped>
.avatar-uploader .avatar {
    position: relative;
    margin: 1vh auto;
    width: 260px;
    height: 260px;
}

.box {
    display: flex;
    flex-direction: column;
}

.btn {
    margin: 1vh auto;
    width: 40%;
}

.avatar-uploader .el-upload {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
    border-color: var(--el-color-primary);
}

.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 40%;
    height: 178px;
    text-align: center;
}
</style>
