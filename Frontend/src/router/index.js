import { createRouter, createWebHistory } from 'vue-router'
import { show_login } from '@/utils/showLogin'
import { req1 } from '@/utils/request'
import pinia from '@/stores/store'
import { InfoStore } from '@/stores/InfoStore'
import { userApi } from '@/api'

const routes = [
    {
        path: '/',
        name: 'home',
        component: () => import('../views/HomeView.vue'),
        meta: {
            requestAuth: false,
            authority: -1
        }
    },
    {
        path: '/load/',
        name: 'loadview',
        component: () => import('../views/LoadView.vue'),
        meta: {
            requestAuth: false,
            authority: -1
        }
    },
    {
        path: '/upload/',
        name: 'uploadview',
        component: () => import('../views/UploadView.vue'),
        meta: {
            requestAuth: true,
            authority: 0
        }
    },
    {
        path: '/resumes/',
        name: 'resumesview',
        component: () => import('../views/ResumesView.vue'),
        meta: {
            requestAuth: true,
            authority: 1
        }
    },
    {
        path: '/data-visualization/',
        name: 'visualizationview',
        component: () => import('../views/VisualizationView.vue'),
        meta: {
            requestAuth: true,
            authority: 1
        }
    },
    {
        path: '/profile/',
        name: 'profileview',
        component: () => import('../views/ProfileView.vue'),
        meta: {
            requestAuth: true,
            authority: -1
        }
    },
    {
        path: '/reset-password/:url_path/',
        name: 'resetpasswordview',
        component: () => import('../views/ResetPasswordView.vue'),
        meta: {
            requestAuth: false,
            authority: -2
        }
    },
    {
        path: '/404/',
        name: '404',
        component: () => import('../views/NotFoundView.vue'),
        meta: {
            requestAuth: false,
            authority: -1
        }
    },
    {
        path: '/:catchAll(.*)',
        redirect: '/404/'
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

const infoStore = InfoStore(pinia)
router.beforeEach(async (to, _, next) => {
    if (to.meta.requestAuth === true) {
        try {
            const res = await userApi.tokenCheck({ token: infoStore.token })
            if (res.data === true) {
                if ((to.meta.authority === 0 && infoStore.type == 1) || (to.meta.authority === 1 && infoStore.type == 0)) {
                    next('/404/')
                }
                next()
            } else {
                infoStore.clear_info()
                show_login()
            }
        } catch (err) {
            //
        }
    } else if (to.meta.authority === -2) {
        req1.post('/req1/user/url-token-check/', {
            url_path: to.params.url_path
        })
            .then(res => {
                if (res.data === true) {
                    next()
                } else {
                    next('/404/')
                }
            })
            .catch(() => {})
    } else {
        next()
    }
})

export default router
