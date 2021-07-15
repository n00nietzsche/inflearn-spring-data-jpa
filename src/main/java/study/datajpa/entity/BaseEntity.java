package study.datajpa.entity;


import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
// 이와 같이 해놓으면,
// `@PrePersist` 같은 것을 코딩할 필요가 없다.
public class BaseEntity {

    // 실무에서는 `BaseTimeEntity`에 아래의 내용만 두고,
    // `BaseEntity`를 만들어 `BaseTimeEntity`를 상속받도록 하게 한다.
    // 등록자와 수정자를 알아야 할 때만, `BaseEntity`를 상속받아서 쓴다.

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime updatedDate;

    // 이건 도대체 값이 어떻게 들어가는 걸까?
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;
}
