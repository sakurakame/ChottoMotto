package jp.co.sss.shop.repository.salesforce;

import java.io.IOException;
import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

		String loginUrl = "https://login.salesforce.com/services/oauth2/token";

		System.out.println("** SalesfroceGetInfoService.salesLoginReeuest() **");

		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.add("grant_type", "password");
		requestBody.add("client_id", CLIENT_ID);
		requestBody.add("client_secret", CLIENT_SECRET);
		requestBody.add("username", USERNAME);
		requestBody.add("password", PASSWORD + SECURITY_TOKEN);

		// リクエストヘッダーを設定
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		// RequestEntityを作成
		RequestEntity<MultiValueMap<String, String>> request = RequestEntity.post(URI.create(loginUrl)).headers(headers)
				.body(requestBody);

		// RestTemplateでリクエストを送信
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(request, String.class);

		// レスポンスの本文を取得
		String getResult = response.getBody();
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

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode;
		
		//jsonをJsonNodeに変換
		 try {
		        jsonNode = objectMapper.readTree(json);
		    } catch (IOException e) {
		        e.printStackTrace();
		        return queryResults;
		    }
		 
		String instanceUrl = jsonNode.get("instance_url").asText();
		String accessToken = jsonNode.get("access_token").asText();

		// HttpHeaders作って、それを使ってRequestEntity作成
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + accessToken);
		RequestEntity<?> request = RequestEntity.get(URI.create(instanceUrl)).headers(headers).build();

		// RestTemplate作成
		RestTemplate restTemplate = new RestTemplate();
		// RestTemplateでGET実行
		ResponseEntity<String> response = restTemplate.exchange(request, String.class);
		
		// レスポンスの本文を取得
	    String responseBody = response.getBody();
	    
	 // レスポンスボディをJsonNodeに変換
	    try {
	        queryResults = objectMapper.readTree(responseBody);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

		System.out.println("queryResults=" + queryResults);
		return queryResults;
	}
}