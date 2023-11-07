package ma.nemo.assignment.web;

import ma.nemo.assignment.domain.Product;
import ma.nemo.assignment.domain.Sale;
import ma.nemo.assignment.domain.TransactionHistory;
import ma.nemo.assignment.exceptions.ProductNotFound;
import ma.nemo.assignment.exceptions.ProductInStockNotSufficientException;
import ma.nemo.assignment.exceptions.ProductValidationException;
import ma.nemo.assignment.repository.ProductRepository;
import ma.nemo.assignment.repository.SaleRepository;
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

@RestController
@RequestMapping("/api/sale")
public class SaleController {
    Logger LOGGER = LoggerFactory.getLogger(SaleController.class);

    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    @PostMapping
    public ResponseEntity<Long> createSale(@RequestBody Sale sale) throws ProductInStockNotSufficientException, ProductNotFound, ProductValidationException {
        LOGGER.info("Creating Sale {} ",sale);

        // getting product from stock with productCode received from payload

        Product product = productRepository.findByProductCode(sale.getProductCode());

        // Verify if product really exists in stock
        if(product==null){
            throw new ProductNotFound("Product not found");
        }
        if(product.getQuantityInStock()<sale.getSoldQuantity()){
            System.out.println("Qantity in stock not sufficient");
            throw new ProductInStockNotSufficientException("Qantity in stock not sufficient");
        }
        sale.setProduct(product);
        // Setting other calculable attributs
        Double totalPrice = product.getUnitPrice()*sale.getSoldQuantity();
        sale.setTotalPrice(totalPrice);
        sale.setSaleDate(new Date());

        // Saving the new Sale
        saleRepository.save(sale);
        // Getting response
        transactionHistoryRepository.save(getTransactionHistory(product,sale));
        return new ResponseEntity<>(sale.getSaleId(), HttpStatus.CREATED);
    }
    public TransactionHistory getTransactionHistory(Product product,Sale sale){
        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setTransactionType("Add sale");
        transactionHistory.setTransactionDate(new Date());
        transactionHistory.setQuantity(sale.getSoldQuantity());
        transactionHistory.setProduct(product);
        return transactionHistory;
    }

    public SaleRepository getSaleRepository() {
        return saleRepository;
    }

    public void setSaleRepository(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
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
}
