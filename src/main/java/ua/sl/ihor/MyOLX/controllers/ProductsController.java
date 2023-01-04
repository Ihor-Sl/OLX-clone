package ua.sl.ihor.MyOLX.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.sl.ihor.MyOLX.DTO.PageableResponseDTO;
import ua.sl.ihor.MyOLX.DTO.ProductFullInfoDTO;
import ua.sl.ihor.MyOLX.DTO.ProductPreviewDTO;
import ua.sl.ihor.MyOLX.domain.Product;
import ua.sl.ihor.MyOLX.domain.User;
import ua.sl.ihor.MyOLX.exceptions.BindingResultValidationException;
import ua.sl.ihor.MyOLX.services.ProductService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductService productService;

    @GetMapping
    public PageableResponseDTO<ProductPreviewDTO> page(Pageable pageable) {
        return productService.page(pageable);
    }

    @GetMapping("/all")
    public List<ProductPreviewDTO> all() {
        return productService.all();
    }

    @GetMapping("/id/{id}")
    public ProductFullInfoDTO one(@PathVariable long id) {
        return productService.findProductFullInfoById(id);
    }

    @PostMapping("/add")
    public void add(@AuthenticationPrincipal User user,
                    @RequestPart("product") @Valid Product newProduct,
                    BindingResult bindingResult,
                    @RequestParam(name = "previewPhoto") MultipartFile previewPhoto,
                    @RequestParam(name = "photo2", required = false) MultipartFile photo2,
                    @RequestParam(name = "photo3", required = false) MultipartFile photo3,
                    @RequestParam(name = "photo4", required = false) MultipartFile photo4,
                    @RequestParam(name = "photo5", required = false) MultipartFile photo5,
                    @RequestParam(name = "photo6", required = false) MultipartFile photo6,
                    @RequestParam(name = "photo7", required = false) MultipartFile photo7,
                    @RequestParam(name = "photo8", required = false) MultipartFile photo8,
                    @RequestParam(name = "photo9", required = false) MultipartFile photo9,
                    @RequestParam(name = "photo10", required = false) MultipartFile photo10
    ) throws IOException {
        newProduct.setUser(user);
        List<MultipartFile> photosList = Stream.of(photo2, photo3, photo4, photo5, photo6, photo7, photo8, photo9, photo10)
                .filter(Objects::nonNull).filter(file -> !file.isEmpty()).toList();
        if (bindingResult.hasErrors()) throw new BindingResultValidationException(bindingResult);
        productService.add(newProduct, previewPhoto, photosList);
    }

    @PutMapping("/update/{id}")
    public void update(@RequestBody @Valid Product newProduct,
                       BindingResult bindingResult,
                       @PathVariable long id,
                       @AuthenticationPrincipal User user
    ) {
        if (bindingResult.hasErrors()) throw new BindingResultValidationException(bindingResult);
        productService.update(id, newProduct, user.getId());
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable long id, @AuthenticationPrincipal User user) {
        productService.delete(id, user.getId());
    }
}
