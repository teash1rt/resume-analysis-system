<template>
    <el-card class="box-card">
        <template #header>
            <div class="card-header">
                <span class="topic">
                    简历数据中心
                    <router-link :to="{ name: 'visualizationview' }" href="#" class="btn">数据可视化</router-link>
                </span>
                <el-dropdown :hide-on-click="false">
                    <span class="el-dropdown-link">
                        {{ sort_order_name }}
                        <svg viewBox="0 0 1024 1024" width="13" height="13">
                            <path
                                fill="currentColor"
                                d="M831.872 340.864 512 652.672 192.128 340.864a30.592 30.592 0 0 0-42.752 0 29.12 29.12 0 0 0 0 41.6L489.664 714.24a32 32 0 0 0 44.672 0l340.288-331.712a29.12 29.12 0 0 0 0-41.728 30.592 30.592 0 0 0-42.752 0z"></path>
                        </svg>
                    </span>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item @click="get_page_resumes(0)">最早发布</el-dropdown-item>
                            <el-dropdown-item @click="get_page_resumes(1)">最新发布</el-dropdown-item>
                            <el-dropdown-item @click="get_page_resumes(2)">按推荐排序</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </div>
        </template>
        <div v-for="(e, index) in resume_data" :key="e.rid">
            <el-card class="resume-card">
                <el-row>
                    <el-col :span="18">
                        {{ e.summaryInfo.basic_data.name }}
                        <el-divider direction="vertical" border-style="solid" />
                        <span v-for="(item, index) in content_show(e)" :key="item">
                            {{ item }}
                            <el-divider direction="vertical" border-style="solid" v-if="index < content_show(e).length - 1" />
                        </span>
                    </el-col>
                    <el-col :span="5" class="create-time"> {{ e.createTime.split('T')[0] }} by {{ e.username }} </el-col>
                </el-row>
                <el-row>
                    <div class="tags">
                        <span v-if="e.summaryInfo.tag.edu_tag.length > 0">
                            <span v-for="tag in e.summaryInfo.tag.edu_tag" :key="tag">
                                <el-tag class="ml-2" type="danger">{{ tag }}</el-tag>
                            </span>
                        </span>
                        <el-tag class="ml-2" type="success" v-if="e.summaryInfo.tag.loc_tag !== ''">
                            {{ e.summaryInfo.tag.loc_tag }}
                        </el-tag>
                        <span v-if="e.summaryInfo.tag.experience_tag.length > 3">
                            <el-tag class="ml-2" type="warning">{{
                                `拥有${e.summaryInfo.tag.experience_tag.length}次工作经历`
                            }}</el-tag>
                        </span>
                        <span v-if="e.summaryInfo.tag.experience_tag.length > 0 && e.summaryInfo.tag.experience_tag.length <= 3">
                            <span v-for="tag in e.summaryInfo.tag.experience_tag" :key="tag">
                                <el-tag class="ml-2" type="warning">{{ tag }}</el-tag>
                            </span>
                        </span>
                        <span v-for="tag in e.summaryInfo.tag.ability" :key="tag">
                            <el-tag class="ml-2">{{ tag }}</el-tag>
                        </span>
                        <span v-for="tag in e.summaryInfo.custom_content.self_tag" :key="tag">
                            <el-tag class="ml-2" type="info">{{ tag }}</el-tag>
                        </span>
                    </div>
                </el-row>
                <el-row>
                    <el-col :span="21">
                        <div class="grid-content ep-bg-purple" />
                    </el-col>
                    <el-col :span="1">
                        <span class="resume-options" @click="preview_resume(e.rid)">摘要</span>
                    </el-col>
                    <el-col :span="1">
                        <span class="resume-options" @click="download_resume(e.rid)">下载</span>
                    </el-col>
                    <el-col :span="1">
                        <svg
                            v-if="!tmp_favorite[index]"
                            @click="change_favorite_status(index, e.rid)"
                            class="icon"
                            viewBox="0 0 1024 1024"
                            width="20"
                            height="20">
                            <path
                                d="M248.482281 938.000324c-4.306072 0-8.592702-1.336438-12.211113-3.967358-6.395664-4.646833-9.600659-12.521175-8.264221-20.314675l48.430012-282.363949L71.288626 431.382914c-5.66093-5.519714-7.698333-13.772678-5.255701-21.291932 2.444679-7.519254 8.943696-13.000082 16.768919-14.137998l283.508006-41.195238L493.099535 97.853655c3.498684-7.089465 10.720156-11.577686 18.627243-11.577686 7.907087 0 15.127536 4.489244 18.627243 11.577686l126.788661 256.904091 283.510052 41.195238c7.823176 1.137916 14.322194 6.618744 16.766872 14.137998 2.442632 7.519254 0.405229 15.773242-5.255701 21.291932L747.012502 631.354342l48.430012 282.363949c1.336438 7.7935-1.868557 15.667841-8.264221 20.314675-6.399757 4.646833-14.878872 5.257747-21.874193 1.582031L511.726777 802.298666 258.146385 935.614997C255.107165 937.211355 251.789607 938.000324 248.482281 938.000324zM130.422422 431.011454 313.25654 609.228415c4.894474 4.7727 7.128351 11.647271 5.974062 18.385742l-43.163055 251.64532 225.994104-118.811989c6.048763-3.180436 13.282514-3.180436 19.331277 0l225.992057 118.811989-43.163055-251.64532c-1.154289-6.738471 1.079588-13.613042 5.974062-18.385742l182.833095-178.216961-252.665557-36.71418c-6.767124-0.983397-12.614296-5.233188-15.641235-11.362792L511.726777 153.97893 398.729214 382.934482c-3.025916 6.129604-8.874111 10.379395-15.639189 11.362792L130.422422 431.011454z"
                                fill="#272636"></path>
                        </svg>
                        <svg
                            v-else
                            @click="change_favorite_status(index, e.rid)"
                            class="icon"
                            viewBox="0 0 1024 1024"
                            width="20"
                            height="20">
                            <path
                                d="M1006.942208 391.708672c-2.10944-5.599232-7.458816-9.310208-13.459456-9.310208L672.516096 382.398464 526.367744 42.118144c-2.251776-5.26336-7.45472-8.675328-13.205504-8.675328-0.021504 0-0.043008 0-0.043008 0-5.772288 0-10.975232 3.45088-13.2096 8.762368L356.626432 382.399488 32.821248 382.399488c-5.983232 0-11.355136 3.709952-13.459456 9.271296-2.10944 5.604352-0.54784 11.91936 3.959808 15.880192L290.67264 642.930688l-88.240128 314.625024c-1.600512 5.72928 0.504832 11.880448 5.28896 15.418368 4.780032 3.53792 11.246592 3.791872 16.303104 0.590848l289.137664-184.52992 289.133568 184.52992c2.359296 1.514496 5.056512 2.230272 7.7312 2.230272 2.989056 0 5.961728-0.927744 8.489984-2.7392 4.780032-3.493888 6.928384-9.560064 5.413888-15.29344l-84.93568-319.50848L1002.939392 407.59296C1007.468544 403.633152 1009.047552 397.313024 1006.942208 391.708672z"
                                fill="#FFB86C"></path>
                        </svg>
                    </el-col>
                </el-row>
            </el-card>
        </div>
        <div class="pagination-block">
            <el-pagination
                background
                layout="prev, pager, next"
                :total="total_count"
                class="mt-4"
                :hide-on-single-page="true"
                v-model:current-page="current_page"
                v-model:page-size="page_size"
                @current-change="page_change()" />
        </div>
    </el-card>
    <el-dialog v-model="dialog_visible" title="" width="70%" top="2vh" :before-close="close_dialog">
        <resumeData :resume_data="data" />
    </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import resumeData from '@/components/common/resumeData.vue'
