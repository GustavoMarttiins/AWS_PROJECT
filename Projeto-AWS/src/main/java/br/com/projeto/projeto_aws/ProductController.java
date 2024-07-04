package br.com.projeto.projeto_aws;

import br.com.projeto.projeto_aws.entity.Product;
import br.com.projeto.projeto_aws.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<Product> getProductAll() {
        return productService.getAllProduct();
    }

    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id) {
        Optional<Product> produtc = productService.productById(id);
        return new ResponseEntity<>(produtc, HttpStatus.OK).getBody();
    }


    @PostMapping("create")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createProduct = productService.createProduct(product);
        return new ResponseEntity<>(createProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product newProductData) {
        Product updatedProduct = productService.updateProduct(id, newProductData);
        return ResponseEntity.ok(updatedProduct);
    }
}
