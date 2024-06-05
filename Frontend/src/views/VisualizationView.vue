<template>
    <div class="box">
        <el-card shadow="hover" class="card">
            <router-link :to="{ name: 'resumesview' }" class="back">&lt;返回</router-link>
            <el-carousel indicator-position="outside" height="80vh" :autoplay="false" trigger="click" v-if="hasInit">
                <el-carousel-item class="carousel-item">
                    <educationChart class="chartA" :data="data.education_statistics" />
                </el-carousel-item>
                <el-carousel-item class="carousel-item">
                    <locationChart class="chartB" :data="data.location_statistics" />
                </el-carousel-item>
                <el-carousel-item class="carousel-item">
                    <experienceChart class="chartC" :data="data.experience_statistics" />
                </el-carousel-item>
            </el-carousel>
        </el-card>
    </div>
</template>

<script setup>
import { ref, shallowRef, onMounted } from 'vue'
import educationChart from '@/components/admin/educationChart.vue'
import locationChart from '@/components/admin/locationChart.vue'
import experienceChart from '@/components/admin/experienceChart.vue'
import { statisticsApi } from '@/api'

const data = shallowRef(null)
const hasInit = ref(false)

onMounted(() => {
    statisticsApi.getStatisticsInfo().then(res => {
        data.value = res.data
        hasInit.value = true
    })
})
</script>

<style lang="less" scoped>
.card {
    width: 65vw;
    margin: 10px auto 0;
    min-width: 800px;
    position: relative;
    min-height: 500px;

    .back {
        position: absolute;
        right: 25px;
        font-size: 1.6rem;
        text-decoration: none;
        z-index: 10;
    }
}
</style>
