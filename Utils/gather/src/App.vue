<template>
  <div class="app-container">
    <LoadView />
    <div class="content-container">
      <router-view />
    </div>
  </div>
</template>

<script setup>
import LoadView from '@/components/LoadView.vue'
</script>

<style>
.app-container {
  position: relative;
}

.content-container {
  margin-top: 8%;
  /* 设置与Navbar高度相等的上边距 */
}

/* body {
  background-image: url(./assets/bc.png);
  background-size: cover;
} */

</style>
