<template>
    <div class="box">
        <div class="desc">
            <div class="text">
                <h1 class="topic">上传你的简历</h1>
                <div class="content">
                    您上传的简历信息将会同步提交给招聘者，我们在此提供简单易用的简历上传功能，同时支持解析简历并添加关键词标签，让您的简历更具吸引力。除此之外，您还可以自定义部分的标签和自我描述，以更好的展现您的优势和特点。
                </div>
                <div class="list">
                    <li>支持docx，pdf，txt格式</li>
                    <li>允许用户补充多维度信息，优化简历</li>
                </div>
            </div>
            <div>
                <el-upload
                    ref="uploadRef"
                    class="upload"
                    :action="uploadUrl"
                    :before-upload="beforeUpload"
                    :data="uploadData"
                    :on-success="handleUploadSuccess"
                    :auto-upload="false"
                    :limit="1"
                    :on-exceed="handleExceed"
                    :on-change="fileChange"
                    name="file"
                    :headers="{ Authorization: `Bearer ${jwtToken}` }"
                    :show-file-list="showFileList"
                    :before-remove="beforeRemove">
                    <template #trigger>
                        <button class="status0-btn" v-if="status === 0">点击上传</button>
                    </template>
                    <button class="status1-btn" @click="uploadToAnalysis" v-if="status === 1">确认上传</button>
                    <button class="status2-btn" v-if="status === 2">上传准备</button>
                </el-upload>
            </div>
        </div>
        <el-dialog v-model="dialogVisible" top="5vh" width="65%" :before-close="closeDialog" class="dialog">
            <div class="dialog-content">
                <div class="dialog-box">
                    <h3 class="dialog-topic">基本信息</h3>
                    <el-descriptions class="descriptions" :column="2" border>
                        <el-descriptions-item v-for="(value, key) in data.basic_data" :key="value">
                            <template #label>
                                <div>
                                    {{ COLUMNS[key] }}
                                </div>
                            </template>
                            <span v-if="key === 'loc' || key === 'college'">
                                <span v-for="(e, idx) in value" style="margin-right: 0.7vw" :key="idx">
                                    {{ e }}
                                </span>
                            </span>
                            <span v-else>
                                {{ value }}
                            </span>
                        </el-descriptions-item>
                    </el-descriptions>
                </div>
                <div class="dialog-box">
                    <h3 class="dialog-topic">发现标签</h3>
                    <div class="tag">
                        <span v-for="(e, idx) in data.tag.edu_tag" :key="idx">
                            <el-tag class="ml-2" type="danger">{{ e }}</el-tag>
                        </span>
                        <span v-for="(e, idx) in data.basic_data.college" :key="idx">
                            <el-tag class="ml-2" type="danger">{{ e }}</el-tag>
                        </span>
                        <span v-for="(e, idx) in data.basic_data.loc" :key="idx">
                            <el-tag class="ml-2" type="success">{{ e }}</el-tag>
                        </span>
                        <el-tag class="ml-2" type="warning" v-if="data.tag.total_work_time > 0">{{
                            `工作年限:${data.tag.total_work_time}年`
                        }}</el-tag>
                        <span v-for="(e, idx) in data.tag.experience_tag" :key="idx">
                            <el-tag class="ml-2" type="warning">{{ e }}</el-tag>
                        </span>
                        <span v-for="(e, idx) in data.tag.ability" :key="idx">
                            <el-tag class="ml-2">{{ e }}</el-tag>
                        </span>
                        <span>
                            <el-tag
                                v-for="tag in dynamicTags"
                                :key="tag"
                                class="ml-2"
                                closable
                                :disable-transitions="false"
                                type="info"
                                @close="handleClose(tag)">
                                {{ tag }}
                            </el-tag>
                            <el-input
                                v-if="inputVisible"
                                ref="InputRef"
                                v-model="inputValue"
                                class="custom-tag-input"
                                size="small"
                                @keyup.enter="handleInputConfirm"
                                @blur="handleInputConfirm" />
                            <el-button v-else class="custom-tag-btn" size="small" @click="showInput">+ 自定义标签</el-button>
                        </span>
                    </div>
                </div>
                <el-divider />
                <div class="dialog-box">
                    <el-collapse>
                        <el-collapse-item title="个人经历" class="dialog-topic">
                            <el-descriptions class="descriptions" :column="1" border>
                                <el-descriptions-item v-if="data.experience">
                                    <template #label> 工作/项目经历 </template>
                                    <div v-for="(e, idx) in cutSentence(data.experience)" :key="idx">
                                        {{ e }}
                                    </div>
                                </el-descriptions-item>
                                <el-descriptions-item v-if="data.award">
                                    <template #label> 所得奖项 </template>
                                    <div v-for="(e, idx) in data.award" :key="idx">
                                        {{ e }}
                                    </div>
                                </el-descriptions-item>
                                <el-descriptions-item v-if="data.ability">
                                    <template #label> 个人能力 </template>
                                    <div v-for="(e, idx) in data.ability" :key="idx">
                                        {{ e }}
                                    </div>
                                </el-descriptions-item>
                            </el-descriptions>
                        </el-collapse-item>
                        <el-collapse-item title="更多补充信息(可选)" class="dialog-topic">
                            <div>
                                <div v-if="data.job_obj.length > 0">
                                    <h3 class="dialog-topic">求职意向</h3>
                                    <span v-for="(e, idx) in data.job_obj" :key="idx" style="margin-right: 0.7vw">
                                        {{ e }}
                                    </span>
                                </div>
                                <div v-else>
                                    <h3 class="dialog-topic">请填写求职意向</h3>
                                    <el-input
                                        v-model="jobObjective"
                                        maxlength="20"
                                        placeholder="如有多个求职意向可用分号分隔"
                                        show-word-limit
                                        type="text" />
                                </div>
                            </div>
                            <div>
                                <h3 class="dialog-topic">薪资预期</h3>
                                <el-input
                                    v-model="salaryObjective"
                                    maxlength="10"
                                    placeholder="请输入您的薪资预期"
                                    show-word-limit
                                    type="text" />
                            </div>
                            <div>
                                <h3 class="dialog-topic">自我描述</h3>
                                <el-input
                                    v-model="selfDescription"
                                    maxlength="30"
                                    placeholder="请输入您的自我描述,支持使用分号划分不同方面的描述"
                                    show-word-limit
                                    type="text" />
                            </div>
                        </el-collapse-item>
                    </el-collapse>
                </div>
            </div>
            <div class="btn-group">
                <el-button type="default" plain @click="cancelUpload">取消上传</el-button>
                <el-button type="success" plain @click="uploadToDatabase">确认上传</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, nextTick, computed } from 'vue'
