package jp.co.sss.shop.controller.client.basket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.form.ItemForm;
import jp.co.sss.shop.repository.ItemRepository;

@Controller
public class ClientBasketController {
	@Autowired
	private ItemRepository repository;

	
	@RequestMapping(path = "/client/basket/list")
	public String showItemList(Model model) {
		List<Item> items = repository.findAll();
		model.addAttribute("items", items);
		return "client/basket/list";
	}

	@SuppressWarnings("null")
	@RequestMapping(path = "/client/basket/add", method = RequestMethod.POST)
	public String addItemToBasket(Collection<? extends BasketBean> basketBean, ItemForm itemForm, Model model) {
		Optional<Item> selectedItem = repository.findById(itemForm.getId());
		List<BasketBean> basket = new ArrayList<>();
		basket.addAll(basketBean);
		List<BasketBean> newBasket = null;
		
		boolean itemInBasket = false;
		for (BasketBean basketItem: basket) {
			if (basketItem.getId().equals(selectedItem.map(Item::getId).orElse(null))) {
				itemInBasket = true;
				break;
			}
		}
		if (!itemInBasket && selectedItem.isPresent()) {
			BasketBean basketItem = new BasketBean(
					selectedItem.get().getId(),
					selectedItem.get().getName(),
					selectedItem.get().getStock());
			
			newBasket.add(basketItem);
			model.addAttribute("newBasket", newBasket);
		}
		return "redirect:/client/basket/list";
	}
	
	@RequestMapping(path = "/client/basket/delete")
	public String deleteItemFromBasket(@RequestParam("itemId") Integer itemId, Model model) {
		List<Item> items = repository.findAll();
		Item itemToRemove = null;
		for (Item item : items) {
			if (item.getId().equals(itemId)) {
				itemToRemove = item;
				break;
			}
		}

		if (itemToRemove != null) {
			items.remove(itemToRemove);
		}
		model.addAttribute("items",items);
		return "redirect:/client/basket/list";
	}

	@RequestMapping(path = "/client/basket/allDelete")
	public String deleteAllItemsFromBasket(Model model) {
		List<Item> items = repository.findAll();
		items.clear();
		model.addAttribute("items",items);
		return "redirect:/client/basket/list";
	}
}
