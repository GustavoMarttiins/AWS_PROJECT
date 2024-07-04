package br.com.projeto.projeto_aws.service;

import br.com.projeto.projeto_aws.entity.Product;
import br.com.projeto.projeto_aws.exception.ResourceNotFoundException;
import br.com.projeto.projeto_aws.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    public Optional<Product> productById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public Product updateProduct(Long id, Product newProductData) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(newProductData.getName());
                    product.setModel(newProductData.getModel());
                    product.setCode(newProductData.getCode());
                    product.setPrice(newProductData.getPrice());
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