import { genFileId, ElNotification } from 'element-plus'
import { InfoStore } from '@/stores/InfoStore'
import { COLUMNS } from '@/constants/columns'
import { FILE_TYPE } from '@/constants/fileType'
import { Analysis_URL } from '@/constants/url'
import { cutSentence } from '@/utils/cutSentence'
import { userApi } from '@/api'

const uploadRef = ref(null)
const jwtToken = ref('')
// 0表示没选文件 1表示选了但没解析 2表示正在解析中 3表示解析完成没上传
const status = ref(0)
// 开始时显示列表 状态为2时消失
const showFileList = ref(true)
const uploadUrl = ref(Analysis_URL)
const dialogVisible = ref(false)
const infoStore = InfoStore()

const handleExceed = files => {
    uploadRef.value.clearFiles()
    const file = files[0]
    file.uid = genFileId()
    uploadRef.value.handleStart(file)
}

const uploadToAnalysis = () => {
    status.value = 2
    showFileList.value = false
    uploadRef.value.submit()
}

const fileChange = () => {
    // 状态转换 0->1
    if (status.value === 0) {
        status.value = 1
    } else if (status.value === 1) {
        status.value = 0
    }
}

const uploadToDatabase = () => {
    uploadUrl.value = '/req1/resume/upload/'
    jwtToken.value = infoStore.token
    // 根据用户自定义内容构造 custom_content JSON 串 定义在下面
    contentParse()
    uploadRef.value.submit()
    dialogVisible.value = false
    clearData()
}

// 这里主要处理解析后取消上传的情况
const closeDialog = done => {
    cancelUpload()
    done()
}
const cancelUpload = () => {
    uploadRef.value.clearFiles()
    status.value = 0
    showFileList.value = true
    dialogVisible.value = false
    clearData()
}

const beforeRemove = () => {
    status.value = 0
    return true
}

const data = ref(null)

const refuseBeforeUpload = () => {
    showFileList.value = true
    uploadRef.value.clearFiles()
    status.value = 0
}

const beforeUpload = async rawFile => {
    if (uploadUrl.value === Analysis_URL) {
        if (!FILE_TYPE.includes(rawFile.type)) {
            ElNotification({
                title: '目前仅支持docx,pdf,txt格式',
                type: 'warning'
            })
            refuseBeforeUpload()
            return false
        } else if (rawFile.size > 5000 * 1024) {
            ElNotification({
                title: '文件过大',
                type: 'warning'
            })
            return false
        }

        try {
            const res = await userApi.getAuthorize()
            jwtToken.value = res.token
        } catch (err) {
            ElNotification({
                title: '服务器接口状态异常 | 请等待处理',
                type: 'error'
            })
            refuseBeforeUpload()
            return false
        }
    }
}

const uploadData = computed(() => {
    return data.value
        ? {
              summary_info: JSON.stringify({
                  basic_data: data.value.basic_data,
                  job_obj: data.value.job_obj,
                  tag: data.value.tag,
                  custom_content: data.value.custom_content
              }),
              detail_info: JSON.stringify({
                  experience: data.value.experience,
                  award: data.value.award,
                  ability: data.value.ability,
                  job_fit: data.value.job_fit
              }),
              score: data.value.score
          }
        : {}
})

