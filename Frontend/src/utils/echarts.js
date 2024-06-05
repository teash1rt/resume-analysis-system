import * as echarts from 'echarts/core'
import { PieChart, ScatterChart } from 'echarts/charts'
import {
    TitleComponent,
    TooltipComponent,
    GridComponent,
    DatasetComponent,
    TransformComponent,
    LegendComponent
} from 'echarts/components'
import { LabelLayout, UniversalTransition } from 'echarts/features'
import { CanvasRenderer } from 'echarts/renderers'

echarts.use([
    TitleComponent,
    TooltipComponent,
    GridComponent,
    DatasetComponent,
    TransformComponent,
    LegendComponent,
    PieChart,
    ScatterChart,
    LabelLayout,
    UniversalTransition,
    CanvasRenderer
])

export const echartsKey = Symbol('echarts')

export default echarts
