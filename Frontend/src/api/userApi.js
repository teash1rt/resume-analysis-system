import { req1, req2 } from './request'

const login = ({ email, password }) => {
    return req1.post('/req1/user/login/', { email, password })
}

const register = ({ email, username, password, verifyCode }) => {
    return req1.post('/req1/user/register/', { email, username, password, verifyCode })
}

const getUserInfo = () => {
    return req1.get('/req1/user/get-info/')
}

const forgetPassword = ({ email, verifyCode }) => {
    return req1.post('/req1/user/forget-password/', { email, verifyCode })
}

const changeUsername = ({ newUsername }) => {
    return req1.post('/req1/user/change-username/', { newUsername })
}

const changeEmail = ({ newEmail }) => {
    return req1.post('/req1/user/change-email/', { newEmail })
}

const changePassword = ({ oldPassword, newPassword }) => {
    return req1.post('/req1/user/change-password/', { oldPassword, newPassword })
}

const getAvatar = () => {
    return req1.get('/req1/user/get-avatar/')
}

const tokenCheck = ({ token }) => {
    return req1.post('/req1/user/visitor-token-check/', { token })
}

const resetPassword = ({ urlPath, newPassword }) => {
    return req1.post('/req1/user/reset-password/', { urlPath, newPassword })
}

const checkApplicationStatus = () => {
    return req1.post('/req1/user/check-application-status/')
}

const getApplyVerifyCode = () => {
    return req1.get('/req1/user/get-apply-verify-code/')
}

const applyPermission = ({ email, purpose, description, verifyCode }) => {
    return req1.post('/req1/user/apply-permission/', { email, purpose, description, verifyCode })
}

const getAuthorize = () => {
    return req2.get('/req2/authorize/')
}

const urlTokenCheck = ({ urlPath }) => {
    return req1.post('/req1/user/url-token-check/', { urlPath })
}

const userApi = {
    login,
    register,
    getUserInfo,
    forgetPassword,
    changeUsername,
    changeEmail,
    changePassword,
    getAvatar,
    tokenCheck,
    resetPassword,
    checkApplicationStatus,
    getApplyVerifyCode,
    applyPermission,
    getAuthorize,
    urlTokenCheck
}

export default userApi