import { resumeApi } from '@/api'
import { download_resume_fn } from '@/utils/download'

const current_page = ref(1)
const page_size = 4
const total_count = ref(0)

const resume_data = reactive([])
const tmp_favorite = reactive([])

const page_change = () => {
    resume_data.length = 0
    get_page_resumes(sort_order.value)
}

onMounted(() => {
    const init = async () => {
        try {
            const res = await resumeApi.getTotalCount()
            total_count.value = res.data
            get_page_resumes(1)
        } catch (err) {
            //
        }
    }

    init()
})

const sort_order_map = {
    0: '最早发布',
    1: '最新发布',
    2: '按推荐倒序'
}

const sort_order = ref(0)
const sort_order_name = ref(sort_order_map[0])
const get_page_resumes = async e => {
    sort_order.value = e
    sort_order_name.value = sort_order_map[e]

    try {
        const res = await resumeApi.getPageResumesInfo({
            page: current_page.value,
            page_size: page_size,
            sort_order: sort_order.value
        })
        res.data.map(item => {
            item.summaryInfo = JSON.parse(item.summaryInfo)
            return item
        })
        Object.assign(resume_data, res.data)
        // 清空数组
        tmp_favorite.length = 0
        for (let i = 0; i < resume_data.length; i++) {
            tmp_favorite.push(resume_data[i].favorite)
        }
    } catch (err) {
        //
    }
}

