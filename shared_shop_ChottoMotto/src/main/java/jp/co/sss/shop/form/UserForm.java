package jp.co.sss.shop.form;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import jp.co.sss.shop.annotation.EmailCheck;

/**
 * 会員情報入力フォーム
 *
 *  TIPS 入力チェックアノテーションのmessage属性に"{messages.propertiesで指定した名前}"と記述することができます。
 */
@EmailCheck
public class UserForm implements Serializable {
	/** 会員ID */
	private Integer	id;

	/** 会員メールアドレス */
	@NotBlank
	@Email
	private String	email;

	/** パスワード */
	@NotBlank
	@Size(min = 8, max = 16)
	@Pattern(regexp = "^[a-zA-Z0-9]+$")
	private String	password;

	/** 会員名 */
	@NotBlank
	@Size(min = 1, max = 30, message = "{text.maxsize.message}")
	private String	name;

	/** 郵便番号 */
	@NotBlank
	@Size(min = 7, max = 7, message = "{text.fixsize.message}")
	@Pattern(regexp = "^[0-9]+$", message = "{userRegist.numberpattern.message}")
	private String	postalCode;

	/** 住所 */
	@NotBlank
	@Size(min = 1, max = 150, message = "{text.maxsize.message}")
	private String	address;

	/** 電話番号 */
	@NotBlank
	@Size(min = 10, max = 11)
	@Pattern(regexp = "^[0-9]+$", message = "{userRegist.numberpattern.message}")
	private String	phoneNumber;

	/** 権限 */
	private Integer authority;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getAuthority() {
		return authority;
	}

	public void setAuthority(Integer authority) {
		this.authority = authority;
	}

	@Override
	public String toString() {
		return "UserForm [id=" + id + ", email=" + email + ", password=" + password + ", name=" + name + ", postalCode="
				+ postalCode + ", address=" + address + ", phoneNumber=" + phoneNumber + ", authority=" + authority
				+ "]";
	}
}