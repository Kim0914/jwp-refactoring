package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DaoTest
class OrderLineItemDaoTest {

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    private Order order;
    private Menu menu;

    @BeforeEach
    void setUp() {
        TableGroup tableGroupEntity = new TableGroup();
        tableGroupEntity.setCreatedDate(LocalDateTime.now());
        TableGroup tableGroup = tableGroupDao.save(tableGroupEntity);

        OrderTable orderTableEntity = new OrderTable();
        orderTableEntity.setTableGroupId(tableGroup.getId());
        OrderTable orderTable = orderTableDao.save(orderTableEntity);

        Order orderEntity = new Order();
        orderEntity.setOrderTableId(orderTable.getId());
        orderEntity.setOrderedTime(LocalDateTime.now());
        orderEntity.setOrderStatus(OrderStatus.COOKING.name());
        order = orderDao.save(orderEntity);

        MenuGroup menuGroupEntity = new MenuGroup();
        menuGroupEntity.setName("샐러드");
        MenuGroup menuGroup = menuGroupDao.save(menuGroupEntity);

        Menu menuEntity = new Menu();
        menuEntity.setMenuGroupId(menuGroup.getId());
        menuEntity.setName("닭가슴살 샐러드");
        menuEntity.setPrice(BigDecimal.valueOf(1_000_000));
        menu = menuDao.save(menuEntity);
    }

    @Test
    void 주문_아이템_엔티티를_저장한다() {
        OrderLineItem orderLineItemEntity = createOrderLineItem();

        OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItemEntity);

        assertThat(savedOrderLineItem.getSeq()).isPositive();
    }

    @Test
    void 주문_아이템_엔티티를_조회한다() {
        OrderLineItem orderLineItemEntity = createOrderLineItem();
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItemEntity);

        assertThat(orderLineItemDao.findById(savedOrderLineItem.getSeq())).isPresent();
    }

    @Test
    void 모든_주문_아이템_엔티티를_조회한다() {
        OrderLineItem orderLineItemEntityA = createOrderLineItem();
        OrderLineItem orderLineItemEntityB = createOrderLineItem();
        OrderLineItem savedOrderLineItemA = orderLineItemDao.save(orderLineItemEntityA);
        OrderLineItem savedOrderLineItemB = orderLineItemDao.save(orderLineItemEntityB);

        List<OrderLineItem> orderLineItems = orderLineItemDao.findAll();

        assertThat(orderLineItems).usingRecursiveFieldByFieldElementComparatorOnFields("seq")
                .contains(savedOrderLineItemA, savedOrderLineItemB);
    }

    @Test
    void 주문에_해당하는_모든_주문_아이템_엔티티를_조회한다() {
        OrderLineItem orderLineItemEntityA = createOrderLineItem();
        OrderLineItem orderLineItemEntityB = createOrderLineItem();
        OrderLineItem saveOrderLineItemA = orderLineItemDao.save(orderLineItemEntityA);
        OrderLineItem saveOrderLineItemB = orderLineItemDao.save(orderLineItemEntityB);

        List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(order.getId());

        assertThat(orderLineItems).usingRecursiveFieldByFieldElementComparatorOnFields("seq")
                .contains(saveOrderLineItemA, saveOrderLineItemB);
    }

    private OrderLineItem createOrderLineItem() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setOrderId(order.getId());
        orderLineItem.setQuantity(10);
        return orderLineItem;
    }
}
