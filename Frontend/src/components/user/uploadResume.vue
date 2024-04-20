<template>
    <div class="box">
        <el-card v-for="e in data" :key="e.rid" shadow="hover" class="card-item">
            <el-row class="row">
                <el-col :span="18" class="topic">
                    <el-tree :data="get_tree_data(e)" class="tree" />
                </el-col>
                <el-col :span="3">
                    <div class="grid-content ep-bg-purple-light" />
                </el-col>
            </el-row>
            <el-row class="row">
                <el-col :span="6" class="date">
                    {{ `发布于 ${e.createTime.split('T')[0]} ${e.createTime.split('T')[1]}` }}
                </el-col>
                <el-col :span="14">
                    <div class="grid-content ep-bg-purple" />
                </el-col>
                <el-col :span="2">
                    <span class="resume_op" @click="del_upload_resume(e.rid)">删除</span>
                </el-col>
                <el-col :span="2">
                    <el-tooltip content="简历关注度" placement="top" effect="customized">
                        <div class="hot">
                            <svg class="icon" viewBox="0 0 1024 1024" width="16" height="16">
                                <path
                                    d="M834.1 469.2c-19.5-43.8-47.4-82.6-82.9-115.2l-29.1-26.7c-4.3-3.8-11.1-2.1-13 3.3l-13 37.3c-8.1 23.4-23 47.3-44.1 70.8-1.4 1.5-3 1.9-4.1 2-1.1 0.1-2.8-0.1-4.3-1.5-1.4-1.2-2.1-3-2-4.8 3.7-60.2-14.3-128.1-53.7-202C555.3 171 510 123.1 453.4 89.7l-41.3-24.3c-5.4-3.2-12.3 1-12 7.3l2.2 48c1.5 32.8-2.3 61.8-11.3 85.9-11 29.5-26.8 56.9-47 81.5-14.2 17.2-30.1 32.7-47.5 46.1-42.1 32.3-76.8 74.4-100.3 121.5C172.5 503.3 160 556.6 160 610c0 47.2 9.3 92.9 27.7 136 17.8 41.5 43.2 78.9 75.5 110.9 32.4 32 70 57.2 111.9 74.7C418.5 949.8 464.5 959 512 959s93.5-9.2 136.9-27.3c41.9-17.5 79.6-42.6 111.9-74.7 32.4-32 57.8-69.4 75.5-110.9 18.4-43.1 27.7-88.8 27.7-136 0-48.8-10-96.2-29.9-140.9zM713 808.5c-53.7 53.2-125 82.4-201 82.4s-147.3-29.2-201-82.4c-53.5-53.1-83-123.5-83-198.4 0-43.5 9.8-85.2 29.1-124 18.8-37.9 46.8-71.8 80.8-97.9 21.5-16.4 41.2-35.5 58.6-56.8 25-30.5 44.6-64.5 58.2-101 5.4-14.5 9.5-30 12.1-46.5 24.1 22.2 44.3 49 61.2 80.4 33.4 62.6 48.8 118.3 45.8 165.7-1.4 22.8 7.5 44.5 24.4 59.8 14.7 13.2 33.7 19.9 53.4 18.8 19.7-1 37.8-9.7 51-24.4 13.3-14.9 24.8-30.1 34.4-45.6 14 17.9 25.7 37.4 35 58.4 15.9 35.8 24 73.9 24 113.1 0 74.9-29.5 145.4-83 198.4z"></path>
                            </svg>
                            <span>{{ e.favorite_score }}</span>
                        </div>
                    </el-tooltip>
                </el-col>
            </el-row>
        </el-card>
    </div>
</template>

<script setup>
import { onMounted, reactive } from 'vue'
import { ElNotification } from 'element-plus'
import { resumeApi } from '@/api'

const data = reactive([])

const mp = {}

onMounted(() => {
    const init = async () => {
        try {
            const res = await resumeApi.getUploadResumes()
            res.data.map((item, idx) => {
                mp[item.rid] = idx
                return (item.summaryInfo = JSON.parse(item.summaryInfo))
            })
            Object.assign(data, res.data)
        } catch (err) {
            //
        }
    }

    init()
})

const del_upload_resume = async rid => {
    try {
        const res = await resumeApi.deleteUploadResume({ rid: rid })
        ElNotification({
            title: res.msg,
            type: 'success'
        })
        data.splice(data[mp[rid]], 1)
    } catch (err) {
        //
    }
}

const get_tree_data = e => {
    return [
        {
            label: '简历信息',
            children: basic_data_tree(e.summaryInfo.basic_data)
        }
    ]
}

const basic_data_map = {
    name: '姓名',
    birth: '生日',
    age: '年龄',
    tel: '电话',
    email: '邮箱',
    college: '学校',
    loc: '住址',
    edu: '学历'
}

const basic_data_tree = e => {
    const arr = []
    for (const key in e) {
        const leaf = {}
        leaf['label'] = `${basic_data_map[key]}:${e[key]}`
        arr.push(leaf)
    }
    return arr
}
</script>

<style lang="less" scoped>
.box {
    height: 45vh;
    overflow-y: auto;
}

.card-item {
    margin-bottom: 1vh;
}

.row {
    margin-bottom: 0.5vh;
}

.hot:hover {
    cursor: pointer;
}
</style>

<style>
.el-popper.is-customized {
    padding: 6px 12px;
    background: linear-gradient(90deg, rgb(159, 229, 151), rgb(204, 229, 129));
}

.el-popper.is-customized .el-popper__arrow::before {
    background: linear-gradient(45deg, #b2e68d, #bce689);
    right: 0;
}
</style>
