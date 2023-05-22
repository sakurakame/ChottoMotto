package jp.co.sss.shop.form;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import jp.co.sss.shop.annotation.ItemCheck;

/**
 * 商品情報のフォーム
 *
 * TIPS 入力チェックアノテーションのmessage属性に"{messages.propertiesで指定した名前}"と記述することができます。
 * 
 */
@ItemCheck
public class ItemForm implements Serializable {

	/** 商品ID */
	private Integer id;

	/** 商品名 */
	@NotBlank
	@Size(min = 1, max = 100, message = "{text.maxsize.message}")
	private String name;

	/** 価格 */
	@NotNull
	@Max(9999999)
	private Integer price;

	/** 在庫数 */
	@NotNull
	@Max(9999)
	private Integer stock;

	/** 商品説明文 */
	@Size(min = 0, max = 400, message = "{text.maxsize.message}")
	private String description;

	/** 商品画像ファイル */
	private MultipartFile imageFile;

	/** 商品画像ファイル名 */
	private String image;

	/** カテゴリID */
	private Integer categoryId;

	/** カテゴリ名 */
	private String categoryName;

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

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public String toString() {
		return "ItemForm [id=" + id + ", name=" + name + ", price=" + price + ", stock=" + stock + ", description="
				+ description + ", imageFile=" + imageFile + ", image=" + image + ", categoryId=" + categoryId
				+ ", categoryName=" + categoryName + "]";
	}
}