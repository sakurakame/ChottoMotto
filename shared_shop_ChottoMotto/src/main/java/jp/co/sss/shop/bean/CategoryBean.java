package jp.co.sss.shop.bean;

/**
 * カテゴリ情報クラス
 *
 */
public class CategoryBean {

	/** カテゴリID */
	private Integer id;
	
	/** カテゴリ名 */
	private String name;

	/** カテゴリ説明 */
	private String description;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
