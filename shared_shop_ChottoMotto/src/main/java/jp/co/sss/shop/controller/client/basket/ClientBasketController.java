package jp.co.sss.shop.controller.client.basket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.shop.entity.Item;
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

	@RequestMapping(path = "/client/basket/add")
	public String addItemToBasket(@RequestParam("itemId") Integer itemId, Model model) {
		List<Item> items = repository.findAll();
		Item item = repository.findById(itemId).orElse(null);
		if (item != null) {
			items.add(item);
		}
		model.addAttribute("items", items);
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
