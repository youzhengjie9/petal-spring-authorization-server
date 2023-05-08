<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
import dashboardIndex from './views/dashboard/index.vue';

export default {
  name: 'App',
  components: {
    dashboardIndex
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
