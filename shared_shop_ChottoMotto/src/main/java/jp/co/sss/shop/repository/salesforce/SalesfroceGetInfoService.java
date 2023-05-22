package jp.co.sss.shop.repository.salesforce;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

@Service
class SalesfroceGetInfoService {
	/**
	 * SalesforceでSOQLを実行し実行結果を取得
	 * 
	 * @param soql Salesforceで実行するSOQL
	 * @return 実行結果のJsonNodeオブジェクト
	 */
	JsonNode getInfo(String soql) {
		JsonNode jsonNode = null;
		String json = salesLoginRequest();
		if (json != null) {
			jsonNode = executeSOQL(soql, json);
		}
		return jsonNode;
	}

	@Value("${salseforce.host}")
	private String HOST;
	@Value("${salseforce.grant-service}")
	private String GRANT_SERVICE;
	@Value("${salseforce.client-id}")
	private String CLIENT_ID;
	@Value("${salseforce.client-secret}")
	private String CLIENT_SECRET;
	@Value("${salseforce.user-name}")
	private String USERNAME;
	@Value("${salseforce.password}")
	private String PASSWORD;
	@Value("${salseforce.security-token}")
	private String SECURITY_TOKEN;
	@Value("${salseforce.ua}")
	private String UA;

	/**
	 * Salesforceにログイン認証リクエストして、"instance_url"と"access_token"を取得
	 * @return "instance_url"と"access_token"のJSON文字列(nullならエラー)
	 */
	private String salesLoginRequest() {
		System.out.println("** SalesfroceGetInfoService.salesLoginReeuest() **");
		String getResult = null;

		
		System.out.println("getResult=" + getResult);
		return getResult;
	}

	/**
	 * SalesforceでSOQLを実行し実行結果を取得
	 * @param soql Salesforceで実行するSOQL
	 * @param json "instance_url"と"access_token"のJSON文字列
	 * @return 実行結果のJsonNodeオブジェクト
	 */
	private JsonNode executeSOQL(String soql, String json) {
		System.out.println("** SalesfroceGetInfoService.executeSOQL() **");
		JsonNode queryResults = null;

		
		
		System.out.println("queryResults=" + queryResults);
		return queryResults;
	}
}