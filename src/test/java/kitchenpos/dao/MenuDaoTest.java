package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DaoTest
class MenuDaoTest {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroupEntity = new MenuGroup();
        menuGroupEntity.setName("반려식물");
        menuGroup = menuGroupDao.save(menuGroupEntity);
    }

    @Test
    void 메뉴_엔티티를_저장한다() {
        Menu menuEntity = createMenu();

        Menu saveMenu = menuDao.save(menuEntity);

        assertThat(saveMenu.getId()).isPositive();
    }

    @Test
    void 메뉴_엔티티를_조회한다() {
        Menu menuEntity = createMenu();
        Menu saveMenu = menuDao.save(menuEntity);

        assertThat(menuDao.findById(saveMenu.getId())).isPresent();
    }

    @Test
    void 모든_메뉴_엔티티를_조회한다() {
        Menu menuEntityA = createMenu();
        Menu menuEntityB = createMenu();
        Menu saveMenuA = menuDao.save(menuEntityA);
        Menu saveMenuB = menuDao.save(menuEntityB);

        List<Menu> menus = menuDao.findAll();

        assertThat(menus).usingRecursiveFieldByFieldElementComparatorOnFields("id")
                .contains(saveMenuA, saveMenuB);
    }

    @Test
    void 존재하는_메뉴의_개수를_조회한다() {
        Menu menuEntityA = createMenu();
        Menu menuEntityB = createMenu();
        Menu saveMenuA = menuDao.save(menuEntityA);
        Menu saveMenuB = menuDao.save(menuEntityB);

        long count = menuDao.countByIdIn(List.of(saveMenuA.getId(), saveMenuB.getId()));

        assertThat(count).isEqualTo(2);
    }

    private Menu createMenu() {
        Menu menu = new Menu();
        menu.setName("스투키");
        menu.setPrice(BigDecimal.valueOf(10_000));
        menu.setMenuGroupId(menuGroup.getId());
        return menu;
    }
}
