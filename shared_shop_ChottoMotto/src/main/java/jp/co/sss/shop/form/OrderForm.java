package jp.co.sss.shop.form;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 注文情報入力フォーム
 *
 * TIPS 入力チェックアノテーションのmessage属性に"{messages.propertiesで指定した名前}"と記述することができます。
 * 
 */
public class OrderForm implements Serializable {

	/** 会員ID */
	private Integer id;

	/** 送付先郵便番号 */
	@NotBlank
	@Size(min = 7, max = 7, message = "{text.fixsize.message}")
	@Pattern(regexp = "^[0-9]+$", message = "{userRegist.numberpattern.message}")
	private String postalCode;

	/** 送付先住所 */
	@NotBlank
	@Size(min = 1, max = 150, message = "{text.maxsize.message}")
	private String address;

	/** 送付先氏名 */
	@NotBlank
	@Size(min = 1, max = 30, message = "{text.maxsize.message}")
	private String name;

	/** 送付先電話番号 */
	@NotBlank
	@Size(min = 10, max = 11)
	@Pattern(regexp = "^[0-9]+$", message = "{userRegist.numberpattern.message}")
	private String phoneNumber;

	/** 支払い方法 */
	private Integer payMethod;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(Integer payMethod) {
		this.payMethod = payMethod;
	}

	@Override
	public String toString() {
		return "OrderForm [id=" + id + ", postalCode=" + postalCode + ", address=" + address + ", name=" + name
				+ ", phoneNumber=" + phoneNumber + ", payMethod=" + payMethod + "]";
	}
}