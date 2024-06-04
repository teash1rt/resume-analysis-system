<template>
    <div class="box">
        <el-card shadow="hover" class="card">
            <router-link :to="{ name: 'resumesview' }" class="back">&lt;返回</router-link>
            <el-carousel
                indicator-position="outside"
                class="carousel"
                height="80vh"
                :autoplay="false"
                trigger="click"
                v-if="hasInit">
                <el-carousel-item class="carousel-item">
                    <educationChart class="chartA" :edu_data="edu_data" />
                </el-carousel-item>
                <el-carousel-item class="carousel-item">
                    <locationChart class="chartB" :loc_data="loc_data" />
                </el-carousel-item>
                <el-carousel-item class="carousel-item">
                    <experienceChart class="chartC" :exp_data="exp_data" />
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

const edu_data = shallowRef(null)
const exp_data = shallowRef(null)
const loc_data = shallowRef(null)

const hasInit = ref(false)

onMounted(() => {
    const init = async () => {
        try {
            const res = await statisticsApi.getStatisticsInfo()
            const { education_statistics, experience_statistics, location_statistics } = res.data
            edu_data.value = education_statistics
            exp_data.value = experience_statistics
            loc_data.value = location_statistics
            hasInit.value = true
        } catch (err) {
            //
        }
    }

    init()
})
</script>

<style lang="less" scoped>
.card {
    width: 65vw;
    margin: 10px auto 0;
    min-width: 800px;
    position: relative;

    .back {
        position: absolute;
        right: 25px;
        font-size: 1.6rem;
        text-decoration: none;
        z-index: 10;
    }
}
</style>
