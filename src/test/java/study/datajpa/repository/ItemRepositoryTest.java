package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.entity.Item;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired ItemRepository itemRepository;

    @Test
    public void save() {
        // `@GeneratedValue`가 아닌 경우
        // `@Id` 멤버에 값이 있기 때문에 `.merge()`가 호출되고,
        // `.merge()`의 경우, DB 데이터가 있을 때 덮어씌우는 것을 목적으로 한 메소드기 때문에
        // `select` 쿼리를 한번 더 내보내게 된다.
        // `.merge()`를 쓰는 것은 지양해야 한다.
         Item item = new Item("A");
         itemRepository.save(item);
    }
}