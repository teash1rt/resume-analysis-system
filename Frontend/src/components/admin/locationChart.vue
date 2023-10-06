<template>
    <div class="chart" ref="loc_chart"></div>
</template>

<script setup>
import { ref, onMounted, getCurrentInstance } from 'vue'

const props = defineProps({
    loc_data: {
        type: Object,
        required: true
    }
})

const loc_data_handle = data => {
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

const loc_chart = ref()
const { proxy } = getCurrentInstance()

onMounted(() => {
    const myChart = proxy.echarts.init(loc_chart.value)
    const data = loc_data_handle(props.loc_data)
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
