import { req1 } from '@/utils/request'

const getFavoriteResume = () => {
    return req1.get(`/req1/resume/get-favorite/`)
}

const addFavoriteResume = ({ rid: rid }) => {
    return req1.post('/req1/resume/add-favorite/', {
        rid
    })
}

const cancelFavoriteResume = ({ rid: rid }) => {
    return req1.post('/req1/resume/cancel-favorite/', {
        rid
    })
}

const downloadResume = ({ rid: rid }) => {
    return req1.get(`/req1/resume/download/${rid}/`)
}

const getTotalCount = () => {
    return req1.get('/req1/resume/get-total-count/')
}

const getPageResumesInfo = ({ page: page, page_size: page_size, sort_order: sort_order }) => {
    return req1.get('/req1/resume/get-page-resumes-info/', {
        params: {
            page,
            page_size,
            sort_order
        }
    })
}

const getOneResumeInfo = ({ rid: rid }) => {
    return req1.get(`/req1/resume/get-one-resume-info/${rid}/`)
}

const getUploadResumes = () => {
    return req1.get(`/req1/resume/get-upload-resumes/`)
}

const deleteUploadResume = ({ rid: rid }) => {
    return req1.post('/req1/resume/del-upload-resume/', {
        rid
    })
}

const checkBeforeUpload = () => {
    return req1.get(`/req1/resume/check-before-upload/`)
}

const resumeApi = {
    getFavoriteResume,
    addFavoriteResume,
    cancelFavoriteResume,
    downloadResume,
    getTotalCount,
    getPageResumesInfo,
    getOneResumeInfo,
    getUploadResumes,
    deleteUploadResume,
    checkBeforeUpload
}

export default resumeApi
