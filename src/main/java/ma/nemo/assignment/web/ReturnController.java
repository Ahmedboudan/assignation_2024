package ma.nemo.assignment.web;

import ma.nemo.assignment.domain.Product;
import ma.nemo.assignment.domain.Return;
import ma.nemo.assignment.domain.Sale;
import ma.nemo.assignment.domain.TransactionHistory;
import ma.nemo.assignment.exceptions.ProductNotFound;
import ma.nemo.assignment.repository.ProductRepository;
import ma.nemo.assignment.repository.ReturnRepository;
import ma.nemo.assignment.repository.TransactionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/return")
public class ReturnController {
    @Autowired
    private ReturnRepository returnRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;
    @PostMapping
    public ResponseEntity<Long> createReturn(@RequestBody Return productReturned) throws ProductNotFound {
        Product product = productRepository.findByProductCode(productReturned.getProductCode());
        if(product==null){
            System.out.println("Product not found ");
            throw new ProductNotFound("Product not found");
        }
        Integer quantity = product.getQuantityInStock();
        Integer newQantity = quantity+productReturned.getReturnQuantity();
        product.setQuantityInStock(newQantity);
        // Update product quantity
        productRepository.save(product);

        productReturned.setProduct(product);
        // Save infos about returned product
        returnRepository.save(productReturned);
        // Save transaction history
        transactionHistoryRepository.save(getTransactionHistory(product,productReturned));
        return new ResponseEntity<>(productReturned.getReturnId(), HttpStatus.CREATED);
    }
    // this method is used to create a Transaction history for current transaction
    public TransactionHistory getTransactionHistory(Product product, Return returned){
        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setTransactionType("Product Return");
        transactionHistory.setTransactionDate(new Date());
        transactionHistory.setQuantity(returned.getReturnQuantity());
        transactionHistory.setProduct(product);
        return transactionHistory;
    }
}
