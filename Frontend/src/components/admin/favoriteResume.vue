<template>
    <ul v-infinite-scroll="load" class="infinite-list" style="overflow: auto">
        <div class="card" v-for="e in data" :key="e.rid">
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
            <div class="date">
                {{ `收藏于 ${e.createTime.split('T')[0]}` }}
            </div>
        </div>
    </ul>
    <el-dialog v-model="dialogVisible" title="" width="70%" top="2vh">
        <resumeData :resume_data="infoData" />
    </el-dialog>
</template>

<script setup>
import { ref, onMounted, toRaw, shallowRef } from 'vue'
import { resumeApi } from '@/api'
import resumeData from '@/components/common/resumeData.vue'
import { downloadResume } from '@/utils/download'

const count = ref(0)
const data = shallowRef([])

const getFavoriteResume = () => {
    resumeApi.getFavoriteResume().then(res => {
        count.value = res.data.length
        res.data.map(item => {
            item.summaryInfo = JSON.parse(item.summaryInfo)
            item.detailInfo = JSON.parse(item.detailInfo)
            return item
        })
        data.value = res.data
    })
}

onMounted(() => {
    getFavoriteResume()
})

const load = () => {
    count.value += 2
}

const dialogVisible = ref(false)
const infoData = ref('')
const previewResume = rid => {
    const item = data.value.find(item => item.rid === rid)
    infoData.value = Object.assign({}, toRaw(item.summaryInfo), toRaw(item.detailInfo))
    dialogVisible.value = true
}

const cancelFavorite = rid => {
    resumeApi.cancelFavoriteResume({ rid }).then(() => {
        getFavoriteResume()
    })
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
