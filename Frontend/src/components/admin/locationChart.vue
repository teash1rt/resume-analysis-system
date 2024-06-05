<template>
    <div class="chart" ref="chart" />
</template>

<script setup>
import { ref, onMounted, getCurrentInstance } from 'vue'

const props = defineProps({
    data: {
        type: Object,
        required: true
    }
})

const dataParse = data => {
    const legendData = []
    const seriesData = []
    data.forEach(val => {
        legendData.push(val.location)
        seriesData.push({
            name: val.location,
            value: val.count
        })
    })
    return {
        legendData: legendData,
        seriesData: seriesData
    }
}

const chart = ref()
const { proxy } = getCurrentInstance()

onMounted(() => {
    const myChart = proxy.echarts.init(chart.value)
    const data = dataParse(props.data)
    const option = {
        title: {
            text: '简历来源地统计'
        },
        tooltip: {
            trigger: 'item',
            formatter: '{b} : {c} ({d}%)'
        },
        legend: {
            type: 'scroll',
            orient: 'vertical',
            right: 160,
            top: 100,
            data: data.legendData
        },
        series: [
            {
                type: 'pie',
                radius: '65%',
                center: ['42%', '50%'],
                data: data.seriesData,
                emphasis: {
                    itemStyle: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    }
    myChart.setOption(option)
})
</script>

<style lang="less" scoped>
.chart {
    width: 70vw;
    height: 75vh;
    margin: 0 auto;
}
</style>
