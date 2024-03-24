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
                    ref="upload_ref"
                    class="upload-demo"
                    :action="upload_url"
                    :before-upload="beforeUpload"
                    :data="{
                        summary_info: summary_info,
                        detail_info: detail_info,
                        score: resume_score
                    }"
                    :on-success="handleUploadSuccess"
                    :auto-upload="false"
                    :limit="1"
                    :on-exceed="handleExceed"
                    :on-change="file_change"
                    name="file"
                    :headers="{ Authorization: `Bearer ${jwt_token}` }"
                    :show-file-list="show_file_list"
                    :before-remove="beforeRemove">
                    <template #trigger>
                        <button class="status0-btn" v-if="status === 0">点击上传</button>
                    </template>
                    <button class="status1-btn" @click="upload_to_analysis" v-if="status === 1">确认上传</button>
                    <button class="status2-btn" v-if="status === 2">上传准备</button>
                </el-upload>
            </div>
        </div>
        <el-dialog v-model="dialog_visible" top="5vh" width="65%" :before-close="close_dialog" class="dialog">
            <div class="dialog-content">
                <div class="dialog-box">
                    <h3 class="dialog-topic">基本信息</h3>
                    <el-descriptions class="descriptions" :column="2" border>
                        <el-descriptions-item v-for="(value, key) in data.basic_data" :key="value">
                            <template #label>
                                <div>
                                    {{ mp.get(key) }}
                                </div>
                            </template>
                            <span v-if="key === 'loc' || key === 'college'">
                                <span v-for="e in value" style="margin-right: 0.7vw" :key="e">
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
                        <span v-for="e in data.tag.edu_tag" :key="e">
                            <el-tag class="ml-2" type="danger">{{ e }}</el-tag>
                        </span>
                        <span v-for="e in data.basic_data.college" :key="e">
                            <el-tag class="ml-2" type="danger">{{ e }}</el-tag>
                        </span>
                        <span v-for="e in data.basic_data.loc" :key="e">
                            <el-tag class="ml-2" type="success">{{ e }}</el-tag>
                        </span>
                        <el-tag class="ml-2" type="warning" v-if="data.tag.total_work_time > 0">{{
                            `工作年限:${data.tag.total_work_time}年`
                        }}</el-tag>
                        <span v-for="e in data.tag.experience_tag" :key="e">
                            <el-tag class="ml-2" type="warning">{{ e }}</el-tag>
                        </span>
                        <span v-for="e in data.tag.ability" :key="e">
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
                                    <div v-for="e in cut_sentence(data.experience)" :key="e">
                                        {{ e }}
                                    </div>
                                </el-descriptions-item>
                                <el-descriptions-item v-if="data.award">
                                    <template #label> 所得奖项 </template>
                                    <div v-for="e in data.award" :key="e">
                                        {{ e }}
                                    </div>
                                </el-descriptions-item>
                                <el-descriptions-item v-if="data.ability">
                                    <template #label> 个人能力 </template>
                                    <div v-for="e in data.ability" :key="e">
                                        {{ e }}
                                    </div>
                                </el-descriptions-item>
                            </el-descriptions>
                        </el-collapse-item>
                        <el-collapse-item title="更多补充信息(可选)" class="dialog-topic">
                            <div>
                                <div v-if="data.job_obj.length > 0">
                                    <h3 class="dialog-topic">求职意向</h3>
                                    <span v-for="e in data.job_obj" :key="e" style="margin-right: 0.7vw">
                                        {{ e }}
                                    </span>
                                </div>
                                <div v-else>
                                    <h3 class="dialog-topic">请填写求职意向</h3>
                                    <el-input
                                        v-model="job_obj_input"
                                        maxlength="20"
                                        placeholder="如有多个求职意向可用分号分隔"
                                        show-word-limit
                                        type="text" />
                                </div>
                            </div>
                            <div>
                                <h3 class="dialog-topic">薪资预期</h3>
                                <el-input
                                    v-model="money_obj_input"
                                    maxlength="10"
                                    placeholder="请输入您的薪资预期"
                                    show-word-limit
                                    type="text" />
                            </div>
                            <div>
                                <h3 class="dialog-topic">自我描述</h3>
                                <el-input
                                    v-model="self_description_input"
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
                <el-button type="default" plain @click="cancel_upload">取消上传</el-button>
                <el-button type="success" plain @click="upload_to_database">确认上传</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, nextTick, h } from 'vue'
import { genFileId, ElNotification } from 'element-plus'
import { InfoStore } from '@/stores/InfoStore'
import { userApi, resumeApi } from '@/api'

const upload_ref = ref(null)
const jwt_token = ref('')
// 0表示没选文件 1表示选了但没解析 2表示正在解析中 3表示解析完成没上传
const status = ref(0)
// 开始时显示列表 状态为2时消失
const show_file_list = ref(true)
const upload_url = ref('/req2/qes/')
const dialog_visible = ref(false)
const infoStore = InfoStore()

const handleExceed = files => {
    upload_ref.value.clearFiles()
    const file = files[0]
    file.uid = genFileId()
    upload_ref.value.handleStart(file)
}

const upload_to_analysis = () => {
    status.value = 2
    show_file_list.value = false
    upload_ref.value.submit()
}

const file_change = () => {
    // 状态转换 0->1
    if (status.value === 0) {
        status.value = 1
    } else if (status.value === 1) {
        status.value = 0
    }
}

