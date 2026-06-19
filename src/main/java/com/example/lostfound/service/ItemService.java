package com.example.lostfound.service;

import com.example.lostfound.model.AppUser;
import com.example.lostfound.model.Item;
import com.example.lostfound.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final FileStorageService fileStorageService;

    public ItemService(ItemRepository itemRepository, FileStorageService fileStorageService) {
        this.itemRepository = itemRepository;
        this.fileStorageService = fileStorageService;
    }

    public List<Item> findAll(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return itemRepository.findAllByOrderByCreatedAtDesc();
        }
        return itemRepository.search(keyword.trim());
    }

    public Item findById(Long id) {
        return itemRepository.findWithOwnerById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item not found."));
    }

    public Item create(Item item, MultipartFile image, AppUser owner) {
        item.setOwner(owner);
        String imagePath = fileStorageService.saveImage(image);
        item.setImagePath(imagePath);
        return itemRepository.save(item);
    }

    public Item update(Long id, Item formItem, MultipartFile image, AppUser currentUser) {
        Item item = findOwnedItem(id, currentUser);
        item.setTitle(formItem.getTitle());
        item.setDescription(formItem.getDescription());
        item.setLocation(formItem.getLocation());
        item.setStatus(formItem.getStatus());
        item.setContactInfo(formItem.getContactInfo());

        String imagePath = fileStorageService.saveImage(image);
        if (imagePath != null) {
            item.setImagePath(imagePath);
        }
        return itemRepository.save(item);
    }

    public void delete(Long id, AppUser currentUser) {
        itemRepository.delete(findOwnedItem(id, currentUser));
    }

    public boolean canEdit(Item item, String email) {
        return item.getOwner() != null && item.getOwner().getEmail().equals(email);
    }

    private Item findOwnedItem(Long id, AppUser currentUser) {
        Item item = findById(id);
        if (!canEdit(item, currentUser.getEmail())) {
            throw new IllegalArgumentException("You can only edit your own posts.");
        }
        return item;
    }
}
