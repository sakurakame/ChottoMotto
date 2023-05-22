package jp.co.sss.shop.controller.admin.item;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.form.ItemForm;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.service.BeanTools;
import jp.co.sss.shop.util.Constant;

/**
 * 商品管理 削除機能のコントローラクラス
 *
 */
@Controller
public class AdminItemDeleteController {

	/** 商品情報 リポジトリ */
	@Autowired
	ItemRepository itemRepository;

	@Autowired
	HttpSession session;

	@Autowired
	BeanTools beanTools;

	/**
	 * 確認画面　表示処理
	 *
	 * @param id 削除対象ID
	 * @return "admin/item/delete_check" 削除確認画面　表示
	 */
	@RequestMapping(path = "/admin/item/delete/check/{id}", method = RequestMethod.POST)
	public String deleteCheck(@PathVariable Integer id) {
		Item item = itemRepository.findByIdAndDeleteFlag(id, Constant.NOT_DELETED);
		if (item == null) {
			// 削除対象が無い場合、システムエラー
			return "redirect:/syserror";
		}

		// 取得情報から表示フォーム情報を生成
		ItemForm itemForm = beanTools.copyEntityToItemForm(item);
		session.setAttribute("itemForm", itemForm);

		// 削除確認画面へリダイレクト
		return "redirect:/admin/item/delete/check";
	}

	/**
	 * 削除確認画面　表示処理(GET)
	 *
	 * @param model Viewとの値受渡し
	 * @return "admin/item/delete_check" 変更入力画面 表示
	 */
	@RequestMapping(path = "/admin/item/delete/check", method = RequestMethod.GET)
	public String deleteChecjView(Model model) {
		ItemForm itemForm = (ItemForm) session.getAttribute("itemForm");
		if (itemForm == null) {
			// セッションに情報がない場合、システムエラー
			return "redirect:/syserror";
		}
		model.addAttribute("itemForm", itemForm);

		// 削除確認画面
		return "admin/item/delete_check";
	}

	/**
	 * 削除処理、完了画面表示処理
	 *
	 * @return "redirect:/admin/item/delete/complete" 削除完了画面　表示
	 */
	@RequestMapping(path = "/admin/item/delete/complete", method = RequestMethod.POST)
	public String deleteComplete() {
		ItemForm itemForm = (ItemForm) session.getAttribute("itemForm");
		if (itemForm == null) {
			// セッションに情報がない場合、システムエラー
			return "redirect:/syserror";
		}

		Item item = itemRepository.findByIdAndDeleteFlag(itemForm.getId(), Constant.NOT_DELETED);
		if (item == null) {
			// 削除対象が無い場合、システムエラー
			return "redirect:/syserror";
		}
		// 削除フラグを立てる
		item.setDeleteFlag(Constant.DELETED);
		// 商品情報を保存
		itemRepository.save(item);

		// 使い終わったセッションの削除情報を削除
		session.removeAttribute("itemForm");

		// 削除完了画面へリダイレクト
		return "redirect:/admin/item/delete/complete";
	}

	/**
	 * 削除完了画面表示
	 * 
	 * @return "admin/item/delete_complete"  削除完了画面
	 */
	@RequestMapping(path = "/admin/item/delete/complete", method = RequestMethod.GET)
	public String deleteCompleteRedirect() {

		return "admin/item/delete_complete";
	}
}