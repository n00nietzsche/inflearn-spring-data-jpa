package study.datajpa.repository;

public interface NestedClosedProjections {

    String getUsername();
    TeamInfo getTeam();

    // 아직은 중첩 구조에 대한 쿼리 최적화를 지원하진 않는다.
    interface TeamInfo {
        String getName();
    }
}
