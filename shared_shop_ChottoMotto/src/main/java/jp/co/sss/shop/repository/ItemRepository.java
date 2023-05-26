package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.Item;

/**
 * itemsテーブル用リポジトリ
 *
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

	/**
	 * 商品情報を登録日付順に取得 管理者機能で利用
	 * @param deleteFlag 削除フラグ
	 * @param pageable ページング情報
	 * @return 商品エンティティのページオブジェクト
	 */
	@Query("SELECT i FROM Item i INNER JOIN i.category c WHERE i.deleteFlag =:deleteFlag ORDER BY i.insertDate DESC,i.id DESC")
	Page<Item> findByDeleteFlagOrderByInsertDateDescPage(
	        @Param(value = "deleteFlag") int deleteFlag, Pageable pageable);

	/**
	 * 商品IDと削除フラグを条件に検索（管理者機能で利用）
	 * @param id 商品ID
	 * @param deleteFlag 削除フラグ
	 * @return 商品エンティティ
	 */
	public Item findByIdAndDeleteFlag(Integer id, int deleteFlag);

	/**
	 * 商品名と削除フラグを条件に検索 (ItemValidatorで利用)
	 * @param name 商品名
	 * @param notDeleted 削除フラグ
	 * @return 商品エンティティ
	 */
	public Item findByNameAndDeleteFlag(String name, int notDeleted);
	
	@Query("SELECT i FROM Item i WHERE i.id = :id")
	public Item findByIdQuery(@Param("id") Integer id);
	
	
	@Query("SELECT i FROM Item i")
	public List<Item> findAllItems();
	
	@Query("SELECT i FROM Item i ORDER BY i.id DESC")
	public List<Item> findAllItemsDESC();
	
	@Query("SELECT i FROM Item i WHERE categoryId=1")
	public List<Item> findCategoryFood();
	
	@Query("SELECT i FROM Item i WHERE categoryId=2")
	public List<Item> findCategoryNotFood();
	
	@Query("SELECT i FROM Item i")
	public Item findAllItemCategories();
	
	@Query("SELECT new Item(i.id, i.name, i.price,i.description, i.image, c.name) FROM Item i INNER JOIN i.category c INNER JOIN i.orderItemList oi WHERE i.deleteFlag = 0 GROUP BY i.id, i.name, i.price,i.description, i.image, c.name ORDER BY COUNT(i.id) DESC,i.id ASC")
	public List<Item> findItemOrderBySales();
	
	@Query("SELECT  i FROM Item i WHERE i.deleteFlag = 0 AND i.category.id = :categoryId")
    public List<Item> findByCategoryId(@Param("categoryId") Integer categoryId);
}
