<template>
    <div class="chart" ref="chart" />
</template>

<script setup>
import { ref, onMounted, inject } from 'vue'
import { echartsKey } from '@/utils/echarts'

const props = defineProps({
    data: {
        type: Object,
        required: true
    }
})

const dataParse = data => {
    data.sort((a, b) => b.count - a.count)
    return data.map(e => {
        return { value: e.count, name: e.education }
    })
}

const chart = ref()
const echarts = inject(echartsKey)

onMounted(() => {
    const myChart = echarts.init(chart.value)
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
                data: dataParse(props.data)
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
