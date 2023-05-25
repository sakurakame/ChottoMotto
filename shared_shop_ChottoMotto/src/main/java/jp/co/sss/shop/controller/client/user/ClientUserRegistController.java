package jp.co.sss.shop.controller.client.user;

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

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.UserDao;
import jp.co.sss.shop.util.Constant;

@Controller
public class ClientUserRegistController {

	@Autowired
	UserDao userRepository;

	@Autowired
	HttpSession session;

	@RequestMapping(path = "/client/user/regist/input/init", method = RequestMethod.GET)
	public String clientRegistInput() {
		UserForm userForm = new UserForm();
		session.setAttribute("userForm", userForm);
		return "redirect:/client/user/regist/input";
	}

	/**
	 * 入力画面　表示処理(POST) 一覧画面での新規ボタン押下後の処理
	 * 
	 * @return "redirect:/admin/user/regist/input" 入力画面　表示処理
	 */
	@RequestMapping(path = "/client/user/regist/input", method = RequestMethod.POST)
	public String registInput() {
		//セッションスコープより入力情報を取り出す
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		if (userForm == null) {
			userForm = new UserForm();
			userForm.setAuthority(((UserBean) session.getAttribute("user")).getAuthority());

			//空の入力フォーム情報をセッションに保持 登録ボタンからの遷移
			session.setAttribute("userForm", userForm);
		}

		//登録入力画面へリダイレクト
		return "redirect:/client/user/regist/input";
	}

	/**
	 * 入力画面　表示処理(GET)
	 * 
	 * @param model Viewとの値受渡し
	 * @return "admin/user/regist_input" 入力画面　表示
	 */
	@RequestMapping(path = "/client/user/regist/input", method = RequestMethod.GET)
	public String registInput(Model model) {
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		if (userForm == null) {
			// セッションに情報がない場合、システムエラー
			return "redirect:/syserror";
		}

		BindingResult result = (BindingResult) session.getAttribute("result");
		if (result != null) {
			//セッションにエラー情報がある場合、エラー情報をスコープに設定
			model.addAttribute("org.springframework.validation.BindingResult.userForm", result);
			// セッションのエラー情報は削除
			session.removeAttribute("result");
		}

		// 入力フォーム情報を表示
		model.addAttribute("userForm", userForm);

		// 入力画面
		return "client/user/regist_input";
	}

	/**
	 * 登録入力確認　処理
	 *
	 * @param form 入力フォーム
	 * @param result 入力値チェックの結果
	 * @return 
	 * 	入力値エラーあり："redirect:/admin/user/regist/input" 入力録画面　表示処理
	 * 	入力値エラーなし："redirect:/admin/user/regist/check" 登録確認画面　表示処理
	 */
	@RequestMapping(path = "/client/user/regist/check", method = RequestMethod.POST)
	public String registInputCheck(@Valid @ModelAttribute UserForm form, BindingResult result) {
		UserForm lastUserForm = (UserForm) session.getAttribute("userForm");
		if (lastUserForm == null) {
			// セッションに情報が無い場合、システムエラー
			return "redirect:/syserror";
		}
		if (form.getAuthority() == null) {
			//権限情報がない場合、セッション情報から値をセット
			form.setAuthority(lastUserForm.getAuthority());
		}
		// 入力フォーム情報をセッションに保持
		session.setAttribute("userForm", form);

		if (result.hasErrors()) {
			// 入力値にエラーがあった場合、エラー情報をセッションに保持
			session.setAttribute("result", result);
			// 登録入力画面へリダイレクト
			return "redirect:/client/user/regist/input";
		}

		// 登録確認画面へリダイレクト
		return "redirect:/client/user/regist/check";
	}

	/**
	 * 確認画面　表示処理
	 *
	 * @param model Viewとの値受渡し
	 * @return "admin/user/regist_check" 確認画面　表示
	 */
	@RequestMapping(path = "/client/user/regist/check", method = RequestMethod.GET)
	public String registCheck(Model model) {
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		if (userForm == null) {
			// セッションに情報がない場合、システムエラー
			return "redirect:/syserror";
		}
		//入力フォーム情報を表示
		model.addAttribute("userForm", userForm);

		//登録確認画面
		return "client/user/regist_check";
	}

	/**
	 * 情報登録処理
	 *
	 * @return "redirect:/admin/user/regist/complete" 登録完了画面　表示処理
	 */
	@RequestMapping(path = "/client/user/regist/complete", method = RequestMethod.POST)
	public String registComplete() {
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		if (userForm == null) {
			// セッションに情報がない場合、システムエラー
			System.out.println("エラー表示");
			return "redirect:/syserror";
		}

		userForm.setAuthority(Constant.AUTH_CLIENT);
		// 入力フォーム情報をDB登録
		User user = new User();
		BeanUtils.copyProperties(userForm, user);
		userRepository.save(user);

		// 使い終わったセッション情報削除
		session.removeAttribute("userForm");

		UserBean userBean = new UserBean();

		userBean.setId(user.getId());
		userBean.setName(user.getName());
		userBean.setAuthority(user.getAuthority());

		// セッションスコープにログインしたユーザの情報を登録
		session.setAttribute("user", userBean);

		//登録完了画面へリダイレクト
		return "redirect:/client/user/regist/complete";

	}

	/**
	 * 登録完了画面　表示処理
	 *
	 * @return "admin/user/regist_complete" 登録完了画面　表示
	 */
	@RequestMapping(path = "/client/user/regist/complete", method = RequestMethod.GET)
	public String registCompleteFinish() {
		return "client/user/regist_complete";
	}
}
