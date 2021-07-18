package study.datajpa.repository;

public class UsernameOnlyDto {

    private final String username;

    // 이 경우 파라미터 명을 가지고 분석한다
    // 이 경우 프록시도 필요 없다.
    // DTO에 최적화된 쿼리로 데이터를 가져올 때 유용하다.
    public UsernameOnlyDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
