package jp.co.sss.shop.controller.client.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	public String showUser( Model model) {
		Integer id = ((UserBean) session.getAttribute("user")).getId();
		User user = userRepository.findByIdAndDeleteFlag(id , Constant.NOT_DELETED);
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
		
		System.out.println("あいうえお");
		// 詳細画面
		return "client/user/detail";
	}

}
