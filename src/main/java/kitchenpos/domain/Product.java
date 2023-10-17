package kitchenpos.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.domain.vo.Price;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Price price;

    protected Product() {
    }

    private Product(String name, Price price) {
        this.name = name;
        this.price = price;
    }

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public static class ProductBuilder {

        private String name;
        private Price price;

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder price(long price) {
            this.price = Price.of(price);
            return this;
        }

        public Product build() {
            return new Product(name, price);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }
}
