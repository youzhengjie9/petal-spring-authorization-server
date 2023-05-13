/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.auth.server.annotation;

import java.lang.annotation.*;


/**
 * 作用:
 * (1)允许所有人访问,服务调用不需要鉴权（只要加了这个@PermitAll注解就会生效）
 * (2)控制接口是否只能通过openfeign所访问,如果在浏览器通过访问gateway的方式访问该接口则会访问失败。（onlyFeignCall属性设置为true）
 *
 * @author youzhengjie
 * @date 2023/05/11 18:13:25
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PermitAll {


	/**
	 * 作用: 该接口是否只能通过openfeign所访问（如果在浏览器通过访问gateway的方式访问该接口则会访问失败。）
	 * <p>
	 * 如果我们不希望这个url可以直接被外网调用，仅能在Feign服务中调用则设置为true
	 * <p>
	 * 使用说明:
	 * 如果该PermitAll注解的onlyFeignCall属性值设置了为true,但是名为only_feign_call的请求头内容为空,则禁止访问,
	 * 也就是说要是想访问onlyFeignCall属性值设置了为true的接口,必须要放上一个名为only_feign_call的请求头
	 *
	 * @return boolean
	 */
	boolean onlyFeignCall() default true;

	/**
	 * 需要特殊判空的字段(预留)
	 */
	String[] field() default {};

}
