package jp.co.sss.shop.controller.admin.category;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.CategoryBean;
import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.form.CategoryForm;
import jp.co.sss.shop.repository.CategoryRepository;
import jp.co.sss.shop.service.BeanTools;
import jp.co.sss.shop.util.Constant;

/**
 * カテゴリ管理 登録機能のコントローラクラス
 *
 */
@Controller
public class AdminCategoryRegistController {

	/** カテゴリ情報 リポジトリ */
	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	HttpSession session;

	@Autowired
	BeanTools beanTools;

	/**
	 * 入力画面　表示処理(POST)
	 * 
	 * @return "admin/category/regist_input" 入力画面　表示
	 */
	@RequestMapping(path = "/admin/category/regist/input", method = RequestMethod.POST)
	public String registInput() {
		CategoryForm categoryForm = (CategoryForm) session.getAttribute("categoryForm");
		if (categoryForm == null) {
			//無かったら、空の入力フォーム情報をセッションに保持 登録ボタンからの遷移
			session.setAttribute("categoryForm", new CategoryForm());
		}
		// 前回の内容が出ないようにリダイレクトで飛ばす
		return "redirect:/admin/category/regist/input";

	}

	/**
	 * 入力画面　表示処理(GET)
	 * 
	 * @param model Viewとの値受渡し
	 * @return "admin/category/regist_input" 登録入力画面　表示
	 */
	@RequestMapping(path = "/admin/category/regist/input", method = RequestMethod.GET)
	public String registInput(Model model) {
		CategoryForm categoryForm = (CategoryForm) session.getAttribute("categoryForm");
		if (categoryForm == null) {
			// セッションにカテゴリ情報がない場合、エラー
			return "redirect:/syserror";
		}
		//セッションから入力チェックエラー情報取得
		BindingResult result = (BindingResult) session.getAttribute("result");
		if (result != null) {
			//エラー情報がある場合、エラー情報をリクエストスコープに設定
			model.addAttribute("org.springframework.validation.BindingResult.categoryForm", result);
			//セッションのエラー情報は削除
			session.removeAttribute("result");
		}
		// 入力フォーム情報をモデルにセットし、画面表示させる
		model.addAttribute("categoryForm", categoryForm);

		return "admin/category/regist_input";
	}

	/**
	 * 入力情報確認処理
	 *
	 * @param form 入力フォーム
	 * @param result 入力値チェックの結果
	 * @return 
	 * 	入力値エラーあり："redirect:/admin/category/regist/input" 登録入力画面　表示処理
	 * 	入力値エラーなし："redirect:/admin/category/regist/check" 登録確認画面　表示処理
	 */
	@RequestMapping(path = "/admin/category/regist/check", method = RequestMethod.POST)
	public String registInputCheck(@Valid @ModelAttribute CategoryForm form, BindingResult result) {
		// 入力されたカテゴリ情報をセッションに保持
		session.setAttribute("categoryForm", form);

		if (result.hasErrors()) {
			// 入力値にエラーがあった場合、エラー情報をセッションに保持
			session.setAttribute("result", result);

			//登録入力画面へリダイレクト
			return "redirect:/admin/category/regist/input";
		}

		//登録確認画面へリダイレクト
		return "redirect:/admin/category/regist/check";
	}

	/**
	 * 確認画面　表示処理
	 *
	 * @param model Viewとの値受渡し
	 * @return "admin/category/regist_check" 確認画面表示
	 */
	@RequestMapping(path = "/admin/category/regist/check", method = RequestMethod.GET)
	public String registCheck(Model model) {
		CategoryForm categoryForm = (CategoryForm) session.getAttribute("categoryForm");
		if (categoryForm == null) {
			// セッションにカテゴリ情報がない場合、システムエラー
			return "redirect:/syserror";
		}
		// 入力フォーム情報をモデルにセットし、画面表示させる
		model.addAttribute("categoryForm", categoryForm);
		// 確認画面
		return "admin/category/regist_check";

	}

	/**
	 * 情報登録処理
	 *
	 * @return "redirect:/admin/category/regist/complete" 登録完了画面　表示処理
	 */
	@RequestMapping(path = "/admin/category/regist/complete", method = RequestMethod.POST)
	public String registComplete() {
		CategoryForm categoryForm = (CategoryForm) session.getAttribute("categoryForm");
		if (categoryForm == null) {
			// セッションにカテゴリ情報がない場合、システムエラー
			return "redirect:/syserror";
		}
		// 入力されたカテゴリ情報をDBに保存
		Category category = new Category();
		BeanUtils.copyProperties(categoryForm, category);
		category.setDeleteFlag(Constant.NOT_DELETED);
		categoryRepository.save(category);

		// 使い終わったセッション情報の削除
		session.removeAttribute("categoryForm");

		// 最新のカテゴリ情報を全件取得
		List<Category> categoryList = categoryRepository
				.findByDeleteFlagOrderByInsertDateDescIdDesc(Constant.NOT_DELETED);
		List<CategoryBean> categoryBeanList = beanTools.copyEntityListToCategoryBeanList(categoryList);

		// 最新のカテゴリ情報をセッションに保存・更新
		session.setAttribute("categories", categoryBeanList);

		// 登録完了画面へリダイレクト
		return "redirect:/admin/category/regist/complete";
	}

	/**
	 * 登録完了画面　表示処理
	 *
	 * @return "admin/category/regist_complete" 登録完了画面　表示
	 */
	@RequestMapping(path = "/admin/category/regist/complete", method = RequestMethod.GET)
	public String registCompleteFinish() {

		// 登録完了画面
		return "admin/category/regist_complete";
	}
}
