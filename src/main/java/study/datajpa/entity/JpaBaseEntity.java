package study.datajpa.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

// JPA에서 헷갈리면 안되는게,
// 값만 내려다 쓰는 상속이 있고, 진짜 DB 컬럼을 상속하는 관계가 있다.
// DB 컬럼을 상속받으려면, `@MappedSuperclass` 애노테이션을 써주어야 한다.
// `@MappedSuperclass`가 없으면 엔티티에서 상속을 받아도 컬럼이 안생긴다.
// 이렇게 만들고 상속해서 쓰면 어차피 공통관심사라서 많은 중복된 코드를 줄여줄 수 있다.
@MappedSuperclass
@Getter
public class JpaBaseEntity {

    // 실수로라도 DB 에 값이 변경되지 않도록
    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        // IDE가 보라색 색칠을 해주기 때문에 this를 잘 안쓴다.
        // 그래도 써주는게 명확할듯
        this.updatedDate = now;
        this.createdDate = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedDate = LocalDateTime.now();
    }
}
