<template>
    <div class="box">
        <div class="content">
            <h1 class="topic">智能简历解析系统</h1>
            <h3 class="version">V0.97</h3>
            <div class="desc">基于自然语言处理技术</div>
            <div class="desc">对简历进行关键信息提取和人才画像构建</div>
            <div class="waring">项目暂不支持移动端</div>
        </div>
    </div>
</template>

<style lang="less" scoped>
.box {
    display: flex;
    justify-content: center;

    .content {
        margin-top: 20vh;
        margin-bottom: 2vh;

        .topic {
            font-size: 4.5rem;
            text-align: center;
            background-image: -webkit-linear-gradient(45deg, #43b375 25%, #3e5df7);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            margin-bottom: 0;
        }

        .version {
            margin-top: 0;
            font-size: 2rem;
            text-align: center;
            background-image: -webkit-linear-gradient(45deg, #43b375 25%, #3e5df7);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        .desc {
            text-align: center;
            font-size: 1.6rem;
            color: grey;
        }

        .waring {
            margin-top: 5vh;
            text-align: center;
            font-size: 2.6rem;
        }
    }
}
</style>
