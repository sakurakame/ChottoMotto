package jp.co.sss.shop.repository.salesforce;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import jp.co.sss.shop.entity.User;

@Service
public class UserFromSalesforce {

	@Autowired
	SalesfroceGetInfoService service;

	/**
	 * メールアドレスに該当する会員情報を検索（メールアドレスのみ） Validatorで利用
	 * @param email メールアドレス
	 * @return 会員エンティティ
	 */
	public User findByEmail(String email) {
		String soql = "SELECT ID__c,EMAIL__c,PASSWORD__c,NAME__c,POSTAL_CODE__c,ADDRESS__c,PHONE_NUMBER__c,AUTHORITY__c,"
				+ "DELETE_FLAG__c FROM APIUser__c WHERE EMAIL__c = \'" + email + "\'";
		JsonNode jsonNode = service.getInfo(soql);
		jsonNode = jsonNode.get("records").get(0);
		return new User(jsonNode.get("ID__c").asInt(), jsonNode.get("EMAIL__c").asText(),
				jsonNode.get("PASSWORD__c").asText(), jsonNode.get("NAME__c").asText(),
				jsonNode.get("POSTAL_CODE__c").asText(), jsonNode.get("ADDRESS__c").asText(),
				jsonNode.get("PHONE_NUMBER__c").asText(), jsonNode.get("AUTHORITY__c").asInt());
	}

	/**
	 * メールアドレスと削除フラグに該当する会員情報を検索 Validatorで利用
	 * @param email メールアドレス
	 * @param deleteFlag 削除フラグ
	 * @return 会員エンティティ
	 */
	public User findByEmailAndDeleteFlag(String email, int deleteFlag) {
		String soql = "SELECT ID__c,EMAIL__c,PASSWORD__c,NAME__c,POSTAL_CODE__c,ADDRESS__c,PHONE_NUMBER__c,AUTHORITY__c,"
				+ "DELETE_FLAG__c FROM APIUser__c WHERE EMAIL__c = \'" + email + "\' AND DELETE_FLAG__c = "
				+ deleteFlag;
		JsonNode jsonNode = service.getInfo(soql);
		jsonNode = jsonNode.get("records").get(0);
		return new User(jsonNode.get("ID__c").asInt(), jsonNode.get("EMAIL__c").asText(),
				jsonNode.get("PASSWORD__c").asText(), jsonNode.get("NAME__c").asText(),
				jsonNode.get("POSTAL_CODE__c").asText(), jsonNode.get("ADDRESS__c").asText(),
				jsonNode.get("PHONE_NUMBER__c").asText(), jsonNode.get("AUTHORITY__c").asInt());
	}

	/**
	 * 会員情報をログインユーザ権限を条件に登録日付降順に取得 (管理者機能で利用)
	 * @param deleteFlag 削除フラグ
	 * @param authority 権限
	 * @param pageable ページング情報
	 * @return 会員エンティティのページオブジェクト
	 */
	public Page<User> findUsersListOrderByInsertDate(@Param("deleteFlag") int deleteFlag,
			@Param("authority") int authority, Pageable pageable) {
		String soql = "SELECT ID__c,EMAIL__c,PASSWORD__c,NAME__c,POSTAL_CODE__c,ADDRESS__c,PHONE_NUMBER__c,AUTHORITY__c,"
				+ "DELETE_FLAG__c FROM APIUser__c WHERE AUTHORITY__c >= " + authority + " AND DELETE_FLAG__c = "
				+ deleteFlag + "ORDER BY INSERT_DATE__c DESC,ID__c DESC";
		JsonNode jsonNode = service.getInfo(soql);
		jsonNode = jsonNode.get("records");
		List<User> users = new ArrayList<>();
		for (int i = 0; i < jsonNode.size(); i++) {
			JsonNode json = jsonNode.get(i);
			users.add(new User(json.get("ID__c").asInt(), json.get("EMAIL__c").asText(),
					json.get("PASSWORD__c").asText(), json.get("NAME__c").asText(),
					json.get("POSTAL_CODE__c").asText(), json.get("ADDRESS__c").asText(),
					json.get("PHONE_NUMBER__c").asText(), json.get("AUTHORITY__c").asInt()));
		}
		Page<User> pageUsers = new PageImpl<>(users);
		return pageUsers;
	}

	/**
	 * 会員IDと削除フラグを条件に検索
	 * @param id 会員ID
	 * @param deleteFlg 削除フラグ
	 * @return 会員エンティティ
	 */
	public User findByIdAndDeleteFlag(Integer id, int deleteFlag) {
		String soql = "SELECT ID__c,EMAIL__c,PASSWORD__c,NAME__c,POSTAL_CODE__c,ADDRESS__c,PHONE_NUMBER__c,AUTHORITY__c,"
				+ "DELETE_FLAG__c FROM APIUser__c WHERE ID__c = \'" + id + "\' AND DELETE_FLAG__c = " + deleteFlag;
		JsonNode jsonNode = service.getInfo(soql);
		jsonNode = jsonNode.get("records").get(0);
		return new User(jsonNode.get("ID__c").asInt(), jsonNode.get("EMAIL__c").asText(),
				jsonNode.get("PASSWORD__c").asText(), jsonNode.get("NAME__c").asText(),
				jsonNode.get("POSTAL_CODE__c").asText(), jsonNode.get("ADDRESS__c").asText(),
				jsonNode.get("PHONE_NUMBER__c").asText(), jsonNode.get("AUTHORITY__c").asInt());
	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	public User save(User user) {
		return user;
	}

	/**
	 * 
	 * @return
	 */
	public Long count() {
		String soql = "SELECT NAME__c FROM APIUser__c";
		JsonNode jsonNode = service.getInfo(soql);
		jsonNode = jsonNode.get("records");
		return (long) jsonNode.size();
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public Optional<User> findById(Integer userId) {
		String soql = "SELECT ID__c,EMAIL__c,PASSWORD__c,NAME__c,POSTAL_CODE__c,ADDRESS__c,PHONE_NUMBER__c,AUTHORITY__c,"
				+ "DELETE_FLAG__c FROM APIUser__c WHERE ID__c = \'" + userId + "\' AND DELETE_FLAG__c = 0";
		JsonNode jsonNode = service.getInfo(soql);
		jsonNode = jsonNode.get("records").get(0);
		User user = new User(jsonNode.get("ID__c").asInt(), jsonNode.get("EMAIL__c").asText(),
				jsonNode.get("PASSWORD__c").asText(), jsonNode.get("NAME__c").asText(),
				jsonNode.get("POSTAL_CODE__c").asText(), jsonNode.get("ADDRESS__c").asText(),
				jsonNode.get("PHONE_NUMBER__c").asText(), jsonNode.get("AUTHORITY__c").asInt());
		return Optional.of(user);
	}
}