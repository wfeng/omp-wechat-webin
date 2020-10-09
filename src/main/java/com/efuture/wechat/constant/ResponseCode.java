/**
 * Copyright (C), 2007-2014, eFuture 北京富基融通科技有限公司
 * FileName:	ResponseCode.java
 * Author:		亮
 * Date:		2014-3-31 上午11:22:22
 * Description:	
 * History:
 * <author>		<time>			<version>		<description>
 * 
 */
package com.efuture.wechat.constant;

/**
 * @author 亮
 * @description
 * 
 */
public interface ResponseCode {
	final String SUCCESS = "0";
	final String FAILURE = "10000";
	final String EXIST_NO_PAY_ORDER = "30000";
	final String EXCEPTION = "50000";
	final String CALLREPEAT = "20000"; // 重复调用
	final String OVERLIMIT = "88888";
	final String SEQREPEAT = "99999";// 序号重复
	final String PROXY_NOT_DEFINE = "NOT_DEFINE";
	final String PROXY_BASE_OBJ = "PROXY_BASE_OBJ";
    

	interface Exception {
		final String SESSION_IS_EMPTY = "50001"; // Session为空
		final String ENTID_IS_EMPTY = "50002"; // 企业ID为空
		final String PARAM_IS_EMPTY = "50003"; // 参数为空
		final String SPECDATA_IS_EMPTY = "50004"; // 指定数据为空
		final String SPECDATA_IS_INVALID = "50005"; // 指定数据无效
	}

	interface Failure {
		final String ALREADY_EXISTS = "10001"; // 数据已存在
		final String ALREADY_PUBLISH = "10002"; // 数据已发布
		final String NOT_EXIST = "10003"; // 数据不存在
		final String BE_USE = "10004"; // 数据被使用
		final String DEPENDENCY_IS_ILLEGAL = "10005"; // 依赖KEY无效
		final String DEPENDENCY_NOT_EXIST = "10006"; // 依赖数据不存在
		final String IS_PARENT = "10007"; // 父级数据不能删除
		final String IS_NOT_LAST = "10008"; // 不是末级数据
		final String PROPERTY_MUST_SET = "10009"; // 品类属性必须设置
		final String PROPERTY_VALUE_INVALID = "10010"; // 品类属性值无效
	}
}
