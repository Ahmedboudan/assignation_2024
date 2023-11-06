package ma.nemo.assignment.web;


import ma.nemo.assignment.domain.Product;
import ma.nemo.assignment.domain.Supply;
import ma.nemo.assignment.domain.TransactionHistory;
import ma.nemo.assignment.exceptions.ProductNotFound;
import ma.nemo.assignment.exceptions.SupplyValidationException;
import ma.nemo.assignment.repository.ProductRepository;
import ma.nemo.assignment.repository.SypplyRepository;
import ma.nemo.assignment.repository.TransactionHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/supply")
public class SupplyController {
    Logger LOGGER = LoggerFactory.getLogger(SupplyController.class);
    @Autowired
    private SypplyRepository supplyRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    @PostMapping
    public ResponseEntity<Long> createSupply(@RequestBody Supply supply) throws SupplyValidationException {
        LOGGER.info("Creating supply {} ",supply);
        Product product = productRepository.findByProductCode(supply.getProductCode());
        if (product == null) {
            LOGGER.warn("Product with code {} not found", supply.getProductCode());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(supply.getQuantity()>500){
            LOGGER.warn("Qantity {} not allowed",supply.getQuantity());
            throw new SupplyValidationException("Qantity "+supply.getQuantity()+" not allowed");
        }
        supply.setProduct(product);
        supply.setSupplyDate(new Date());
        Supply saved = supplyRepository.save(supply);
        transactionHistoryRepository.save(getTransactionHistory(product,supply));
        return new ResponseEntity<>(saved.getSupplyId(), HttpStatus.CREATED);
    }
    public TransactionHistory getTransactionHistory(Product product,Supply supply){
        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setTransactionType("Supply");
        transactionHistory.setProduct(product);
        transactionHistory.setTransactionDate(new Date());
        transactionHistory.setQuantity(supply.getQuantity());
        return transactionHistory;
    }

    public SypplyRepository getSupplyRepository() {
        return supplyRepository;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public TransactionHistoryRepository getTransactionHistoryRepository() {
        return transactionHistoryRepository;
    }

    public void setTransactionHistoryRepository(TransactionHistoryRepository transactionHistoryRepository) {
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    public void setSupplyRepository(SypplyRepository supplyRepository) {
        this.supplyRepository = supplyRepository;
    }
}
