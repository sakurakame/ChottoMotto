package jp.co.sss.shop.controller.client.user;

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

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.repository.UserDao;
import jp.co.sss.shop.util.Constant;

/**
 * 会員管理 表示機能(一般会員)のコントローラクラス
 *
 */

@Controller
public class ClientUserShowController {
	
	@Autowired
	UserDao userRepository;	
	
	@Autowired
	HttpSession session;
	
	@RequestMapping(path = "/client/user/detail", method = { RequestMethod.GET, RequestMethod.POST })
	public String showUserList(Model model, Pageable pageable) {
		Boolean registrable = true;

		// 会員情報リストを取得
		Integer authority = ((UserBean) session.getAttribute("user")).getAuthority();
		Page<User> userList = userRepository.findUsersListOrderByInsertDate(Constant.NOT_DELETED, authority, pageable);

		// 会員情報をViewに渡す
		model.addAttribute("registrable", registrable);
		model.addAttribute("pages", userList);
		model.addAttribute("users", userList.getContent());

		// セッションの会員登録・変更・削除用情報あれば消す
		session.removeAttribute("userForm");

		// 一覧表示s
		return "client/user/detail";
	}
	
	@RequestMapping(path = "/client/user/detail/{id}", method = { RequestMethod.GET, RequestMethod.POST })
	public String showUser(@PathVariable int id, Model model) {
		User user = userRepository.findByIdAndDeleteFlag(id, Constant.NOT_DELETED);
		if (user == null) {
			//表示対象がない場合、システムエラー
			return "redirect:/syserror";
		}
		
		// Userエンティティの各フィールドの値を表示
		UserBean userBean = new UserBean();
		BeanUtils.copyProperties(user, userBean);
		model.addAttribute("userBean", userBean);
		
		//セッションの会員登録・変更・削除用情報あれば消す
		session.removeAttribute("userForm");
		
		// 詳細画面
		return "client/user/detail";
	}

}
