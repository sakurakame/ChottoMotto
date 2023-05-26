	package jp.co.sss.shop.controller.client.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderItemRepository;
import jp.co.sss.shop.service.BeanTools;

/**
 * 商品管理 一覧表示機能(一般会員用)のコントローラクラス
 *
 */
@Controller
public class ClientItemShowController {
	/** 商品情報 */
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	OrderItemRepository orderItemRepository;

	@Autowired
	BeanTools beanTools;
	
	/**
	 * トップ画面 表示処理
	 *
	 * @param model    Viewとの値受渡し
	 * @return "index" トップ画面
	 */
	@RequestMapping(path = "/" , method = { RequestMethod.GET, RequestMethod.POST })
	public String index(Model model) {
		List<Item> items = itemRepository.findItemOrderBySales();
		model.addAttribute("items",items);
		
		
		
		return "index";
	}
	
	@RequestMapping(path="/client/item/list/{sortType}",method=RequestMethod.POST)
	public String list(Model model) {
		List<Item> items = itemRepository.findAll();
		model.addAttribute("items",items);
		System.out.println("こんにちは");
		return "client/item/list";
	}
	
	@RequestMapping(path="/client/item/detail/{id}")
	public String detail(@PathVariable int id, Model model) {
		Item item = itemRepository.findByIdQuery(id);
		ItemBean itemBean = beanTools.copyEntityToItemBean(item);
		model.addAttribute("item", itemBean);
		return "client/item/detail";
	}
	
	@RequestMapping(path="/client/item/list/{sortType}" ,method = RequestMethod.GET)
	public String showListPageItems(@PathVariable int sortType, Model model) {
		List<Item> item;
		
		model.addAttribute("sortType", sortType);
		if (sortType == 1) {
			item = itemRepository.findAllItemsDESC();
		} else {
			item = itemRepository.findItemOrderBySales();
		}
		model.addAttribute("items", item);
		model.addAttribute("sortType", sortType);
		
		
		return "client/item/list";
	} 

	@RequestMapping(path="/client/item/list/{sortType}?category={id}", method=RequestMethod.GET)
	public String category(@PathVariable Integer id, Model model) {
		List<Item> items = null;
		if (id == 1) {
			items = itemRepository.findCategoryFood();
			model.addAttribute("items", items);
		} else if (id == 2) {
			items = itemRepository.findCategoryNotFood();
			model.addAttribute("items", items);
		} else {
			items = itemRepository.findAll();
			model.addAttribute("items", items);
		}
		return "client/item/list";
		
	}
	@RequestMapping(path="/{sortType}" ,method = RequestMethod.GET)
	public String showTopPageItems(@PathVariable int sortType, Model model) {
		List<Item> item;
		
		model.addAttribute("sortType", sortType);
		if (sortType == 1) {
			item = itemRepository.findAllItemsDESC();
		} else {
			item = itemRepository.findItemOrderBySales();
		}
		model.addAttribute("items", item);
		model.addAttribute("sortType", sortType);
		
		
		return "index";
	} 
	
}