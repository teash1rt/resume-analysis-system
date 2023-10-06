<template>
    <div class="chart" ref="exp_chart"></div>
</template>

<script setup>
import { ref, onMounted, getCurrentInstance } from 'vue'

const props = defineProps({
    exp_data: {
        type: Object,
        required: true
    }
})

// 二维转三维 第三维为出现的次数
const exp_data_handle = data => {
    const arr = []
    data.total_company_count.forEach((val, idx) => {
        arr.push([val, data.total_work_time[idx]])
    })

    const mp = {}
    arr.forEach(e => {
        const key = JSON.stringify(e)
        mp[key] = mp[key] ? mp[key] + 1 : 1
    })

    const res = Object.entries(mp).map(([key, value]) => {
        const item = JSON.parse(key)
        return [...item, value]
    })
    return res
}

const exp_chart = ref()
const { proxy } = getCurrentInstance()

onMounted(() => {
    const myChart = proxy.echarts.init(exp_chart.value)
    const option = {
        title: {
            text: '工作经验统计',
            padding: [2, 20, 200, 0]
        },
        grid: {
            top: '15%',
            bottom: '5%'
        },
        xAxis: {
            name: '工作单位数量'
        },
        yAxis: {
            name: '工作年限'
        },
        tooltip: {
            trigger: 'item',
            formatter: '{c0}'
        },
        series: [
            {
                data: exp_data_handle(props.exp_data),
                symbolSize: data => {
                    // 做对数处理
                    return 60 * Math.log10(data[2] + 1)
                },
                type: 'scatter',
                center: ['60%', '50%']
            }
        ]
    }
    myChart.setOption(option)
})
</script>

<style lang="less" scoped>
.chart {
    width: 65vw;
    height: 65vh;
    margin: 2vh auto;
}
</style>
