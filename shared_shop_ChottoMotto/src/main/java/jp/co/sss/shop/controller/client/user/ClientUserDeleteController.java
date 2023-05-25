package jp.co.sss.shop.controller.client.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.UserDao;
import jp.co.sss.shop.util.Constant;

@Controller
public class ClientUserDeleteController {

	@Autowired
	UserDao userRepository;

	@Autowired
	HttpSession session;

	// /delete/check/{id},Method:POST
	@RequestMapping(path = "/client/user/delete/check/{id}", method = RequestMethod.POST)
	public String deleteCheck(@PathVariable Integer id) {
		User user = userRepository.findByIdAndDeleteFlag(id, Constant.NOT_DELETED);
		if (user == null) {
			// 削除対象が無い場合、システムエラー
			return "redirect:/syserror";
		}

		// 取得情報から表示フォーム情報を生成
		UserForm userForm = new UserForm();
		BeanUtils.copyProperties(user, userForm);
		session.setAttribute("userForm", userForm);

		//退会確認画面へリダイレクト
		return "redirect:/client/user/delete/check";
	}

	// /delete/check Method:GET
	@RequestMapping(path = "/client/user/delete/check", method = RequestMethod.GET)
	public String updateInput(Model model) {
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		if (userForm == null) {
			// セッションに情報がない場合、システムエラー
			return "redirect:/syserror";
		}
		// 入力フォーム情報を画面表示設定
		model.addAttribute("userForm", userForm);

		// 削除確認画面
		return "client/user/delete_check";
	}

	// /delete/complete, Method:POST
	@RequestMapping(path = "/client/user/delete/complete", method = RequestMethod.POST)
	public String deleteComplete() {
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		if (userForm == null) {
			// セッションに情報がない場合、システムエラー
			return "redirect:/syserror";
		}
		User user = userRepository.findByIdAndDeleteFlag(userForm.getId(), Constant.NOT_DELETED);
		if (user == null) {
			// 削除対象が無い場合、システムエラー
			return "redirect:/syserror";
		}

		// 削除フラグを立てる
		user.setDeleteFlag(Constant.DELETED);
		userRepository.save(user);

		// 使い終わったセッション情報を削除
		session.removeAttribute("userForm");

		//セッションスコープの情報を破棄
		session.invalidate();

		// 削除完了画面へリダイレクト
		return "redirect:/client/user/delete/complete";
	}

	// /delete/complete, Method:GET
	@RequestMapping(path = "/client/user/delete/complete", method = RequestMethod.GET)
	public String deleteCompleteFinish() {
		return "client/user/delete_complete";
	}
}
