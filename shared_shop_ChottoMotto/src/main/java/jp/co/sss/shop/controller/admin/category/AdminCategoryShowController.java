package jp.co.sss.shop.controller.admin.category;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.CategoryBean;
import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.repository.CategoryRepository;
import jp.co.sss.shop.util.Constant;

/**
 * カテゴリ管理 一覧表示機能のコントローラクラス
 *
 */
@Controller
public class AdminCategoryShowController {

	/** カテゴリ情報 リポジトリ */
	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	HttpSession session;

	/**
	 * 一覧表示処理
	 *
	 * @param model Viewとの値受渡し
	 * @param pageable ページング制御
	 * @return "admin/category/list" カテゴリ情報 一覧画面へ
	 */
	@RequestMapping(path = "/admin/category/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String showCategoryList(Model model, Pageable pageable) {
		// 商品情報の登録数の取得と新規追加可否チェック
		Long categoriesCount = categoryRepository.count();
		Boolean registrable = true;
		if (categoriesCount == Constant.CATEGORIES_MAX_COUNT) {
			//カテゴリ情報の登録数が最大値の場合、新規追加不可
			registrable = false;
		}

		// カテゴリ情報を取得
		Page<Category> categoriesPage = categoryRepository
				.findByDeleteFlagOrderByInsertDateDescIdDescPage(Constant.NOT_DELETED, pageable);
		// カテゴリ情報をViewへ
		List<Category> categoryList = categoriesPage.getContent();
		model.addAttribute("pages", categoriesPage);
		model.addAttribute("categories", categoryList);
		model.addAttribute("registrable", registrable);

		// セッションにカテゴリ登録・変更・削除用情報あれば消す
		session.removeAttribute("categoryForm");

		return "admin/category/list";
	}

	/**
	 * 詳細画面表示 処理
	 *
	 * @param id ID 
	 * @param model Viewとの値受渡し
	 * @return "admin/category/detail" 詳細画面 表示
	 */
	@RequestMapping(path = "/admin/category/detail/{id}", method = { RequestMethod.GET, RequestMethod.POST })
	public String showCategory(@PathVariable Integer id, Model model) {
		Category category = categoryRepository.findByIdAndDeleteFlag(id, Constant.NOT_DELETED);
		if (category == null) {
			// 表示対象が無い場合、システムエラー
			return "redirect:/syserror";
		}

		// カテゴリ情報をViewへ
		CategoryBean categoryBean = new CategoryBean();
		BeanUtils.copyProperties(category, categoryBean);
		model.addAttribute("category", categoryBean);

		// セッションにカテゴリ登録・変更・削除用情報あれば消す
		session.removeAttribute("categoryForm");

		return "admin/category/detail";
	}
}