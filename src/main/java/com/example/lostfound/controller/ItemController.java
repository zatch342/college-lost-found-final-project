package com.example.lostfound.controller;

import com.example.lostfound.model.AppUser;
import com.example.lostfound.model.Item;
import com.example.lostfound.model.ItemStatus;
import com.example.lostfound.service.ItemService;
import com.example.lostfound.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final UserService userService;

    public ItemController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
                       Authentication authentication,
                       Model model) {
        model.addAttribute("items", itemService.findAll(keyword));
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        model.addAttribute("currentEmail", authentication.getName());
        return "items/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("item", new Item());
        model.addAttribute("statuses", ItemStatus.values());
        model.addAttribute("mode", "create");
        return "items/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute Item item,
                         BindingResult bindingResult,
                         @RequestParam("image") MultipartFile image,
                         Authentication authentication,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", ItemStatus.values());
            model.addAttribute("mode", "create");
            return "items/form";
        }
        AppUser owner = userService.findByEmail(authentication.getName());
        try {
            itemService.create(item, image, owner);
            return "redirect:/items?created";
        } catch (IllegalArgumentException exception) {
            model.addAttribute("error", exception.getMessage());
            model.addAttribute("statuses", ItemStatus.values());
            model.addAttribute("mode", "create");
            return "items/form";
        }
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id,
                          Authentication authentication,
                          Model model) {
        Item item = itemService.findById(id);
        model.addAttribute("item", item);
        model.addAttribute("canEdit", itemService.canEdit(item, authentication.getName()));
        return "items/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id,
                           Authentication authentication,
                           Model model) {
        AppUser user = userService.findByEmail(authentication.getName());
        Item item = itemService.findById(id);
        if (!itemService.canEdit(item, user.getEmail())) {
            return "redirect:/items/" + id + "?denied";
        }

        model.addAttribute("item", item);
        model.addAttribute("statuses", ItemStatus.values());
        model.addAttribute("mode", "edit");
        return "items/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute Item item,
                         BindingResult bindingResult,
                         @RequestParam("image") MultipartFile image,
                         Authentication authentication,
                         Model model) {
        if (bindingResult.hasErrors()) {
            item.setId(id);
            model.addAttribute("statuses", ItemStatus.values());
            model.addAttribute("mode", "edit");
            return "items/form";
        }
        AppUser user = userService.findByEmail(authentication.getName());
        try {
            itemService.update(id, item, image, user);
            return "redirect:/items/" + id + "?updated";
        } catch (IllegalArgumentException exception) {
            item.setId(id);
            model.addAttribute("error", exception.getMessage());
            model.addAttribute("statuses", ItemStatus.values());
            model.addAttribute("mode", "edit");
            return "items/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Authentication authentication) {
        AppUser user = userService.findByEmail(authentication.getName());
        itemService.delete(id, user);
        return "redirect:/items?deleted";
    }
}