const handleUploadSuccess = (res, uploadFile) => {
    // 这里单独处理 因为单独用url而没用 axios 所以没办法被拦截
    // 这里首先判断调用这个方法的是哪次请求
    if (uploadUrl.value === Analysis_URL) {
        // 关键代码 要不然无法二次提交
        uploadFile.status = 'ready'
        if (res.code === 400) {
            ElNotification({
                title: res.msg,
                type: 'warning'
            })
            // 恢复状态
            status.value = 0
            uploadRef.value.clearFiles()
            uploadUrl.value = Analysis_URL
        } else if (res.code === 200) {
            data.value = JSON.parse(res.res)
            dialogVisible.value = true
            status.value = 3
        }
    } else {
        ElNotification({
            title: res.msg,
            type: res.code === 400 ? 'warning' : 'success'
        })
        uploadRef.value.clearFiles()
        showFileList.value = true
        // 这里结束的时候会调用 fileChange
        status.value = 1
        uploadUrl.value = Analysis_URL
    }
}

// 添加标签
const inputValue = ref('')
const dynamicTags = ref([])
const inputVisible = ref(false)
const InputRef = ref(null)

const handleClose = tag => {
    dynamicTags.value.splice(dynamicTags.value.indexOf(tag), 1)
}

const showInput = () => {
    inputVisible.value = true
    nextTick(() => {
        InputRef.value.input.focus()
    })
}

const handleInputConfirm = () => {
    if (inputValue.value && inputValue.value.trim() !== '') {
        if (inputValue.value.length > 8) {
            ElNotification({
                title: '自定义标签最多支持8个字符',
                type: 'warning'
            })
        } else if (dynamicTags.value.length >= 3) {
            ElNotification({
                title: '最多添加3个自定义标签',
                type: 'warning'
            })
        } else {
            dynamicTags.value.push(inputValue.value)
        }
    }
    inputVisible.value = false
    inputValue.value = ''
}

// 自定义内容
const jobObjective = ref('')
const salaryObjective = ref('')
const selfDescription = ref('')

const contentParse = () => {
    if (data.value.job_obj.length === 0 && jobObjective.value) {
        const job_obj = jobObjective.value.split(/[; ；]/).filter(e => e.trim() !== '')
        data.value.job_obj.push(...job_obj)
    }
    if (salaryObjective.value && salaryObjective.value.trim !== '') {
        data.value.custom_content.money_obj = salaryObjective.value
    }
    if (selfDescription.value) {
        const self_description = selfDescription.value.split(/[; ；]/).filter(e => e.trim() !== '')
        data.value.custom_content.self_desc = [...self_description]
    }
    if (dynamicTags.value.length > 0) {
        data.value.custom_content.self_tag = [...dynamicTags.value]
    }
}

const clearData = () => {
    dynamicTags.value.length = 0
    jobObjective.value = ''
    salaryObjective.value = ''
    selfDescription.value = ''
}
</script>

<style lang="less" scoped>
.box {
    width: 90vw;
}

.desc {
    display: inline-block;
    margin: auto;
    width: 70vw;
}

.text {
    padding-top: 3vh;
    margin-left: 13vw;
    width: 45vw;
}

.list {
    display: flex;
    font-size: 2rem;
    margin-top: 15px;
}

.list li {
    margin-left: 0.5vw;
    margin-right: 2vw;
}

.topic {
    font-size: 4rem;
}

.content {
    font-size: 2rem;
}

.status0-btn,
.status1-btn,
.status2-btn {
    margin-top: 30px;
    width: 170px;
    height: 60px;
    font-size: 1.6rem;
    color: #fff;
    border-radius: 20px;
    position: relative;
    border: transparent;
}

.status0-btn,
.status1-btn {
    background: linear-gradient(90deg, #755bea, #ff72c0);
    cursor: pointer;

    &:hover {
        background: linear-gradient(90deg, #8167f6, #ff43aa);
    }
}

.upload {
    margin-left: 13vw;
    margin-right: 60vw;
    height: 20vh;
    width: 10vw;
}

.status2-btn {
    cursor: not-allowed;
    background: linear-gradient(90deg, #f9d923, #cb49ff, #ff6666, #f9d923);
    background-size: 400%;
    animation: glow 30s linear forwards;
}

@keyframes glow {
    from {
        background-position: 0%;
    }

    to {
        background-position: 400%;
    }
}

.dialog .el-dialog__body {
    padding: 0;
}

.dialog .el-dialog__header {
    height: 2vh;
}

.dialog-content {
    width: 95%;
    margin: 0 auto;
}

.dialog-box {
    margin-bottom: 1vh;
}

.dialog-topic {
    margin-bottom: 1vh;
    color: black;
}

.tag {
    display: flex;
    flex-wrap: wrap;
    gap: 0.3vw;
}

.custom-tag-input {
    width: auto;
}

.btn-group {
    display: flex;
    justify-content: space-between;
    width: 95%;
    margin: 1vh auto;
}
</style>
