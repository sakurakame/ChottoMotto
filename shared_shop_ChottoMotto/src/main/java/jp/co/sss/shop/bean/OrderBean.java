package jp.co.sss.shop.bean;

/**
 * 注文情報クラス
 *
 */
public class OrderBean {

	/** 注文ID */
	private Integer id;

	/** 注文日付 */
	private String insertDate;

	/** 支払方法 */
	private Integer payMethod;

	/** 合計金額 */
	private Integer total;

	/** お届け先郵便番号 */
	private String postalCode;

	/** お届け先住所 */
	private String address;

	/** お届け先宛名 */
	private String name;

	/** お届け先電話番号 */
	private String phoneNumber;

	/** 注文会員名 */
	private String userName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(String insertDate) {
		this.insertDate = insertDate;
	}

	public Integer getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(Integer payMethod) {
		this.payMethod = payMethod;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}