import { req1 } from './request'

const getFavoriteResumes = () => {
    return req1.get('/req1/resume/get-favorite/')
}

const favoriteResume = ({ rid, isFavorite }) => {
    return req1.post('/req1/resume/favorite-resume/', { rid, isFavorite })
}

const downloadResume = ({ rid }) => {
    return req1.get('/req1/resume/download/', { params: { rid } })
}

const getPageResumesInfo = ({ page, pageSize, sortOrder }) => {
    return req1.get('/req1/resume/get-page-info/', { params: { page, pageSize, sortOrder } })
}

const getResumeInfo = ({ rid }) => {
    return req1.get('/req1/resume/get-resume-info/', { params: { rid } })
}

const getUploadResumes = () => {
    return req1.get('/req1/resume/get-upload-resumes/')
}

const deleteUploadResume = ({ rid }) => {
    return req1.post('/req1/resume/del-upload-resume/', { rid })
}

const resumeApi = {
    getFavoriteResumes,
    favoriteResume,
    downloadResume,
    getPageResumesInfo,
    getResumeInfo,
    getUploadResumes,
    deleteUploadResume
}

export default resumeApi
