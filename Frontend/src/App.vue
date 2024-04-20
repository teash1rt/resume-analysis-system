<template>
    <div class="app-container" v-if="!infoStore.is_mobile">
        <navBar />
        <div class="content-container">
            <router-view />
        </div>
    </div>
    <div v-else>
        <MobileView />
    </div>
</template>

<script setup>
import navBar from '@/components/common/navBar.vue'
import MobileView from './views/MobileView.vue'
import { InfoStore } from '@/stores/InfoStore'

const infoStore = InfoStore()
</script>

<style>
.app-container {
    position: relative;
}

.content-container {
    padding-top: 90px;
}

html {
    font-size: 10px;
}

body {
    background-image: url(./assets/bc.jpg);
    background-size: cover;
    background-attachment: fixed;
}
</style>
