package ma.nemo.assignment.domain;

import jakarta.persistence.*;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "Sales")
@ToString
public class Sale {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long saleId;

  @ManyToOne
  @JoinColumn(name = "productId")
  private Product product;

  @Transient
  private String productCode;

  private Integer soldQuantity;

  private Double totalPrice;

  @Temporal(TemporalType.TIMESTAMP)
  private Date saleDate;

  @ManyToOne
  @JoinColumn(name = "userId")
  private User user;

  public Long getSaleId() {
    return saleId;
  }

  public String getProductCode() {
    return productCode;
  }

  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }

  public void setSaleId(Long saleId) {
    this.saleId = saleId;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Integer getSoldQuantity() {
    return soldQuantity;
  }

  public void setSoldQuantity(Integer soldQuantity) {
    this.soldQuantity = soldQuantity;
  }

  public Double getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(Double totalPrice) {
    this.totalPrice = totalPrice;
  }

  public Date getSaleDate() {
    return saleDate;
  }

  public void setSaleDate(Date saleDate) {
    this.saleDate = saleDate;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  // Getters, setters, etc.
}
