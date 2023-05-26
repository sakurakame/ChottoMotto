package jp.co.sss.shop.controller.client.order;

import java.util.ArrayList;
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

import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.bean.OrderItemBean;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.form.OrderForm;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderRepository;
import jp.co.sss.shop.repository.UserDao;
import jp.co.sss.shop.util.Constant;

@Controller
public class ClientOrderRegistController {
	@Autowired
	UserDao userDao;

	@Autowired
	HttpSession session;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	ItemRepository itemRepository;

	
	
	//1
	@RequestMapping(path = "/client/order/address/input", method = RequestMethod.POST)
	public String handleOrderAddressInput(Model model) {

		// orderFormオブジェクトを作成
		OrderForm orderForm = new OrderForm();

		// セッションスコープからログイン会員情報を取得
		orderForm.setId(((UserBean) session.getAttribute("user")).getId());

		// 注文入力フォーム情報の支払方法に初期値とsしてクレジットカードを設定
		orderForm.setPayMethod(Constant.DEFAULT_PAYMENT_METHOD);

		// 注文入力フォーム情報をセッションスコープに保存
		session.setAttribute("orderForm", orderForm);

		// 届け先入力画面表示処理にリダイレクト
		return "redirect:/client/order/address/input";
	}
	//2
	@RequestMapping(value = "/client/order/address/input", method = RequestMethod.GET)
	public String showAddressInputPage(Model model) {
		// セッションスコープから注文入力フォーム情報を取得
		OrderForm orderForm = (OrderForm) session.getAttribute("orderForm");

		BindingResult result = (BindingResult) session.getAttribute("result");
		if (result != null) {
			//セッションにエラー情報がある場合、エラー情報をスコープに設定
			model.addAttribute("org.springframework.validation.BindingResult.orderForm", result);
			// セッションのエラー情報は削除
			session.removeAttribute("result");
		}

		// 注文入力フォーム情報をリクエストスコープに設定
		model.addAttribute("orderForm", orderForm);

		// 登録画面表示処理にフォワード
		return "client/order/address_input";
	}

	//3
	@RequestMapping(path = "/client/order/payment/input", method = RequestMethod.POST)
	public String processPaymentInput(@Valid @ModelAttribute OrderForm orderForm, BindingResult bindingResult) {
		// 画面から入力されたフォーム情報を注文入力フォーム情報として保存
		session.setAttribute("orderForm", orderForm);

		// BindingResultオブジェクトに入力エラー情報がある場合
		if (bindingResult.hasErrors()) {
			// エラー情報をセッションスコープに保存
			session.setAttribute("errorInfo", bindingResult.getAllErrors());

			// 届け先入力画面表示処理にリダイレクト
			return "redirect:/client/order/address/input";
		}

		// 入力エラーがない場合、支払方法選択画面表示処理にリダイレクト
		return "redirect:/client/order/payment/input";
	}

	//4
	@RequestMapping(path = "/client/order/payment/input", method = RequestMethod.GET)
	public String showPaymentInput(Model model) {
		// セッションスコープから注文入力フォーム情報を取得
		OrderForm orderForm = (OrderForm) session.getAttribute("orderForm");
		
		// 注文入力フォーム情報をリクエストスコープに設定
		model.addAttribute("orderForm", orderForm);
		// 支払方法選択画面を表示する
		return "client/order/payment_input";
	}

	//5
	@RequestMapping(path = "/client/order/check", method = RequestMethod.POST)
	public String processOrderCheck(@ModelAttribute("orderForm") OrderForm orderForm) {
		// セッションスコープから注文入力フォーム情報を取得
		OrderForm OrderForm = (OrderForm) session.getAttribute("orderForm");

		// 画面から入力された支払方法を取得した注文入力フォーム情報に設定
		OrderForm.setPayMethod(orderForm.getPayMethod());

		// 注文入力フォーム情報をセッションスコープに保存
		session.setAttribute("orderForm", OrderForm);

		// 注文確認画面表示処理にリダイレクト
		return "redirect:/client/order/check";
	}

