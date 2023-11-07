package ma.nemo.assignment.web;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import ma.nemo.assignment.domain.Product;
import ma.nemo.assignment.domain.TransactionHistory;
import ma.nemo.assignment.domain.User;
import ma.nemo.assignment.exceptions.ProductAlreadyExists;
import ma.nemo.assignment.exceptions.ProductValidationException;
import ma.nemo.assignment.repository.ProductRepository;
import ma.nemo.assignment.repository.TransactionHistoryRepository;
import ma.nemo.assignment.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository repo;

    @Autowired
    private TransactionHistoryRepository  transactionRepo;

    @PostMapping
    public ResponseEntity<Long> createProduct(@RequestBody Product prd)
        throws ProductAlreadyExists, ProductValidationException {

        LOGGER.info("Creating prd {}", prd);

        Product p = productRepository.findByProductCode(prd.getProductCode());

        if (p != null) {
            System.out.printf("Product with code %s already exists", prd.getProductCode());
            throw new ProductAlreadyExists();
        }

        if (prd.getProductCode().equals(null)) {
            System.out.printf("Product code is null");
            throw new ProductValidationException("Product code is null");
        } else if (prd.getProductCode().equals("")) {
            System.out.printf("Product code is empty");
            throw new ProductValidationException("Product code is empty");
        } else if (prd.getProductCode().length() > 10) {
            System.out.printf("Product code is too long");
            throw new ProductValidationException("Product code is too long");
        } else if (prd.getProductCode().length() < 3) {
            System.out.printf("Product code is too short");
            throw new ProductValidationException("Product code is too short");
        } else if (prd.getQuantityInStock() <= 0) {
            System.out.printf("Product quantity is invalid");
            throw new ProductValidationException("Product quantity is invalid");
        } else if (prd.getUnitPrice().intValue() < 0) {
            System.out.printf("Product price is invalid");
            throw new ProductValidationException("Product price is invalid");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date expirationDate = calendar.getTime();

        prd.setCreationDate(new Date());
        prd.setModificationDate(new Date());
        prd.setExpirationDate(expirationDate);

        Product saved = productRepository.save(prd);
        transactionRepo.save(getTransactionHistory(prd));
        return new ResponseEntity<>(saved.getProductId(), HttpStatus.CREATED);
    }
    public TransactionHistory getTransactionHistory(Product product){
        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setTransactionType("Adding product");
        transactionHistory.setProduct(product);
        transactionHistory.setTransactionDate(new Date());
        transactionHistory.setQuantity(1);
        return transactionHistory;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Product>> listProducts() {
        LOGGER.info("Listing products");
        List<Product> prds = productRepository.findAll();

        if (CollectionUtils.isEmpty(prds)) {
            return null;
        } else {
            return new ResponseEntity<>(prds, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            System.out.printf("Product with id %d found", id);
            return new ResponseEntity<>(optionalProduct.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("list_users")
    List<User> loadAllUtilisateur() {
        List<User> all = repo.findAll();

        if (CollectionUtils.isEmpty(all)) {
            return null;
        } else {
            return all;
        }
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public UserRepository getRepo() {
        return repo;
    }

    public void setRepo(UserRepository repo) {
        this.repo = repo;
    }

    public TransactionHistoryRepository getTransactionRepo() {
        return transactionRepo;
    }

    public void setTransactionRepo(TransactionHistoryRepository transactionRepo) {
        this.transactionRepo = transactionRepo;
    }
}

