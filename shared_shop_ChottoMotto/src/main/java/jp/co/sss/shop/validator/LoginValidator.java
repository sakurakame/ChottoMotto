package jp.co.sss.shop.validator;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import jp.co.sss.shop.annotation.LoginCheck;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.repository.UserDao;
import jp.co.sss.shop.util.Constant;

/**
 * ログインチェックの独自検証クラス
 *
 */
public class LoginValidator implements ConstraintValidator<LoginCheck, Object> {
	private String email;
	private String password;

	@Autowired
	UserDao userRepository;

	@Autowired
	HttpSession session;

	@Override
	public void initialize(LoginCheck annotation) {
		this.email = annotation.fieldEmail();
		this.password = annotation.fieldPassword();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		BeanWrapper beanWrapper = new BeanWrapperImpl(value);
		boolean isValidFlg = false;
		String emailProp = (String) beanWrapper.getPropertyValue(this.email);
		String passwordProp = (String) beanWrapper.getPropertyValue(this.password);

		User user = userRepository.findByEmailAndDeleteFlag(emailProp, Constant.NOT_DELETED);
		if (user != null && passwordProp.equals(user.getPassword())) {
			UserBean userBean = new UserBean();

			userBean.setId(user.getId());
			userBean.setName(user.getName());
			userBean.setAuthority(user.getAuthority());

			// セッションスコープにログインしたユーザの情報を登録
			session.setAttribute("user", userBean);
			isValidFlg = true;
		} else {
			//ユーザ認証に失敗
			isValidFlg = false;
		}
		return isValidFlg;
	}
}