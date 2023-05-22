package jp.co.sss.shop.controller.admin.category;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.CategoryBean;
import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.form.CategoryForm;
import jp.co.sss.shop.repository.CategoryRepository;
import jp.co.sss.shop.service.BeanTools;
import jp.co.sss.shop.util.Constant;

/**
 * カテゴリ管理 削除機能のコントローラクラス
 *
 */
@Controller
public class AdminCategoryDeleteController {

	/** カテゴリ情報 リポジトリ */
	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	HttpSession session;

	@Autowired
	BeanTools beanTools;

	/**
	 * カテゴリ削除
	 *
	 * @param id 削除対象ID
	 * @return "redirect:/admin/category/delete/check" 削除確認画面　表示
	 */
	@RequestMapping(path = "/admin/category/delete/check/{id}", method = RequestMethod.POST)
	public String deleteCheck(@PathVariable Integer id) {
		// 削除対象の情報取得
		Category category = categoryRepository.findByIdAndDeleteFlag(id, Constant.NOT_DELETED);
		if (category == null) {
			// 削除対象無かったらシステムエラー
			return "redirect:/syserror";
		}
		CategoryForm categoryForm = new CategoryForm();
		BeanUtils.copyProperties(category, categoryForm);
		session.setAttribute("categoryForm", categoryForm);

		// 削除確認画面へリダイレクト
		return "redirect:/admin/category/delete/check";
	}

	/**
	 * カテゴリ削除確認
	 *
	 * @param model リクエストスコープ
	 * @return "admin/category/delete_check" 削除確認画面　表示
	 */
	@RequestMapping(path = "/admin/category/delete/check", method = RequestMethod.GET)
	public String deleteCheckView(Model model) {
		CategoryForm categoryForm = (CategoryForm) session.getAttribute("categoryForm");
		if (categoryForm == null) {
			// セッションに情報無かったらシステムエラー
			return "redirect:/syserror";
		}
		model.addAttribute("categoryForm", categoryForm);

		// 削除確認画面
		return "admin/category/delete_check";
	}

	/**
	 * カテゴリ削除
	 *
	 * @return "redirect:/admin/category/delete/complete" 削除完了画面　表示処理
	 */
	@RequestMapping(path = "/admin/category/delete/complete", method = RequestMethod.POST)
	public String deletetComplete() {
		CategoryForm categoryForm = (CategoryForm) session.getAttribute("categoryForm");
		if (categoryForm == null) {
			// セッションに情報無かったらシステムエラー
			return "redirect:/syserror";
		}

		// 削除対象の情報取得
		Category category = categoryRepository.findByIdAndDeleteFlag(categoryForm.getId(), Constant.NOT_DELETED);
		if (category == null) {
			// 削除対象無かったらシステムエラー
			return "redirect:/syserror";
		}

		// 削除
		category.setDeleteFlag(Constant.DELETED);
		categoryRepository.save(category);

		// カテゴリ情報を全件取得し、セッションに保存されたカテゴリ一覧情報を更新
		List<Category> categoryList = categoryRepository
				.findByDeleteFlagOrderByInsertDateDescIdDesc(Constant.NOT_DELETED);
		List<CategoryBean> categoryBeanList = beanTools.copyEntityListToCategoryBeanList(categoryList);
		session.setAttribute("categories", categoryBeanList);

		// 使い終わったセッション情報を削除
		session.removeAttribute("categoryForm");

		// 削除完了画面へリダイレクト
		return "redirect:/admin/category/delete/complete";
	}

	/**
	 * カテゴリ削除完了
	 *
	 * @return "admin/category/delete_complete" 削除完了画面表示
	 */
	@RequestMapping(path = "/admin/category/delete/complete", method = RequestMethod.GET)
	public String deletetCompleteRedirect() {
		return "admin/category/delete_complete";
	}
}