import { req1 } from '@/utils/request'

const login = ({ email: email, password: password }) => {
    return req1.post('/req1/user/login/', {
        email,
        password
    })
}

const register = ({ email: email, username: username, password: password, verify_code: verify_code }) => {
    return req1.post('/req1/user/register/', {
        email,
        username,
        password,
        verify_code
    })
}

const getUserInfo = () => {
    return req1.get(`/req1/user/get-info/`)
}

const forgetPassword = ({ email: email, verify_code: verify_code }) => {
    return req1.post('/req1/user/forget-password/', {
        email,
        verify_code
    })
}

const changeUsername = ({ new_username: new_username }) => {
    return req1.post('/req1/user/change-username/', {
        new_username
    })
}

const changeEmail = ({ new_email: new_email }) => {
    return req1.post('/req1/user/change-email/', {
        new_email
    })
}

const changePassword = ({ old_password: old_password, new_password: new_password }) => {
    return req1.post('/req1/user/change-password/', {
        old_password,
        new_password
    })
}

const userApi = {
    login,
    register,
    getUserInfo,
    forgetPassword,
    changeUsername,
    changeEmail,
    changePassword
}

export default userApi
