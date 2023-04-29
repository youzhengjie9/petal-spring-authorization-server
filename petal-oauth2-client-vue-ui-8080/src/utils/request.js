//二次封装axios
import axios from 'axios'

const service = axios.create({
  //oauth2客户端的ip地址
  baseURL: "http://127.0.0.1:8300",
  //6s没有响应就算超时
  timeout: 6000
})

//二次封装axios的使用（在api包下使用方式）
/*
1：get请求传参：（get请求传参是params属性）

export function 自定义的方法名(){
    return request({
        method: 'get',
        url: '/xxx',
        params: {
            参数1: '111',
            参数2: '222'
         }
    })
}

2：post请求传参：（post请求传参是data属性，后端要用@RequestBody接收）

export function 自定义的方法名(){
    return request({
        method: 'post',
        url: '/xxx',
        data: {
            参数1: '111',
            参数2: '222'
        }
    })
}


*/


//添加axios请求拦截器（在发送axios请求前自动执行）
service.interceptors.request.use(function (config) {
    return config;
  }, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
  });

//添加axios响应拦截器（axios请求发送后，后台返回数据给前端，当接收响应数据后自动执行）
service.interceptors.response.use(function (response) {

     return response; //记得要返回response。

}, function (error) {
    // 对响应错误做点什么
    return Promise.reject(error);
});


//对外暴露
export default service
