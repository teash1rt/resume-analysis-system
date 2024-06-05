<template>
    <ul v-infinite-scroll="load" class="infinite-list" style="overflow: auto">
        <el-card class="card-item" v-for="e in data" :key="e.rid">
            <el-row>
                <el-col :span="17" class="topic">
                    {{ e.summaryInfo.basic_data.name }}
                    <el-divider direction="vertical" border-style="solid" />
                    <span v-for="(item, index) in getDescription(e)" :key="item">
                        {{ item }}
                        <el-divider direction="vertical" border-style="solid" v-if="index < getDescription(e).length - 1" />
                    </span>
                </el-col>
                <el-col :span="3">
                    <span class="resume-options" @click="cancelFavorite(e.rid)">取消收藏</span>
                </el-col>
                <el-col :span="2">
                    <span class="resume-options" @click="previewResume(e.rid)">摘要</span>
                </el-col>
                <el-col :span="2">
                    <span class="resume-options" @click="downloadResume(e.rid)">下载</span>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="20">
                    <div class="grid-content ep-bg-purple" />
                </el-col>
                <el-col :span="4" class="date">
                    {{ `收藏于 ${e.favorite_createTime.split('T')[0]}` }}
                </el-col>
            </el-row>
        </el-card>
    </ul>
    <el-dialog v-model="dialogVisible" title="" width="70%" top="2vh">
        <resumeData :resume_data="infoData" />
    </el-dialog>
</template>

<script setup>
import { ref, onMounted, reactive, toRaw } from 'vue'
import { resumeApi } from '@/api'
import resumeData from '@/components/common/resumeData.vue'
import { downloadResume } from '@/utils/download'

const count = ref(0)
const data = reactive([])

// 记录data下标和rid的映射关系
const ridMap = new Map()

onMounted(() => {
    resumeApi.getFavoriteResume().then(res => {
        count.value = res.data.length
        res.data.map((item, idx) => {
            item.summaryInfo = JSON.parse(item.summaryInfo)
            item.detailInfo = JSON.parse(item.detailInfo)
            ridMap.set(item.rid, idx)
            return item
        })
        Object.assign(data, res.data)
    })
})

const load = () => {
    count.value += 2
}

const dialogVisible = ref(false)
const infoData = ref('')
const previewResume = rid => {
    infoData.value = Object.assign({}, toRaw(data[ridMap.get(rid)].summaryInfo), toRaw(data[ridMap.get(rid)].detailInfo))
    dialogVisible.value = true
}

const cancelFavorite = rid => {
    data.splice(data[ridMap.get(rid)], 1)
    resumeApi.cancelFavoriteResume(rid)
}

const getDescription = e => {
    const content = []
    if (e.summaryInfo.job_obj[0] !== undefined) content.push(`求职意向：${e.summaryInfo.job_obj[0]}`)
    if (e.summaryInfo.tag.total_work_time > 0) content.push(`工作年限：${e.summaryInfo.tag.total_work_time}年`)
    if (e.summaryInfo.custom_content.money_obj !== '') content.push(`薪资期望：${e.summaryInfo.custom_content.money_obj}`)
    if (content.length === 0) content.push('收到一份新简历!')
    return content
}
</script>

<style lang="less" scoped>
.infinite-list {
    height: 45vh;
    padding: 0;
    margin: 0;
    list-style: none;
}

.card-item {
    margin-bottom: 1vh;
}

.topic {
    font-size: 1.5rem;
    text-align: left;
}

.resume-options {
    cursor: pointer;
}

.date {
    color: grey;
}
</style>