const upload_to_database = () => {
    upload_url.value = '/req1/resume/upload/'
    jwt_token.value = infoStore.token
    const { basic_data, job_obj, experience, award, ability, job_fit, score, tag, custom_content } = data.value
    // 根据用户自定义内容构造 custom_content JSON 串 定义在下面
    calculate_content()
    // 划分 优化查询花销
    summary_info.value = JSON.stringify({ basic_data, job_obj, tag, custom_content })
    detail_info.value = JSON.stringify({ experience, award, ability, job_fit })
    resume_score.value = score
    upload_ref.value.submit()
    dialog_visible.value = false
    clear_data()
}

// 这里主要处理解析后取消上传的情况
const close_dialog = done => {
    cancel_upload()
    done()
}
const cancel_upload = () => {
    upload_ref.value.clearFiles()
    status.value = 0
    show_file_list.value = true
    dialog_visible.value = false
    clear_data()
}

const beforeRemove = () => {
    status.value = 0
    return true
}

const summary_info = ref('')
const detail_info = ref('')
const resume_score = ref(0)
const data = ref(null)

const refuse_beforeUpload = () => {
    show_file_list.value = true
    upload_ref.value.clearFiles()
    status.value = 0
}

const beforeUpload = async rawFile => {
    if (upload_url.value === '/req2/qes/') {
        const fileType = rawFile.type
        const fileSize = rawFile.size
        if (
            fileType !== 'application/vnd.openxmlformats-officedocument.wordprocessingml.document' &&
            fileType !== 'application/pdf' &&
            fileType !== 'text/plain'
        ) {
            ElNotification({
                title: '目前仅支持docx,pdf,txt格式',
                type: 'warning'
            })
            refuse_beforeUpload()
            return false
        } else if (fileSize > 5000 * 1024) {
            ElNotification({
                title: '文件过大',
                type: 'warning'
            })
            return false
        }
        try {
            await resumeApi.checkBeforeUpload()
        } catch {
            ElNotification({
                title: '用户同时只能上传三份简历',
                message: h('span', { style: 'color: teal' }, "请在'个人中心->上传记录'中删除简历再尝试提交")
            })
            refuse_beforeUpload()
            return false
        }
        try {
            const res = await userApi.getAuthorize()
            jwt_token.value = res.token
        } catch (err) {
            ElNotification({
                title: '服务器接口状态异常 | 请等待处理',
                type: 'error'
            })
            refuse_beforeUpload()
            return false
        }
    }
}

const handleUploadSuccess = (res, uploadFile) => {
    // 这里单独处理 因为单独用url而没用 axios 所以没办法被拦截
    // 这里首先判断调用这个方法的是哪次请求
    if (upload_url.value === '/req2/qes/') {
        // 关键代码 要不然无法二次提交
        uploadFile.status = 'ready'
        if (res.code === 400) {
            ElNotification({
                title: res.msg,
                type: 'warning'
            })
            // 恢复状态
            status.value = 0
            upload_ref.value.clearFiles()
            upload_url.value = '/req2/qes/'
        } else if (res.code === 200) {
            data.value = JSON.parse(res.res)
            dialog_visible.value = true
            status.value = 3
        }
    } else {
        if (res.code === 400) {
            ElNotification({
                title: res.msg,
                type: 'warning'
            })
        } else {
            ElNotification({
                title: res.msg,
                type: 'success'
            })
        }
        upload_ref.value.clearFiles()
        show_file_list.value = true
        // 这里结束的时候会调用 file_change
        status.value = 1
        upload_url.value = '/req2/qes/'
    }
}

// 由于JSON串的key是英文的 这里标注对应关系方便Descriptions中显示
const mp = new Map()
mp.set('name', '姓名')
    .set('birth', '生日')
    .set('age', '年龄')
    .set('tel', '电话')
    .set('email', '邮箱')
    .set('college', '学校')
    .set('loc', '住址')
    .set('edu', '学历')

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
const job_obj_input = ref('')
const money_obj_input = ref('')
const self_description_input = ref('')

const calculate_content = () => {
    if (data.value.job_obj.length === 0 && job_obj_input.value) {
        const job_obj = job_obj_input.value.split(/[; ；]/).filter(e => e.trim() !== '')
        data.value.job_obj.push(...job_obj)
    }
    if (money_obj_input.value && money_obj_input.value.trim !== '') {
        data.value.custom_content.money_obj = money_obj_input.value
    }
    if (self_description_input.value) {
        const self_description = self_description_input.value.split(/[; ；]/).filter(e => e.trim() !== '')
        data.value.custom_content.self_desc = [...self_description]
    }
    if (dynamicTags.value.length > 0) {
        data.value.custom_content.self_tag = [...dynamicTags.value]
    }
}

const clear_data = () => {
    dynamicTags.value.length = 0
    job_obj_input.value = ''
    money_obj_input.value = ''
    self_description_input.value = ''
}

const cut_sentence = sentence => {
    const arr = sentence
    for (let i = 0; i < sentence.length; i++) {
        if (arr[i].length > 60) {
            // 将字符串分为长度为60的块
            const chunks = arr[i].match(/.{1,60}/g)
            arr.splice(i, 1, ...chunks)
            i += chunks.length - 1
        }
    }
    return arr
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
    margin-top: 3vh;
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
    margin-top: 6vh;
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

.upload-demo {
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
