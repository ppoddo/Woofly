package com.kh.woofly.account.model.service;

import java.util.HashMap;

public interface KakaoLoginService {

	String getAccessToken(String code);

	HashMap<String, Object> getUserInfo(String accessToken);

}
