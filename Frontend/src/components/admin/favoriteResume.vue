<template>
    <ul v-infinite-scroll="load" class="infinite-list" style="overflow: auto">
        <el-card class="card-item" v-for="e in data" :key="e.rid">
            <el-row>
                <el-col :span="17" class="topic">
                    {{ e.summaryInfo.basic_data.name }}
                    <el-divider direction="vertical" border-style="solid" />
                    <span v-for="(item, index) in content_show(e)" :key="item">
                        {{ item }}
                        <el-divider direction="vertical" border-style="solid" v-if="index < content_show(e).length - 1" />
                    </span>
                </el-col>
                <el-col :span="3">
                    <span class="resume-options" @click="cancel_favorite(e.rid)">取消收藏</span>
                </el-col>
                <el-col :span="2">
                    <span class="resume-options" @click="preview_resume(e.rid)">摘要</span>
                </el-col>
                <el-col :span="2">
                    <span class="resume-options" @click="download_resume(e.rid)">下载</span>
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
    <el-dialog v-model="dialog_visible" title="" width="70%" top="2vh">
        <resumeData :resume_data="info_data" />
    </el-dialog>
</template>

<script setup>
import { ref, onMounted, reactive, toRaw } from 'vue'
import { resumeApi } from '@/api'
import resumeData from '@/components/common/resumeData.vue'
import { download_resume_fn } from '@/utils/download'

const count = ref(0)
const data = reactive([])

// 记录data下标和rid的映射关系
const mp = new Map()

const getFavoriteResume = async () => {
    const res = await resumeApi.getFavoriteResume()
    count.value = res.data.length
    res.data.map((item, idx) => {
        item.summaryInfo = JSON.parse(item.summaryInfo)
        item.detailInfo = JSON.parse(item.detailInfo)
        mp.set(item.rid, idx)
        return item
    })
    Object.assign(data, res.data)
}

onMounted(() => {
    getFavoriteResume()
})

const load = () => {
    count.value += 2
}

const dialog_visible = ref(false)
const info_data = ref('')
const preview_resume = rid => {
    info_data.value = Object.assign({}, toRaw(data[mp.get(rid)].summaryInfo), toRaw(data[mp.get(rid)].detailInfo))
    dialog_visible.value = true
}

const cancel_favorite = rid => {
    data.splice(data[mp.get(rid)], 1)
    resumeApi.cancelFavoriteResume(rid)
}

const download_resume = async rid => {
    download_resume_fn(rid)
}

const content_show = e => {
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
