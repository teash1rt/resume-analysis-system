import { req1 } from '@/utils/request'

const getStatisticsInfo = () => {
    return req1.get('/req1/statistics/get-statistics-info/')
}

const statisticsApi = {
    getStatisticsInfo
}

export default statisticsApi
