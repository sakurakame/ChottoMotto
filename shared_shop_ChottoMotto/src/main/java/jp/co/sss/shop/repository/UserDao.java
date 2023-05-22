package jp.co.sss.shop.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.repository.salesforce.UserFromSalesforce;

/** 
 * UserDao【UserはUserRepositoryでなくこちらを使う事】
 */
@Service
public class UserDao {

	@Value("${salseforce.enabled}")
	private boolean isSalesforce;

	@Autowired
	private UserRepository repository;

	@Autowired
	private UserFromSalesforce salesforceRepo;

	/**
	 * メールアドレスに該当する会員情報を検索（メールアドレスのみ） Validatorで利用
	 * @param email メールアドレス
	 * @return 会員エンティティ
	 */
	public User findByEmail(String email) {
		if (isSalesforce) {
			return salesforceRepo.findByEmail(email);
		} else {
			return repository.findByEmail(email);
		}
	}

	/**
	 * メールアドレスと削除フラグに該当する会員情報を検索 Validatorで利用
	 * @param email メールアドレス
	 * @param deleteFlag 削除フラグ
	 * @return 会員エンティティ
	 */
	public User findByEmailAndDeleteFlag(String email, int deleteFlag) {
		if (isSalesforce) {
			return salesforceRepo.findByEmailAndDeleteFlag(email, deleteFlag);
		} else {
			return repository.findByEmailAndDeleteFlag(email, deleteFlag);
		}
	}

	/**
	 * 会員情報をログインユーザ権限を条件に登録日付降順に取得 (管理者機能で利用)
	 * @param deleteFlag 削除フラグ
	 * @param authority 権限
	 * @param pageable ページング情報
	 * @return 会員エンティティのページオブジェクト
	 */
	public Page<User> findUsersListOrderByInsertDate(@Param("deleteFlag") int deleteFlag,
			@Param("authority") int authority,
			Pageable pageable) {
		if (isSalesforce) {
			return salesforceRepo.findUsersListOrderByInsertDate(deleteFlag, authority, pageable);
		} else {
			return repository.findUsersListOrderByInsertDate(deleteFlag, authority, pageable);
		}
	}

	/**
	 * 会員IDと削除フラグを条件に検索
	 * @param id 会員ID
	 * @param deleteFlg 削除フラグ
	 * @return 会員エンティティ
	 */
	public User findByIdAndDeleteFlag(Integer id, int deleteFlg) {
		if (isSalesforce) {
			return salesforceRepo.findByIdAndDeleteFlag(id, deleteFlg);
		} else {
			return repository.findByIdAndDeleteFlag(id, deleteFlg);
		}
	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	public User save(User user) {
		if (isSalesforce) {
			return salesforceRepo.save(user);
		} else {
			return repository.save(user);
		}
	}

	/**
	 * 
	 * @return
	 */
	public Long count() {
		if (isSalesforce) {
			return salesforceRepo.count();
		} else {
			return repository.count();
		}
	}
}