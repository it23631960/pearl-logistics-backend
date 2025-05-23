package com.example.pearllogistics.ItemManagement;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service

@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public ItemAuthResponse addItem(ItemAuthRequest request, MultipartFile image1, MultipartFile image2, MultipartFile image3, MultipartFile image4) {
        System.out.println("Image 1: " + (image1 != null ? image1.getSize() : "null"));
        System.out.println("Image 2: " + (image2 != null ? image2.getSize() : "null"));
        System.out.println("Image 3: " + (image3 != null ? image3.getSize() : "null"));
        System.out.println("Image 4: " + (image4 != null ? image4.getSize() : "null"));

        try {
            Item item = new Item();
            item.setName(request.getName());
            item.setDescription(request.getDescription());
            item.setCategory(request.getCategory());
            item.setQuantity(request.getQuantity());
            item.setPrice(request.getPrice());
            item.setBestseller(request.getBestseller());


            if (image1 != null && !image1.isEmpty()) {
                item.setImage1(image1.getBytes());
            }if (image2 != null && !image2.isEmpty()) {
                item.setImage2(image2.getBytes());
            }if (image3 != null && !image3.isEmpty()) {
                item.setImage3(image3.getBytes());
            }if (image4 != null && !image4.isEmpty()) {
                item.setImage4(image4.getBytes());
            }

            itemRepository.save(item);




            return new ItemAuthResponse( true,"Item Added successful", item);
        } catch (IOException e) {
            return new ItemAuthResponse(false,"Error processing image: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ItemAuthResponse(false, "Error during registration: " + e.getMessage(), null);
        }
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public List<ItemCategoryDTO> getAllCategories() {
        return itemRepository.findAllCategories();
    }


    @Transactional
    public boolean deleteItem(Long id) {
        try {
            Optional<Item> itemOptional = itemRepository.findById(id);

            if (itemOptional.isPresent()) {
                itemRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete item", e);
        }
    }

    @Transactional(readOnly = true)
    public ItemDetailResponse getItemById(Long itemId) {
        try {
            Optional<Item> itemOptional = itemRepository.findById(itemId);

            if (itemOptional.isEmpty()) {
                return ItemDetailResponse.builder()
                        .success(false)
                        .message("Item not found with ID: " + itemId)
                        .build();
            }

            Item item = itemOptional.get();


//            String image1Base64 = item.getImage1() != null ? Base64.getEncoder().encodeToString(item.getImage1()) : null;
//            String image2Base64 = item.getImage2() != null ? Base64.getEncoder().encodeToString(item.getImage2()) : null;
//            String image3Base64 = item.getImage3() != null ? Base64.getEncoder().encodeToString(item.getImage3()) : null;
//            String image4Base64 = item.getImage4() != null ? Base64.getEncoder().encodeToString(item.getImage4()) : null;

            return ItemDetailResponse.builder()
                    .success(true)
                    .message("Item retrieved successfully")
                    .item(item)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ItemDetailResponse.builder()
                    .success(false)
                    .message("Error retrieving item: " + e.getMessage())
                    .build();
        }
    }
}