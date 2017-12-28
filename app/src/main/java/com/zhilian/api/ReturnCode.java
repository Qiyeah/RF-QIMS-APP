package com.zhilian.api;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信接口全局返回码
 */
public class ReturnCode {
	
    @SuppressWarnings("serial")
	private static final Map<Integer, String> errCodeToErrMsg = new HashMap<Integer, String>(){{
    	put(-1, "系统繁忙");
    	put(0, "请求成功");
    	put(40001, "获取access_token时AppSecret错误，或者access_token无效");
    	put(40002, "不合法的凭证类型");
    	put(40003, "不合法的UserID");
    	put(40004, "不合法的媒体文件类型");
    	put(40005, "不合法的文件类型");
    	put(40006, "不合法的文件大小");
    	put(40007, "不合法的媒体文件id");
    	put(40008, "不合法的消息类型");
    	put(40009, "不合法的图片文件大小");//官方文档无此项 2015年4月3日10:18:54
    	put(40010, "不合法的语音文件大小");//官方文档无此项 2015年4月3日10:18:54
    	put(40011, "不合法的视频文件大小");//官方文档无此项 2015年4月3日10:18:54
    	put(40012, "不合法的缩略图文件大小");//官方文档无此项 2015年4月3日10:18:54
    	put(40013, "不合法的APPID(corpid)");
    	put(40014, "不合法的access_token");
    	put(40015, "不合法的菜单类型");
    	put(40016, "不合法的按钮个数");
    	put(40017, "不合法的按钮个数");
    	put(40018, "不合法的按钮名字长度");
    	put(40019, "不合法的按钮KEY长度");
    	put(40020, "不合法的按钮URL长度");
    	put(40021, "不合法的菜单版本号");
    	put(40022, "不合法的子菜单级数");
    	put(40023, "不合法的子菜单按钮个数");
    	put(40024, "不合法的子菜单按钮类型");
    	put(40025, "不合法的子菜单按钮名字长度");
    	put(40026, "不合法的子菜单按钮KEY长度");
    	put(40027, "不合法的子菜单按钮URL长度");
    	put(40028, "不合法的自定义菜单使用成员");
    	put(40029, "不合法的oauth_code");
    	put(40030, "不合法的refresh_token");
    	put(40031, "不合法的UserID列表");
    	put(40032, "不合法的UserID列表长度");
    	put(40033, "不合法的请求字符，不能包含\\uxxxx格式的字符");
    	put(40035, "不合法的参数");
    	put(40038, "不合法的请求格式");
    	put(40039, "不合法的URL长度");
    	put(40040, "不合法的插件token");
    	put(40041, "不合法的插件id");
    	put(40042, "不合法的插件会话");
    	put(40048, "url中包含不合法domain");
    	put(40050, "不合法的分组id");//官方文档无此项 2015年4月3日10:18:54
    	put(40051, "分组名字不合法");//官方文档无此项 2015年4月3日10:18:54
        put(40054, "不合法的子菜单url域名");
        put(40055, "不合法的按钮url域名");
        put(40056, "不合法的agentid");
        put(40057, "不合法的callbackurl");
        put(40058, "不合法的红包参数");
        put(40059, "不合法的上报地理位置标志位");
        put(40060, "设置上报地理位置标志位时没有设置callbackurl");
        put(40061, "设置应用头像失败");
        put(40062, "不合法的应用模式");
        put(40063, "红包参数为空");
        put(40064, "管理组名字已存在");
        put(40065, "不合法的管理组名字长度");
        put(40066, "不合法的部门列表");
        put(40067, "标题长度不合法");
        put(40068, "不合法的标签ID");
        put(40069, "不合法的标签ID列表");
        put(40070, "列表中所有标签（成员）ID都不合法");
        put(40071, "不合法的标签名字，标签名字已经存在");
        put(40072, "不合法的标签名字长度");
        put(40073, "不合法的UserId");
        put(40074, "news消息不支持指定为高保密消息");
        put(40077, "不合法的预授权码");
        put(40078, "不合法的临时授权码");
        put(40079, "不合法的授权信息");
        put(40080, "不合法的suitesecret");
        put(40082, "不合法的suitetoken");
        put(40083, "不合法的suiteid");
        put(40084, "不合法的永久授权码");
        put(40085, "不合法的suiteticket");
        put(40086, "不合法的第三方应用appid");
    	put(41001, "缺少access_token参数");
    	put(41002, "缺少appid(corpid)参数");
    	put(41003, "缺少refresh_token参数");
    	put(41004, "缺少secret参数");
    	put(41005, "缺少多媒体文件数据");
    	put(41006, "缺少media_id参数");
    	put(41007, "缺少子菜单数据");
    	put(41008, "缺少oauth code");
    	put(41009, "缺少UserID");
    	put(41010, "缺少url");
    	put(41011, "缺少agentid");
    	put(41012, "缺少应用头像mediaid");
    	put(41013, "缺少应用名字");
    	put(41014, "缺少应用描述");
    	put(41015, "缺少Content");
    	put(41016, "缺少标题");
    	put(41017, "缺少标签ID");
    	put(41018, "缺少标签名字");
    	put(41021, "缺少suiteid");
    	put(41022, "缺少suitetoken");
    	put(41023, "缺少suiteticket");
    	put(41024, "缺少suitesecret");
    	put(41025, "缺少永久授权码");
    	put(42001, "access_token超时");
    	put(42002, "refresh_token超时");
    	put(42003, "oauth_code超时");
    	put(42004, "插件token超时");
    	put(42007, "预授权码失效");
    	put(42008, "临时授权码失效");
    	put(42009, "suitetoken失效");
    	put(43001, "需要GET请求");
    	put(43002, "需要POST请求");
    	put(43003, "需要HTTPS请求");
    	put(43004, "需要成员已关注");
    	put(43005, "需要好友关系");
    	put(43006, "需要订阅");
    	put(43007, "需要授权");
    	put(43008, "需要支付授权");
    	put(43010, "需要处于回调模式");
    	put(43011, "需要企业授权");
    	put(44001, "多媒体文件为空");
    	put(44002, "POST的数据包为空");
    	put(44003, "图文消息内容为空");
    	put(44004, "文本消息内容为空");
    	put(45001, "多媒体文件大小超过限制");
    	put(45002, "消息内容超过限制");
    	put(45003, "标题字段超过限制");
    	put(45004, "描述字段超过限制");
    	put(45005, "链接字段超过限制");
    	put(45006, "图片链接字段超过限制");
    	put(45007, "语音播放时间超过限制");
    	put(45008, "图文消息超过限制");
    	put(45009, "接口调用超过限制");
    	put(45010, "创建菜单个数超过限制");
    	put(45015, "回复时间超过限制");
    	put(45016, "系统分组，不允许修改");
    	put(45017, "分组名字过长");
    	put(45018, "分组数量超过上限");
    	put(45024, "账号数量超过上限");
    	put(45027, "mpnews消息每天只能发送100次");
    	put(46001, "不存在媒体数据");
    	put(46002, "不存在的菜单版本");
    	put(46003, "不存在的菜单数据");
    	put(46004, "不存在的成员");
    	put(47001, "解析JSON/XML内容错误");
    	put(48001, "api功能未授权");
    	put(48002, "api功能未授权");
    	put(48003, "api功能未授权");
    	put(48004, "api功能未授权");
    	put(50001, "redirect_uri未授权");
    	put(50002, "redirect_uri未授权");
    	put(50003, "redirect_uri未授权");
    	put(50004, "redirect_uri未授权");
    	put(50005, "redirect_uri未授权");
    	put(60001, "redirect_uri未授权");
    	put(60001, "redirect_uri未授权");
    	put(60002, "redirect_uri未授权");
    	put(60003, "redirect_uri未授权");
    	put(60004, "redirect_uri未授权");
    	put(60005, "redirect_uri未授权");
    	put(60006, "redirect_uri未授权");
    	put(60007, "redirect_uri未授权");
    	put(60008, "redirect_uri未授权");
    	put(60009, "redirect_uri未授权");
    	put(60010, "redirect_uri未授权");
    	put(60011, "redirect_uri未授权");
    	put(60012, "redirect_uri未授权");
    	put(60013, "redirect_uri未授权");
    	put(60014, "redirect_uri未授权");
    	put(60015, "redirect_uri未授权");
    	put(60016, "redirect_uri未授权");
    	put(60017, "redirect_uri未授权");
    	put(60018, "redirect_uri未授权");
    	put(60019, "redirect_uri未授权");
    	put(60020, "redirect_uri未授权");
    	put(60023, "redirect_uri未授权");
    	put(60102, "redirect_uri未授权");
    	put(60103, "redirect_uri未授权");
    	put(60104, "redirect_uri未授权");
    	put(60105, "redirect_uri未授权");
    	put(60106, "redirect_uri未授权");
    	put(60107, "redirect_uri未授权");
    	put(60108, "redirect_uri未授权");
    	put(60109, "redirect_uri未授权");
    	put(60110, "部门个数超出限制");
    	put(60111, "UserID不存在");
    	put(60112, "成员姓名不合法");
    	put(60113, "身份认证信息（微信号/手机/邮箱）不能同时为空");
    	put(60114, "性别不合法");
    	put(60115, "已关注成员微信不能修改");
    	put(60118, "成员无有效邀请字段（微信，邮箱，手机号）");
    	put(60119, "成员已关注");
    	put(60120, "成员已禁用");
    	put(60121, "找不到该成员");
    	put(60122, "邮箱已被外部管理员使用");
    	put(60123, "无效的部门id");
    	put(60124, "无效的父部门id");
    	put(60125, "部门名字长度超过限制");
    	put(60126, "创建部门失败");
    	put(60127, "缺少部门id");
    	put(60128, "字段不合法，可能存在主键冲突或者格式错误");
    	put(80001, "可信域名没有IPC备案，后续将不能在该域名下正常使用jssdk");
    	put(82001, "发送消息或者邀请的参数全部为空或者全部不合法");
    	put(82002, "不合法的PartyID列表长度");
    	put(82003, "不合法的TagID列表长度");

    }};
    
    /**
     * 通过返回码获取返回信息
     */
    public static String get(int errCode){
    	String result = errCodeToErrMsg.get(errCode);
    	return result !=null? result : "未知返回码：" + errCode;
    }
}




