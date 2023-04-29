import Vue from 'vue'
//导入路由插件
import VueRouter from 'vue-router'
//导入进度条nprogress
import NProgress from 'nprogress'
//导入进度条nprogress样式
import 'nprogress/nprogress.css'


//解决Vue路由重复跳转报错,要放到Vue.use(VueRouter)之前
const routerPush = VueRouter.prototype.push;
VueRouter.prototype.push = function (location) {
    return routerPush.call(this, location).catch(err => {})
};

//安装路由插件
Vue.use(VueRouter);

//配置动态路由，这里存放的路由都是静态路由，也就是不管什么用户都一定会有的路由
const router = new VueRouter({
  mode:'history',
  routes: [
    {
      path:'/res',
      component: () =>import('../views/login/index.vue')
    },
  ]
});

//配置全局路由守卫（进入路由前自动执行）
router.beforeEach((to, from, next) => {
  //当进入路由前进度条开启
  NProgress.start();
  next();
})
//配置全局路由守卫（退出路由后自动执行）
router.afterEach(() => {
  //当退出路由后进度条关闭
  NProgress.done()
})

//导出路由
export default router