	//6
	@RequestMapping(path = "/client/order/check", method = RequestMethod.GET)
	public String showOrderCheckPage(Model model, HttpSession session, OrderItem orderItems) {
		// セッションスコープから注文情報を取得
		OrderForm orderForm = (OrderForm) session.getAttribute("orderForm");
		
		// セッションスコープから買い物かご情報を取得
		@SuppressWarnings("unchecked")
		List<BasketBean>  myBaskets = (List<BasketBean>)session.getAttribute("myBaskets");

		
		//セッションにエラー情報がある場合、エラー情報をスコープに設定
		BindingResult result = (BindingResult) session.getAttribute("result");
		if (result != null) {
			//セッションにエラー情報がある場合、エラー情報をスコープに設定
			model.addAttribute("org.springframework.validation.BindingResult.orderForm", result);
			// セッションのエラー情報は削除
			session.removeAttribute("result");
		}

		/** 各種入れ物用意とセッションから「買い物かご」 */
		// 「最新買い物かご」用意 "List<BasketBean> newBasketBeans"
		// 「在庫不足商品名」用意 "List<String> lessItemNames"
		// 「在庫無し商品名」用意 "List<String> zeroItemNames"
		// 「注文情報」用意 "List<OrderItemBean> orderItemBeanList"
		
		
		List<BasketBean> newBasketBeans = new ArrayList<>();
		List<String> lessItemName = new ArrayList<>();
		List<String> zeroItemNames = new ArrayList<>();
		List<OrderItemBean> oredItemBeans = new ArrayList<>();
		OrderItemBean orderItemBean = new OrderItemBean();
		Item item;
		
		// 注文商品の最新情報をDBから取得し、在庫チェックをする
		for(BasketBean goods :myBaskets) {
			goods.getName();
			item = itemRepository.getReferenceById(goods.getId());
			if(item.getStock() == 0)
			{
				continue;
			}
			else if(item.getStock() < goods.getOrderNum())
			{
				goods.setOrderNum(item.getStock());
			}
			orderItemBean.setId(goods.getId());
			
			oredItemBeans.add(orderItemBean);
			
		}
		

		//			// 注文商品の最新情報をDBから取得し、在庫チェックをする
		//			List<Integer> orderItem = orderForm.getOrderItems();
		//			for (OrderItem item : orderItem) {
		//				// 最新の在庫情報をDBから取得
		//				Product product = productService.getProductById(item.getProductId());
		//	
		//				// 在庫チェックと注文数の調整
		//				if (item.getQuantity() > product.getStock()) {
		//					// 在庫不足の場合は警告メッセージを設定し、注文数を在庫数に調整
		//					model.addAttribute("warningMessage", "在庫が不足しています");
		//					item.setQuantity(product.getStock());
		//				}
		//			}
		//	
		//			// 在庫切れ商品の削除
		//			orderItems.removeIf(item -> item.getQuantity() <= 0);
		//	
		//			// 在庫状況を反映した買い物かご情報をセッションに保存
		//			session.setAttribute("cart", cart);
		//	
		//			// 買い物かご情報から商品ごとの金額小計と合計金額を算出し、注文入力フォーム情報に設定
		//			BigDecimal subtotal = BigDecimal.ZERO;
		//			for (CartItem cartItem : cartItems) {
		//				BigDecimal itemPrice = cartItem.getProduct().getPrice()
		//						.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
		//				subtotal = subtotal.add(itemPrice);
		//			}
		// 注文入力フォーム情報をリクエストスコープに設定
		model.addAttribute("oredItemBeans", oredItemBeans);

		// 注文確認画面を表示する
		return "client/order/check";
	}

	//7
	@RequestMapping(path = "/client/order/payment/back", method = RequestMethod.POST)
	public String redirectToPaymentInput() {
		// 支払方法選択画面表示処理にリダイレクト
		return "redirect:/client/order/payment/input";
	}

	//8
	@RequestMapping(path = "/client/order/complete", method = RequestMethod.POST)
	public String completeOrder(Model model, HttpSession session) {
		// セッションスコープから注文情報を取得
		OrderForm orderForm = (OrderForm) session.getAttribute("orderForm");
		
		// セッションスコープから買い物かご情報を取得
		@SuppressWarnings("unchecked")
		List<BasketBean>  myBaskets = (List<BasketBean>)session.getAttribute("myBaskets");

		// 注文入力フォーム情報をセッションスコープに保存
		model.addAttribute("orderForm", orderForm);
		


		//		// 注文商品の在庫チェックをする
		//		List<OrderItem> orderItems = order.getOrderItems();
		//		for (OrderItem item : orderItems) {
		//			// 在庫切れ商品がある場合は、注文確認画面表示処理にリダイレクト
		//			Product product = productService.getProductById(item.getProductId());
		//			if (item.getQuantity() > product.getStock()) {
		//				return "redirect:/client/order/check";
		//			}
		//		} 
		Order order = new Order();
		BeanUtils.copyProperties(orderForm, order);
		orderRepository.save(order);
		
		//		// 注文情報を元にDB登録用エンティティオブジェクトを生成し、注文テーブルおよび注文商品テーブルに登録を実施
		//		orderService.placeOrder(order);
		//
		//		// セッションスコープの注文入力フォーム情報と買い物かご情報を削除
		//		session.removeAttribute("order");
		//		session.removeAttribute("myBaskets");

		// 注文完了画面表示処理にリダイレクト
		return "redirect:/client/order/complete";
	}

	//8
	@RequestMapping(path = "/client/order/complete", method = RequestMethod.GET)
	public String showOrderCompletePage() {
		// 注文完了画面を表示する
		return "client/order/complete";
	}

}
