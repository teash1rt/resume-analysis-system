import { req1 } from './request'

const getFavoriteResume = () => {
    return req1.get('/req1/resume/get-favorite/')
}

const addFavoriteResume = ({ rid }) => {
    return req1.post('/req1/resume/add-favorite/', { rid })
}

const cancelFavoriteResume = ({ rid }) => {
    return req1.post('/req1/resume/cancel-favorite/', { rid })
}

const downloadResume = ({ rid }) => {
    return req1.get('/req1/resume/download/', { params: { rid } })
}

const getTotalCount = () => {
    return req1.get('/req1/resume/get-total-count/')
}

const getPageResumesInfo = ({ page, pageSize, sortOrder }) => {
    return req1.get('/req1/resume/get-page-resumes-info/', { params: { page, pageSize, sortOrder } })
}

const getOneResumeInfo = ({ rid }) => {
    console.log(rid)
    return req1.get('/req1/resume/get-resume-info/', { params: { rid } })
}

const getUploadResumes = () => {
    return req1.get('/req1/resume/get-upload-resumes/')
}

const deleteUploadResume = ({ rid }) => {
    return req1.post('/req1/resume/del-upload-resume/', { rid })
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
    deleteUploadResume
}

export default resumeApi
