package jp.co.sss.shop.form;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import jp.co.sss.shop.annotation.CategoryCheck;

/**
 * カテゴリ情報のフォーム
 *
 * TIPS 入力チェックアノテーションのmessage属性に"{messages.propertiesで指定した名前}"と記述することができます。
 * 
 */
@CategoryCheck
public class CategoryForm implements Serializable {

	/** カテゴリID */
	private Integer id;

	/** カテゴリ名 */
	@NotBlank
	@Size(min = 1, max = 15, message = "{text.maxsize.message}")
	private String name;

	/** カテゴリ説明 */
	@Size(max = 30, message = "{text.maxsize.message}")
	private String description = "";

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

	@Override
	public String toString() {
		return "CategoryForm [id=" + id + ", name=" + name + ", description=" + description + "]";
	}
}