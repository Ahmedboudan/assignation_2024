package ma.nemo.assignment.domain;


import jakarta.persistence.*;
import lombok.ToString;

import java.util.Date;

@Entity
@ToString
@Table(name = "Supplies")
public class Supply {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long supplyId;

  @ManyToOne
  @JoinColumn(name = "productId")
  private Product product;

  @Transient
  private String productCode;

  private Integer quantity;

  @Temporal(TemporalType.TIMESTAMP)
  private Date supplyDate;

  public String getProductCode() {
    return productCode;
  }

  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }

  public Long getSupplyId() {
    return supplyId;
  }

  public void setSupplyId(Long supplyId) {
    this.supplyId = supplyId;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer addedQuantity) {
    this.quantity = addedQuantity;
  }

  public Date getSupplyDate() {
    return supplyDate;
  }

  public void setSupplyDate(Date supplyDate) {
    this.supplyDate = supplyDate;
  }

  // Getters, setters, etc.
}
