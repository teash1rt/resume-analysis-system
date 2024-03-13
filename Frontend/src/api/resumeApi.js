import { req1 } from '@/utils/request'

const getFavoriteResume = () => {
    return req1.get(`/req1/resume/get-favorite/`)
}

const cancelFavoriteResume = ({ rid: rid }) => {
    return req1.post('/req1/resume/cancel-favorite/', {
        rid
    })
}

const downloadResume = ({ rid: rid }) => {
    return req1.get(`/req1/resume/download/${rid}/`)
}

const resumeApi = {
    getFavoriteResume,
    cancelFavoriteResume,
    downloadResume
}

export default resumeApi
