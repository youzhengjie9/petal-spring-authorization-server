import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

Vue.config.productionTip = false

//使用vue-router插件
Vue.use(router)
//使用elementui插件
Vue.use(ElementUI)

new Vue({
  el: '#app',
  router,
  render: h => h(App),
})



