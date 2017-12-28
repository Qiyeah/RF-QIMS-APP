package com.zhilian.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

//import org.dom4j.DocumentException;
//import com.jfinal.kit.StrKit;

public class InMsgParser {

	private InMsgParser() {
	}

	/**
	 * 从 json 中解析出各类消息
	 */
	public static InMsg parse(String json) {
		try {
			return doParse(json);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 消息类型 1：query 查询消息 2：save 保存消息 3：delete 删除消息
	 */
	private static InMsg doParse(String json) throws IOException {
		if (StrKit.notBlank(json)) {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(
					DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			InMsg inMsg = objectMapper.readValue(json, InMsg.class);
			String msgType = inMsg.getMsgType();
			if ("query".equals(msgType))
				return objectMapper.readValue(json, InQueryMsg.class);
			if ("save".equals(msgType))
				return objectMapper.readValue(json, InSaveMsg.class);
			if ("delete".equals(msgType))
				return objectMapper.readValue(json, InDeleteMsg.class);
			System.out.println("无法识别的消息类型!");
			return null;
		} else {
			System.out.println("无请求数据!");
			return null;
		}
	}

//	public static void main(String[] args) throws DocumentException {
//		String json = "{ " + "createTime:'1348831860'" + ",msgType:'query' "
//				+ ",queryName:'查询方法名' " + ",queryPara:{ " + "    name:'abc' "
//				+ "    ,code:'def' " + "} " + "    }";
//
//		InQueryMsg msg = (InQueryMsg) parse(json);
//		System.out.println(msg.getCreateTime());
//		System.out.println(msg.getMsgType());
//		System.out.println(msg.getQueryName());
//		System.out.println(msg.getQueryPara());
//	}
}
