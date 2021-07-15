package study.datajpa.dto;

import lombok.Data;
import study.datajpa.entity.Member;

@Data // 엔티티에는 `@Data` 웬만하면 쓰지말자. `@Setter`도 포함되기 때문
public class MemberDto {
    private Long id;
    private String username;
    private String teamName;

    public MemberDto() {
    }

    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }

    public MemberDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.teamName = "팀없음";
    }
}
