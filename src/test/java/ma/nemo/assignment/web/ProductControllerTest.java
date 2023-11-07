package ma.nemo.assignment.web;

import ma.nemo.assignment.domain.Product;
import ma.nemo.assignment.exceptions.ProductAlreadyExists;
import ma.nemo.assignment.exceptions.ProductValidationException;
import ma.nemo.assignment.repository.ProductRepository;
import ma.nemo.assignment.repository.TransactionHistoryRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;


import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductControllerTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;
    private Product product;
    private ProductController productController = new ProductController();

    @Test
    public void shoulSaveProductWithSuccess() throws ProductValidationException, ProductAlreadyExists {

        setProductRepoAndTransactionHistoryRep();
        product.setProductCode("TesT");
        product.setProductName("Test product");
        product.setUnitPrice(199.99);
        product.setDescription("Product for testing");
        product.setQuantityInStock(2);
        ResponseEntity controllerResponse = productController.createProduct(product);
        assertEquals(HttpStatus.CREATED,HttpStatus.CREATED);
    }
    @Test
    public void shouldThrowExceptionForShortProductCode(){
        setProductRepoAndTransactionHistoryRep();
        product.setProductCode("A");
        setProductRepoAndTransactionHistoryRep();
        assertThrows(ProductValidationException.class, () -> {
            ResponseEntity<Long> responseEntity = productController.createProduct(product);
        });
    }
    @Test
    public void shouldThrowExceptionForLongProductCode() {
        setProductRepoAndTransactionHistoryRep();
        product.setProductCode("ThisIsAReallyLongProductCode"); // length > 10
        setProductRepoAndTransactionHistoryRep();
        assertThrows(ProductValidationException.class, () -> {
            ResponseEntity<Long> responseEntity = productController.createProduct(product);
        });
    }
    @Test
    public void shouldThrowExceptionForEmptyProductCode() {
        setProductRepoAndTransactionHistoryRep();
        product.setProductCode("");
        setProductRepoAndTransactionHistoryRep();
        assertThrows(ProductValidationException.class, () -> {
            ResponseEntity<Long> responseEntity = productController.createProduct(product);
        });
    }
    @Test
    public void shouldThrowExceptionForInvalidQuantity() {
        setProductRepoAndTransactionHistoryRep();
        product.setProductCode("Test");
        product.setQuantityInStock(0);
        setProductRepoAndTransactionHistoryRep();
        assertThrows(ProductValidationException.class, () -> {
            ResponseEntity<Long> responseEntity = productController.createProduct(product);
        });
    }
    @Test
    public void shouldThrowExceptionForInvalidPrice() {
        setProductRepoAndTransactionHistoryRep();
        product.setProductCode("Test");
        product.setQuantityInStock(10);
        product.setUnitPrice(-1.0);
        assertThrows(ProductValidationException.class, () -> {
            ResponseEntity<Long> responseEntity = productController.createProduct(product);
        });
    }
    public void setProductRepoAndTransactionHistoryRep(){
        if(product==null){
            product = new Product();
        }
        productController.setProductRepository(productRepository);
        productController.setTransactionRepo(transactionHistoryRepository);
    }
}