<template>
    <ul v-infinite-scroll="load" class="infinite-list" style="overflow: auto">
        <div class="card" v-for="e in resumes" :key="e.rid">
            <el-row>
                <el-col :span="17" class="topic">
                    {{ e.summaryInfo.basic_data.name }}
                    <el-divider direction="vertical" border-style="solid" />
                    <span v-for="(item, index) in getDescription(e)" :key="item">
                        {{ item }}
                        <el-divider direction="vertical" border-style="solid" v-if="index < getDescription(e).length - 1" />
                    </span>
                </el-col>
                <el-col :span="3" v-if="permission == 1">
                    <span class="resume-options" @click="cancelFavorite(e.rid)">取消收藏</span>
                </el-col>
                <el-col :span="2" v-if="permission == 0">
                    <span class="resume-options" @click="deleteUploadResume(e.rid)">删除</span>
                </el-col>
                <el-col :span="2">
                    <span class="resume-options" @click="previewResume(e.rid)">摘要</span>
                </el-col>
                <el-col :span="2">
                    <span class="resume-options" @click="downloadResume(e.rid)">下载</span>
                </el-col>
            </el-row>
            <div class="date">
                {{ `收藏于 ${e.createTime.split('T')[0]}` }}
            </div>
        </div>
    </ul>
    <el-dialog v-model="dialogVisible" title="" width="70%" top="2vh">
        <resumeData :resume_data="resumeInfo" />
    </el-dialog>
</template>

<script setup>
import { ref, onMounted, shallowRef } from 'vue'
import { ElNotification } from 'element-plus'
import { resumeApi } from '@/api'
import resumeData from '@/components/common/resumeData.vue'
import { downloadResume } from '@/utils/download'
import { getDescription } from '@/utils/getDescription'
import { InfoStore } from '@/stores/InfoStore'
import { useResumePreview } from '@/hooks/useResumePreview'

const count = ref(0)
const resumes = shallowRef([])
const permission = InfoStore().type

const { dialogVisible, resumeInfo, previewResume } = useResumePreview()

const getResumes = () => {
    const api = permission == 1 ? resumeApi.getFavoriteResumes : resumeApi.getUploadResumes
    api().then(res => {
        count.value = res.data.length
        resumes.value = res.data.map(item => {
            return {
                ...item,
                summaryInfo: JSON.parse(item.summaryInfo)
            }
        })
    })
}

const deleteUploadResume = rid => {
    resumeApi.deleteUploadResume({ rid: rid }).then(res => {
        ElNotification({
            title: res.msg,
            type: 'success'
        })
        getResumes()
    })
}

onMounted(() => {
    getResumes()
})

const load = () => {
    count.value += 2
}

const cancelFavorite = rid => {
    resumeApi.favoriteResume({ rid, isFavorite: false }).then(res => {
        ElNotification({
            title: res.msg,
            type: 'success'
        })
        getResumes()
    })
}
</script>

<style lang="less" scoped>
.infinite-list {
    height: 45vh;
    padding: 0;
    margin: 0;
    list-style: none;
}

.card {
    margin-bottom: 10px;
    height: 60px;
    position: relative;
    border: 1px rgb(223, 223, 223) solid;
    border-radius: 5px;
    padding: 20px 0 0 20px;
}

.topic {
    font-size: 1.7rem;
    text-align: left;
}

.resume-options {
    font-size: 1.5rem;
    cursor: pointer;
    color: grey;
}

.date {
    color: grey;
    position: absolute;
    bottom: 5px;
    right: 15px;
    font-size: 1.2rem;
}
</style>
