import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productName;
}

@Entity
@Table(name = "purchase_orders")
class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private String customerEmail;

    private String status;

    private LocalDateTime orderTime;

    @OneToMany(
            mappedBy = "purchaseOrder",
            cascade = CascadeType.ALL
    )
    private List<OrderLineItem> lineItems;
}

@Embeddable
class OrderLineItemId {

    private Long orderId;

    private Long productId;

    public OrderLineItemId() {
    }

    public OrderLineItemId(
            Long orderId,
            Long productId) {

        this.orderId = orderId;
        this.productId = productId;
    }
}

@Entity
@Table(name = "order_line_items")
class OrderLineItem {

    @EmbeddedId
    private OrderLineItemId id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private PurchaseOrder purchaseOrder;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    private BigDecimal lockedPrice;
}

interface PurchaseOrderRepository
        extends JpaRepository<PurchaseOrder, Long> {

    @Query("""
        SELECT DISTINCT po
        FROM PurchaseOrder po
        JOIN FETCH po.lineItems li
        JOIN FETCH li.product
        WHERE po.status = 'PENDING'
    """)
    List<PurchaseOrder> findPendingOrdersWithProducts();

    List<PurchaseOrder>
    findByOrderTimeBetweenAndCustomerEmailEndingWith(
            LocalDateTime start,
            LocalDateTime end,
            String domain
    );
}

public class CartFluxOrderFulfillmentEngine {

    public static void main(String[] args) {

        System.out.println(
                "CartFlux Order Fulfillment Engine");
    }
}