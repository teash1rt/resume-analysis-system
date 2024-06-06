import { req1 } from './request'

const getStatisticsInfo = () => {
    return req1.get('/req1/statistics/get-statistics-info/')
}

const statisticsApi = {
    getStatisticsInfo
}

export default statisticsApi
