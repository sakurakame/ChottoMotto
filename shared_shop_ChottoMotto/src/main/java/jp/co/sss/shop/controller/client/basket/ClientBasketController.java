package jp.co.sss.shop.controller.client.basket;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.service.BeanTools;


@Controller
public class ClientBasketController {
	@Autowired
	private ItemRepository repository;
	
	@Autowired
	BeanTools beanTools;
	
	@RequestMapping(path = "/client/basket/list")
	public String showItemList(HttpSession session, Model model) {
		@SuppressWarnings({ "unchecked"})
		List<BasketBean> myBaskets = (List<BasketBean>)session.getAttribute("myBaskets");
		session.setAttribute("myBaskets", myBaskets);
		model.addAttribute("myBaskets", myBaskets);
		return "client/basket/list";
	}

	@RequestMapping(path = "/client/basket/add/{id}", method = RequestMethod.GET)
	public String addItemToBasket(HttpSession session,  @PathVariable("id") Integer id, Model model) {
			Item item = repository.findByIdQuery(id);
			ItemBean itemBean = beanTools.copyEntityToItemBean(item);
			model.addAttribute("item", itemBean);
			
			@SuppressWarnings("unchecked")
			List<BasketBean> myBaskets = (List<BasketBean>)session.getAttribute("myBaskets");
			if (myBaskets == null) {
				myBaskets = new ArrayList<BasketBean>();
			}
			
			boolean itemExists = false;
		    for (BasketBean basketItem : myBaskets) {
		        if (basketItem.getId().equals(item.getId())) {
		            basketItem.setOrderNum(basketItem.getOrderNum() + 1);
		            itemExists = true;
		            break;
		        }
		    }
		    
		    if (!itemExists) {
			BasketBean basketBean = new BasketBean();
			basketBean.setId(item.getId());
			basketBean.setName(item.getName());
			basketBean.setStock(item.getStock());
			basketBean.setOrderNum(1);
			myBaskets.add(basketBean);
			
		}
		    
		session.setAttribute("myBaskets", myBaskets);
		model.addAttribute("myBaskets", myBaskets);
			
			
		return "client/basket/list";
	}
	
	@RequestMapping(path = "/client/basket/delete")
	public String deleteItemFromBasket(@RequestParam("id") Integer id, HttpSession session, Model model) {
		@SuppressWarnings("unchecked")
		List<BasketBean> myBaskets = (List<BasketBean>)session.getAttribute("myBaskets");
		
		if (myBaskets != null) {
	        BasketBean itemToRemove = null;
	        for (BasketBean basket : myBaskets) {
	            if (basket.getId().equals(id)) {
	                if (basket.getOrderNum() > 1) {
	                    basket.setOrderNum(basket.getOrderNum() - 1);
	                } else {
	                    itemToRemove = basket;
	                }
	                break;
	            }
	        }
	        if (itemToRemove != null) {
	            myBaskets.remove(itemToRemove);
	        }

	        session.setAttribute("myBaskets", myBaskets);
	    }
		return "redirect:/client/basket/list";
	}

	@RequestMapping(path = "/client/basket/allDelete")
	public String deleteAllItemsFromBasket(HttpSession session, Model model) {
		@SuppressWarnings("unchecked")
		List<BasketBean> myBaskets = (List<BasketBean>)session.getAttribute("myBaskets");
		myBaskets.removeAll(myBaskets);
		return "redirect:/client/basket/list";
	}
}