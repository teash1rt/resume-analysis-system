<template>
    <div class="box">
        <div class="content">
            <h1 class="topic">智能简历解析系统 V1.2</h1>
            <div class="desc">基于自然语言处理技术对简历进行关键信息提取和人才画像构建</div>
            <div class="desc">整合后端技术为求职者和招聘者搭建沟通桥梁</div>
            <div class="btn-group">
                <button class="get-start" @click="get_start">开始体验</button>
                <button class="get-author" @click="get_author">联系我们</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()
const get_start = () => {
    router.push({ name: 'loadview' })
}

const get_author = () => {
    window.open('https://github.com/teashirtt/resume-analysis-system')
}
</script>

<style lang="less" scoped>
.box {
    display: flex;
    justify-content: center;
}

.content {
    margin-top: 12vh;
    margin-bottom: 2vh;
}

.topic {
    font-size: 5.5rem;
    text-align: center;
    background-image: -webkit-linear-gradient(45deg, #43b375 25%, #3e5df7);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
}

.desc {
    text-align: center;
    font-size: 2.7rem;
    color: grey;
}

.btn-group {
    margin-top: 10vh;
}

.get-start {
    border: none;
    width: 130px;
    height: 53px;
    background-color: rgb(66, 184, 131);
    border-radius: 10px;
    color: white;
    font-size: 1.8rem;
    letter-spacing: 0.2rem;
    font-weight: bold;
    margin-left: 26%;
    cursor: pointer;

    &:hover {
        background-color: rgb(71, 200, 142);
        transition: 0.5s;
    }
}

.get-start svg {
    vertical-align: middle;
}

.get-author {
    border: 2px solid rgb(150, 150, 150);
    width: 130px;
    height: 53px;
    border-radius: 10px;
    background-color: rgb(232, 241, 247);
    margin-left: 10%;
    color: rgb(44, 44, 44);
    font-weight: bold;
    letter-spacing: 0.2rem;
    font-size: 1.8rem;
    cursor: pointer;

    &:hover {
        transition: 0.5s;
        background-color: rgb(76, 76, 76);
        color: rgb(232, 241, 247);
    }
}
</style>
