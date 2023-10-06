<template>
  <div class="box">
    <el-upload class="upload-demo" drag :action="uploadUrl" :before-upload="beforeAvatarUpload" :auto-upload="false"
      ref="uploadRef" :name="fieldName" :on-success="handleUploadSuccess" :on-error="handleUploadError" :limit="1">
      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text">
        拖入文件 <em>点击上传</em>
      </div>
    </el-upload>
    <el-button class="btn" type="primary" plain @click="submitUpload">上传</el-button>
  </div>
  <div v-if="dataget" v-for="(item, index) in data_arr" :key="index">
    <div class="sentence" @click="choice(item)">
      {{ item }}
    </div>
  </div>
  <div v-if="showBox" class="choice_box" :style="{ top: YY - 10 + 'px', left: XX - 10 + 'px' }">
    <div v-for="i in 8" :key="i" class="box-item" @click="classification(i)">
      {{ i }}
    </div>
  </div>
</template>

<script setup>
import { UploadFilled } from '@element-plus/icons-vue'
import { ref, onMounted } from 'vue'
import axios from 'axios'

const uploadRef = ref()
const uploadUrl = 'http://127.0.0.1:3010/analysis-docx-file/' 
const fieldName = 'file' // 上传文件的字段名称

const dataget = ref(false)
const showBox = ref(false)
const data_arr = ref([])

const beforeAvatarUpload = (rawFile) => {
  const fileType = rawFile.type
  const fileSize = rawFile.size
  dataget.value = false
  if (fileType !== 'application/vnd.openxmlformats-officedocument.wordprocessingml.document') {
    // 非docx文件
    console.log('Please upload a valid docx file.')
    return false
  }

  if (fileSize > 3000 * 1024) {
    // 文件大小超过限制（500kb）
    console.log('too big')
    return false
  }
  return true
}

const handleUploadSuccess = (response, file, fileList) => {
  dataget.value = true
  data_arr.value = response
  uploadRef.value.clearFiles() // 清除已上传的文件
}

const handleUploadError = (error, file, fileList) => {
  console.error('Upload error:', error)
}

const submitUpload = () => {
  uploadRef.value.submit()
}

const handleClick = (event) => {
  if (event.button === 2) {
    showBox.value = false

  }
}
const mouseX = ref(0)
const mouseY = ref(0)
const XX = ref(0)
const YY = ref(0)
onMounted(() => {
  const handleMouseMove = (event) => {
    mouseX.value = event.pageX - 40
    mouseY.value = event.pageY - window.scrollY - 30
  }
  document.addEventListener('mousemove', handleMouseMove)
  document.addEventListener('mousedown', handleClick)
})

const sen = ref('')
const choice = (s) => {
  sen.value = s
  showBox.value = true
  XX.value = mouseX.value
  YY.value = mouseY.value
}

const classification = (ord) => {
  showBox.value = false
  axios({
    method: 'post',
    url: 'http://127.0.0.1:3010/classification',
    data: {
      item: `${sen.value}&${ord}`
    }
  }).then(() => {})
}
</script>

<style scoped>
.box {
  width: 40vw;
  margin: 50px auto;
  text-align: center;
}

.btn {
  margin-top: 20px;
}

.choice_box {
  position: fixed;
  display: flex;
  /* flex-direction: column; */
  align-items: center;
  justify-content: center;
  width: 240px;
  height: 30px;
  border: 1px solid #ccc;
  z-index: 99;
}

.box-item {
  border: 1px solid black;
  width: 30px;
  height: 100%;
  background-color: blanchedalmond;
  color: blue;
  text-align: center;
}

.sentence {
  border: 1px solid black;
}

.sentence,
.box-item:hover {
  cursor: pointer;
}
</style>