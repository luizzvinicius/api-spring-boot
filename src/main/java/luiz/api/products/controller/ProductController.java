package luiz.api.products.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import luiz.api.products.dto.CreateProductRequestDto;
import luiz.api.products.dto.ProductDTO;
import luiz.api.products.dto.ProductPageDTO;
import luiz.api.products.dto.UpdateProductDto;
import luiz.api.products.service.ProductService;
import luiz.api.products.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
@CrossOrigin(origins = "http://localhost:3001", maxAge = 3600)
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final Utils utils;

    public ProductController(ProductService productService, Utils utils) {
        this.productService = productService;
        this.utils = utils;
    }

    @GetMapping
    public ResponseEntity<ProductPageDTO> getAllProducts(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                         @RequestParam(defaultValue = "20") @Positive @Max(100) int qtdProducts) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts(page, qtdProducts));
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ProductDTO> saveProduct(@RequestParam("name") String name,
                                                  @RequestParam("price") double price,
                                                  @RequestParam("imageFile") List<MultipartFile> imageFiles
    ) {
        imageFiles.forEach(file -> {
            if (!utils.isValidImageType(file)) throw new RuntimeException("Invalid Image type");
        });

        CreateProductRequestDto dto = new CreateProductRequestDto(name, price, imageFiles);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.saveProduct(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getOneProduct(@PathVariable UUID id) {
        var product = productService.getOneProduct(id);
        return ResponseEntity.ok().body(product);
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> listObjects(@RequestParam String folder) {
        return ResponseEntity.ok(utils.listFiles(folder));
    }

    @DeleteMapping("/files")
    public ResponseEntity<Void> listObjects(@RequestBody List<String> urls) {
        utils.deleteFiles(urls);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/updatefile")
    public List<String> updateFile(@RequestParam("name") String name,
                                   @RequestParam("price") double price, @RequestParam List<MultipartFile> productImages) {
        return utils.updateFiles(productImages, "gatos");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @NotNull @PathVariable UUID id,
            @RequestParam("name") String name,
            @RequestParam("price") double price,
            @RequestParam("productImages") List<MultipartFile> productImages
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.updateProduct(id, new UpdateProductDto(name, price, productImages)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}