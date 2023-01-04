package ua.sl.ihor.MyOLX.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.sl.ihor.MyOLX.DTO.PageableResponseDTO;
import ua.sl.ihor.MyOLX.DTO.ProductFullInfoDTO;
import ua.sl.ihor.MyOLX.DTO.ProductPreviewDTO;
import ua.sl.ihor.MyOLX.domain.Product;
import ua.sl.ihor.MyOLX.domain.ProductPhoto;
import ua.sl.ihor.MyOLX.exceptions.AccessDeniedException;
import ua.sl.ihor.MyOLX.exceptions.IncorrectFileFormat;
import ua.sl.ihor.MyOLX.exceptions.NotFoundException;
import ua.sl.ihor.MyOLX.repositories.ProductRepository;
import ua.sl.ihor.MyOLX.services.ProductService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    @Value("${images-storage-path}")
    private String imagesStoragePath;
    private final ProductRepository productRepository;

    @Override
    public PageableResponseDTO<ProductPreviewDTO> page(Pageable pageable) {
        log.info("Getting page: {}.", pageable.getPageNumber());
        return new PageableResponseDTO<>(productRepository.findAll(pageable).map(ProductPreviewDTO::new));
    }

    @Override
    public List<ProductPreviewDTO> all() {
        log.info("Getting all products.");
        return productRepository.findAll().stream().map(ProductPreviewDTO::new).collect(Collectors.toList());
    }

    @Override
    public Product findById(long id) {
        log.info("Finding product by id: {}.", id);
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product with id " + id + " not found"));
    }

    @Override
    public ProductFullInfoDTO findProductFullInfoById(long id) {
        return new ProductFullInfoDTO(findById(id));
    }

    @Override
    @Transactional
    public ProductFullInfoDTO add(Product newProduct, MultipartFile previewPhoto, List<MultipartFile> photosList) throws IOException {
        // Validation
        validatePhotos(previewPhoto, photosList);

        // Saving preview photo
        String previewFileName = UUID.randomUUID() + extractFileExtension(previewPhoto);
        previewPhoto.transferTo(new File(imagesStoragePath + "/" + previewFileName));
        newProduct.setPreviewPhoto(previewFileName);
        log.info("Saved preview photo.");

        // Saving other photos
        List<ProductPhoto> productPhotos = new ArrayList<>();
        for (MultipartFile photo : photosList) {
            String fileName = UUID.randomUUID() + extractFileExtension(photo);
            photo.transferTo(new File(imagesStoragePath + "/" + fileName));
            productPhotos.add(new ProductPhoto(newProduct, fileName));
        }
        newProduct.setProductPhotos(productPhotos);
        log.info("Saved all photos.");
        return new ProductFullInfoDTO(productRepository.save(newProduct));
    }

    private String extractFileExtension(MultipartFile previewPhoto) {
        return previewPhoto.getOriginalFilename().substring(previewPhoto.getOriginalFilename().lastIndexOf('.'));
    }

    private void validatePhotos(MultipartFile previewPhoto, List<MultipartFile> photosList) {
        if (previewPhoto.isEmpty() || !previewPhoto.getContentType().startsWith("image")) {
            log.error("Preview photo validation exception.");
            throw new IncorrectFileFormat("You can attach only images.");
        }
        for (MultipartFile multipartFile : photosList) {
            if (multipartFile.isEmpty() || !multipartFile.getContentType().startsWith("image")) {
                log.error("Photos validation exception.");
                throw new IncorrectFileFormat("You can attach only images.");
            }
        }
    }

    @Override
    @Transactional
    public ProductFullInfoDTO update(long productId, Product updatedProduct, long userId) {
        Product currentProduct = findById(productId);
        if (currentProduct.getUser().getId() == userId) {
            BeanUtils.copyProperties(updatedProduct, currentProduct, "id", "user");
            return new ProductFullInfoDTO(productRepository.save(currentProduct));
        } else {
            throw new AccessDeniedException("This is not yours product");
        }
    }

    @Override
    @Transactional
    public void delete(long id, long userId) {
        Product product = findById(id);
        if (product.getUser().getId() == userId) {
            log.info("Product with id: {} deleted.", id);
            product.setDeleted(true);
            productRepository.save(product);
        } else {
            throw new AccessDeniedException("This is not yours product");
        }
    }
}
