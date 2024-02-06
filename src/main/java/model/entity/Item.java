package model.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class Item {

  @Id
  @GeneratedValue
  @Column(name = "ITEM_ID")
  private Long id;

  private String name; // 이름
  private int price; // 가격
  private int stockQuantity; // 재고 수량

  // OrderItem -> Item 방향으로 참조하는 다대일 단방향 관계만 설정
  // 이유 : 주문상품에서 상품을 상품을 참조할 일은 많지만, 상품에서 주문상품을 참조할 일은 거의 없기 때문이다.

  @ManyToMany(mappedBy = "items")
  private List<Category> categories = new ArrayList<>();
}
