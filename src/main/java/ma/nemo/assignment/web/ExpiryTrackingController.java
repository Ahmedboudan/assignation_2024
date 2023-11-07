package ma.nemo.assignment.web;

import ma.nemo.assignment.domain.Product;
import ma.nemo.assignment.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/expiry-alerts")
public class ExpiryTrackingController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<Product>> findProductsNearExpiry() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH,1);
        Date expiredDate = calendar.getTime();
        List<Product> nearExpiryProducts = productRepository
                .findByExpirationDateGreaterThanEqualAndExpirationDateLessThanEqual(expiredDate);
        if (nearExpiryProducts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(nearExpiryProducts, HttpStatus.OK);
        }
    }
}
