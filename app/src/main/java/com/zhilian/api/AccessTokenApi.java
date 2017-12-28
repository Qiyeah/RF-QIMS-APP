package com.zhilian.api;

import java.util.Map;

/**
 * 认证并获取 access_token API
 * http://qydev.weixin.qq.com/wiki/index.php?title=主动调用
 */
public class AccessTokenApi {

    private static String url = "http://192.168.9.125:8085/Api/getToken";

    private static AccessToken accessToken;

    public static AccessToken getAccessToken() {
        if (accessToken != null && accessToken.isAvailable())
            return accessToken;

        refreshAccessToken();
        return accessToken;
    }

    public static void refreshAccessToken() {
        accessToken = requestAccessToken();
    }

    private static synchronized AccessToken requestAccessToken() {
        AccessToken result = null;
        ApiConfig ac = ApiConfigKit.getApiConfig();
        for (int i = 0; i < 3; i++) {
            String appId = ac.getAppId();
            String appSecret = ac.getAppSecret();
            String type = ac.getType();
            final Map<String, String> queryParas = ParaMap.create("corpId", appId)
                    .put("corpSecret", appSecret)
                    .put("type", type)
                    .getData();

            //String json = RequestUtil.post(url,queryParas,null);
            String json = "";
            result = new AccessToken(json);

            if (result.isAvailable())
                break;
        }
        return result;
    }

}
