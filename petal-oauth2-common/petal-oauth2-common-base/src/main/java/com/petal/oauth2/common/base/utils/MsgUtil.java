//package com.petal.oauth2.common.base.utils;
//
//import lombok.experimental.UtilityClass;
//import org.springframework.context.MessageSource;
//
//import java.util.Locale;
//
//
///**
// * i18n工具类
// *
// * @author youzhengjie
// * @date 2023/05/09 23:53:23
// */
//@UtilityClass
//public class MsgUtil {
//
//	/**
//	 * 通过code 获取中文错误信息
//	 * @param code
//	 * @return
//	 */
//	public String getMessage(String code) {
//		MessageSource messageSource = SpringContextHolder.getBean("messageSource");
//		return messageSource.getMessage(code, null, Locale.CHINA);
//	}
//
//	/**
//	 * 通过code 和参数获取中文错误信息
//	 * @param code
//	 * @return
//	 */
//	public String getMessage(String code, Object... objects) {
//		MessageSource messageSource = SpringContextHolder.getBean("messageSource");
//		return messageSource.getMessage(code, objects, Locale.CHINA);
//	}
//
//	/**
//	 * security 通过code 和参数获取中文错误信息
//	 * @param code
//	 * @return
//	 */
//	public String getSecurityMessage(String code, Object... objects) {
//		MessageSource messageSource = SpringContextHolder.getBean("securityMessageSource");
//		return messageSource.getMessage(code, objects, Locale.CHINA);
//	}
//
//}
