package jp.co.sss.shop.repository.salesforce;

import java.net.URI;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
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
		try {
			// "instance_url"と"access_token"の取得
			final JSONObject jsonObject = (JSONObject) new JSONTokener(json).nextValue();
			final String instanceUrl = jsonObject.getString("instance_url");
			final String accessToken = jsonObject.getString("access_token");

			// URLとHttpヘッダ組み立て→resuest
			String url = instanceUrl + "/services/data/v50.0/query/?q={soql}";
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization","Bearer " + accessToken);
			System.out.println("url="+url);
			RequestEntity<?> request = RequestEntity.get(url, soql).headers(headers).build();

			// RestTemplateのexchangeで処理呼んで、response取得
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.exchange(request, String.class);

			// responseのstatusCodeで処理成功か否か判定
			final HttpStatus statusCode = response.getStatusCode();
			if (statusCode.equals(HttpStatus.OK)) {
				queryResults = new ObjectMapper().readValue(response.getBody(), JsonNode.class);
			} else {
				System.err.println("Error Execute SOQL(" + soql + ") to Salesforce.com:" + statusCode);
			}
		} catch (HttpClientErrorException e) {
			//4xx系のエラー
			System.err.println("Error status=" + e.getRawStatusCode() + "," + e.getResponseBodyAsString());
		} catch (HttpServerErrorException e) {
			//5xx系のエラー
			System.err.println("Error status=" + e.getRawStatusCode() + "," + e.getResponseBodyAsString());
		} catch (Exception e) {
			System.err.println(e.getMessage() + ":" + e.getCause());
		}
		System.out.println("queryResults=" + queryResults);
		return queryResults;
	}
}