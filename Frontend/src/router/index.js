import { createRouter, createWebHistory } from 'vue-router'
import { showLogin } from '@/utils/showLogin'
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

router.beforeEach((to, _, next) => {
    if (to.meta.requestAuth === true) {
        userApi.tokenCheck({ token: infoStore.token }).then(res => {
            if (res.data === true) {
                if ((to.meta.authority === 0 && infoStore.type == 1) || (to.meta.authority === 1 && infoStore.type == 0)) {
                    next('/404/')
                }
                next()
            } else {
                infoStore.clearInfo()
                showLogin()
            }
        })
    } else if (to.meta.authority === -2) {
        userApi.urlTokenCheck({ urlPath: to.params.url_path }).then(res => {
            res.data ? next() : next('/404/')
        })
    } else {
        next()
    }
})

export default router
