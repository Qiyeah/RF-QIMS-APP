package com.zhilian.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * 封装 accessToken
 * 正确获取
 * {"accessToken":"PYtr70kAdrfdTQjCht8yEcJtfi1LYlUMivgRoJgrZEzNnzmUTm9gFnlif3OtyKmT","expiresIn":7200}
 * 获取错误
 * {"errCode":40001,"errMsg":"invalid credential"}
 */
public class AccessToken {
	
	private String accessToken;	// 正确获取到 accessToken 时有值
	private String key;	// 正确获取到 accessToken 时有值
	private Integer expiresIn;		// 正确获取到 accessToken 时有值,有效时长，单位为秒
	private Integer errCode;		// 出错时有值
	private String errMsg;			// 出错时有值
	
	private Long expiredTime;		// 正确获取到 accessToken 时有值，存放过期时间
	private String json;
	
	public AccessToken(String jsonStr) {
		this.json = jsonStr;
		
		try {
			@SuppressWarnings("rawtypes")
			Map map = new ObjectMapper().readValue(jsonStr, Map.class);
			accessToken = (String)map.get("accessToken");
			expiresIn = (Integer)map.get("expiresIn");
            key = (String)map.get("key");
			errCode = (Integer)map.get("errCode");
			errMsg = (String)map.get("errMsg");
			
			if (expiresIn != null)
				expiredTime = System.currentTimeMillis() + ((expiresIn -5) * 1000);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getJson() {
		return json;
	}
	
	public boolean isAvailable() {
		if (expiredTime == null)
			return false;
		if (errCode != null)
			return false;
		if (expiredTime < System.currentTimeMillis())
			return false;
		return accessToken != null;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	
	public Integer getExpiresIn() {
		return expiresIn;
	}
	
	public Integer getErrorCode() {
		return errCode;
	}
	
	public String getErrorMsg() {
		if (errCode != null) {
			String result = ReturnCode.get(errCode);
			if (result != null)
				return result;
		}
		return errMsg;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