const content_show = e => {
    const content = []
    if (e.summaryInfo.job_obj[0] !== undefined) content.push(`求职意向：${e.summaryInfo.job_obj[0]}`)
    if (e.summaryInfo.tag.total_work_time !== 0) content.push(`工作年限：${e.summaryInfo.tag.total_work_time}`)
    if (e.summaryInfo.custom_content.money_obj !== '') content.push(`薪资期望：${e.summaryInfo.custom_content.money_obj}`)
    if (content.length === 0) content.push('收到一份新简历!')
    return content
}

// 防抖
let timer = null
let lastIdx = null
const change_favorite_status = (idx, rid) => {
    tmp_favorite[idx] = !tmp_favorite[idx]
    if (timer && idx === lastIdx) {
        clearTimeout(timer)
    }
    timer = setTimeout(() => {
        lastIdx = idx
        tmp_favorite[idx] ? resumeApi.addFavoriteResume({ rid: rid }) : resumeApi.cancelFavoriteResume({ rid: rid })
    }, 750)
}

const data = ref('')
const dialog_visible = ref(false)
const preview_resume = async rid => {
    try {
        const res = await resumeApi.getOneResumeInfo({ rid: rid })
        data.value = Object.assign({}, JSON.parse(res.data.summaryInfo), JSON.parse(res.data.detailInfo))
        dialog_visible.value = true
    } catch (err) {
        //
    }
}

const close_dialog = done => {
    dialog_visible.value = false
    done()
}

const download_resume = async rid => {
    download_resume_fn(rid)
}
</script>

<style lang="less" scoped>
.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.btn,
.btn:link,
.btn:visited {
    margin-left: 1.5vw;
    padding: 13px 0;
    color: #393939;
    text-transform: uppercase;
    font-size: 1.4rem;
    letter-spacing: 1px;
    transition: all 0.2s ease-in-out;
}

.box-card {
    width: 65vw;
    margin: 10px auto 0;
    height: 82vh;
    min-width: 800px;
    overflow: auto;
}

.pagination-block {
    display: block;
    position: absolute;
    bottom: 1vh;
    right: 18vw;
}

.resume-card {
    height: 140px;
    margin-bottom: 3vh;
}

:deep(.el-card) {
    .el-card__body {
        width: 95%;
        margin: auto;
        font-size: 2rem;
    }
}

.ml-2 {
    margin-right: 0.6vw;
}

.create-time {
    font-size: 1rem;
    color: grey;
    text-align: right;
    margin-left: 1.5vw;
}

.tags {
    margin-top: 1vh;
}

.topic {
    font-size: 2.3rem;
    font-weight: bold;
}

.resume-options {
    font-size: 1.5rem;
    color: rgb(0, 0, 122);
    vertical-align: top;
    cursor: pointer;
}
</style>
