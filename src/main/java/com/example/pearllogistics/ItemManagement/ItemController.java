package com.example.pearllogistics.ItemManagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/items")
public class ItemController {
    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final ObjectMapper objectMapper;

    @PostMapping(value = "/add-item", consumes = {"multipart/form-data"})
    public ResponseEntity<ItemAuthResponse> register(
            @RequestParam("item") String itemJson,
            @RequestParam(value = "image1", required = false) MultipartFile image1,
            @RequestParam(value = "image2", required = false) MultipartFile image2,
            @RequestParam(value = "image3", required = false) MultipartFile image3,
            @RequestParam(value = "image4", required = false) MultipartFile image4) {
        System.out.println("Received item JSON: " + itemJson);
        System.out.println("Image1 is null: " + (image1 == null));
        try {
            ItemAuthRequest itemRequest = objectMapper.readValue(itemJson, ItemAuthRequest.class);
            return ResponseEntity.ok(itemService.addItem(itemRequest, image1, image2, image3, image4));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ItemAuthResponse(false, e.getMessage(), null));
        }
    }

    @GetMapping(value = "get-items")
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<ItemCategoryDTO>> getAllCategories() {
        List<ItemCategoryDTO> categories = itemService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteItem(@PathVariable Long id) {
        try {
            boolean deleted = itemService.deleteItem(id);
            if (deleted) {
                return ResponseEntity.ok(
                        ResponseDTO.builder()
                                .status("success")
                                .message("Item deleted successfully")
                                .build()
                );
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ResponseDTO.builder()
                                .status("error")
                                .message("Item not found")
                                .build()
                );
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResponseDTO.builder()
                            .status("error")
                            .message("Error deleting item: " + e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ItemDetailResponse> getItemById(@PathVariable Long id) {
        try {
            ItemDetailResponse response = itemService.getItemById(id);
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ItemDetailResponse.builder()
                            .success(false)
                            .message("Error retrieving item: " + e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping("/{itemId}/image")
    public ResponseEntity<byte[]> getItemImage(@PathVariable Long itemId) {
        try {
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new EntityNotFoundException("Item not found"));

            // Get the first available image (prioritize image1)
            byte[] imageData = item.getImage1();

            // If image1 is null, try the next images
            if (imageData == null || imageData.length == 0) {
                imageData = item.getImage2();
            }
            if (imageData == null || imageData.length == 0) {
                imageData = item.getImage3();
            }
            if (imageData == null || imageData.length == 0) {
                imageData = item.getImage4();
            }

            // If no image is available, return a 404
            if (imageData == null || imageData.length == 0) {
                return ResponseEntity.notFound().build();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}