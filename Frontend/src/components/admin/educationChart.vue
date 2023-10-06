<template>
    <div class="chart" ref="edu_chart"></div>
</template>

<script setup>
import { ref, onMounted, getCurrentInstance } from 'vue'

const props = defineProps({
    edu_data: {
        type: Object,
        required: true
    }
})

const edu_data_handle = data => {
    data.sort((a, b) => b.count - a.count)
    return data.map(e => {
        return { value: e.count, name: e.education }
    })
}

const edu_chart = ref()
const { proxy } = getCurrentInstance()

onMounted(() => {
    const myChart = proxy.echarts.init(edu_chart.value)
    const option = {
        title: {
            text: '学历水平统计'
        },
        legend: {
            top: 'bottom'
        },
        tooltip: {
            trigger: 'item',
            formatter: '{b} : {c} ({d}%)'
        },
        series: [
            {
                name: 'Nightingale Chart',
                type: 'pie',
                radius: [50, 250],
                center: ['50%', '50%'],
                roseType: 'area',
                itemStyle: {
                    borderRadius: 8
                },
                data: edu_data_handle(props.edu_data)
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
