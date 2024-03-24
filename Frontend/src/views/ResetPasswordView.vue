<template>
    <el-card class="card">
        <div class="login">
            <div class="box">
                <p class="table">重置密码</p>
                <br />
                <input class="input-content" type="text" placeholder="请输入新密码" v-model="new_password" />
                <input class="input-content" type="password" placeholder="请确认密码" v-model="new_password_confirm" />
                <br />
                <a href="#" class="go" @click="reset_password">确认</a>
            </div>
        </div>
    </el-card>
</template>

<script setup>
import { ref } from 'vue'
import { ElNotification } from 'element-plus'
import { useRouter } from 'vue-router'
import { SHA256Encrypt } from '@/utils/encrypt'
import { userApi } from '@/api'

const new_password = ref('')
const new_password_confirm = ref('')
const router = useRouter()

const reset_password = async () => {
    if (new_password.value !== new_password_confirm.value) {
        ElNotification({
            title: '两次密码不一致',
            type: 'warning'
        })
    } else {
        try {
            // 获得 url 中最后的 token 部分做验证
            const paths = router.currentRoute.value.fullPath.split('/')
            const url_path = paths[paths.length - 2]
            await userApi.resetPassword({
                url_path: url_path,
                new_password: SHA256Encrypt(new_password.value)
            })
            ElNotification({
                title: '更改成功',
                type: 'success'
            })
            router.push({ name: 'home' })
        } catch (err) {
            //
        }
    }
}
</script>

<style lang="less" scoped>
.card {
    width: 40%;
    margin: 20vh auto;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.table {
    font: 900 27px '';
    text-align: center;
    letter-spacing: 5px;
    color: #3d3d3d;
}

.input-content {
    width: 90%;
    height: 100%;
    margin-bottom: 16px;
    outline: none;
    border: 0;
    padding: 20px;
    background-color: transparent;
    border-bottom: 3px solid rgb(150, 150, 240);
    font: 700 15px '';
    color: gray;
}

.go {
    width: 95%;
    margin: 3vh auto;
    text-align: center;
    display: block;
    height: 24px;
    padding: 12px;
    border-radius: 10px;
    text-decoration: none;
    font: 700 1rem '';
    color: #fff;
    letter-spacing: 3px;
    background-image: linear-gradient(to left, #fd79a8, #a29bf6);
}
</style>
