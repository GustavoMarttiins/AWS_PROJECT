package br.com.projeto.projeto_aws.service;

import br.com.projeto.projeto_aws.entity.Envelope;
import br.com.projeto.projeto_aws.entity.Product;
import br.com.projeto.projeto_aws.entity.ProductEvent;
import br.com.projeto.projeto_aws.enums.EventType;
import br.com.projeto.projeto_aws.exception.ResourceNotFoundException;
import br.com.projeto.projeto_aws.repository.ProductRepository;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.Topic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);

    private ProductRepository productRepository;

    private AmazonSNS snsClient;
    private Topic productEventsTopic;
    private ObjectMapper mapper;

    public ProductService(AmazonSNS snsClient,
                          @Qualifier("productEventsTopic") Topic productEventsTopic,
                          ObjectMapper mapper) {

        this.snsClient = snsClient;
        this.productEventsTopic = productEventsTopic;
        this.mapper = mapper;
    }

    public void publishProductEvent(Product product, EventType eventType, String username) {
        ProductEvent productEvent = new ProductEvent();
        productEvent.setCode(product.getCode());
        productEvent.setProductId(product.getId());
        productEvent.setUsername(username);

        Envelope envelope = new Envelope();
        envelope.setEventType(eventType);

        try {
            envelope.setData(mapper.writeValueAsString(productEvent));

            snsClient.publish(
                    productEventsTopic.getTopicArn(),
                    mapper.writeValueAsString(envelope));

        } catch (JsonProcessingException e) {
            LOG.error("Failed to create product event message");
        }
    }

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
