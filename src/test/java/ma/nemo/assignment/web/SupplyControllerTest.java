package ma.nemo.assignment.web;

import ma.nemo.assignment.domain.Product;
import ma.nemo.assignment.domain.Supply;
import ma.nemo.assignment.exceptions.SupplyValidationException;
import ma.nemo.assignment.repository.ProductRepository;
import ma.nemo.assignment.repository.SypplyRepository;
import ma.nemo.assignment.repository.TransactionHistoryRepository;
import ma.nemo.assignment.web.SupplyController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SupplyControllerTest {
    private SupplyController supplyController;

    @Mock
    private SypplyRepository supplyRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    @Before
    public void setup() {
        // Initialize mock objects
        MockitoAnnotations.initMocks(this);

        // Create an instance of SupplyController and set mock dependencies
        supplyController = new SupplyController();
        supplyController.setSupplyRepository(supplyRepository);
        supplyController.setProductRepository(productRepository);
        supplyController.setTransactionHistoryRepository(transactionHistoryRepository);
    }

    @Test
    public void shouldCreateSupplyWithValidProduct() throws SupplyValidationException {
        // Prepare test data
        Supply supply = new Supply();
        supply.setProductCode("TestProduct");
        supply.setQuantity(100);

        Product product = new Product();
        product.setProductCode("TestProduct");

        // Mock behavior of productRepository and supplyRepository
        when(productRepository.findByProductCode("TestProduct")).thenReturn(product);
        when(supplyRepository.save(any(Supply.class))).thenReturn(supply);

        // Call the method being tested
        ResponseEntity<Long> responseEntity = supplyController.createSupply(supply);

        // Verify that the expected methods were called and the response is as expected
        verify(supplyRepository, times(1)).save(supply);
        verify(transactionHistoryRepository, times(1)).save(any());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(supply.getSupplyId(), responseEntity.getBody());
    }

    @Test
    public void shouldReturnNotFoundForInvalidProductCode() throws SupplyValidationException {
        // Prepare test data
        Supply supply = new Supply();
        supply.setProductCode("InvalidProduct");
        supply.setQuantity(100);

        // Mock behavior of productRepository
        when(productRepository.findByProductCode("InvalidProduct")).thenReturn(null);

        // Call the method being tested
        ResponseEntity<Long> responseEntity = supplyController.createSupply(supply);

        // Verify that supplyRepository and transactionHistoryRepository were not called,
        // and the response is as expected (NOT_FOUND)
        verify(supplyRepository, never()).save(any());
        verify(transactionHistoryRepository, never()).save(any());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test(expected = SupplyValidationException.class)
    public void shouldThrowSupplyValidationExceptionForLargeQuantity() throws SupplyValidationException {
        // Prepare test data
        Supply supply = new Supply();
        supply.setProductCode("TestProduct");
        supply.setQuantity(1000);

        Product product = new Product();
        product.setProductCode("TestProduct");

        // Mock behavior of productRepository
        when(productRepository.findByProductCode("TestProduct")).thenReturn(product);

        // Call the method being tested, which is expected to throw SupplyValidationException
        supplyController.createSupply(supply);
    }
}
