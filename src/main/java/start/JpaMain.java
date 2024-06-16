package start;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

  public static void main(String[] args) {

    /* 엔티티 매니저 팩토리 - 생성 */
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("JAVA-ORM-JPA");

    /* 엔티티 매니저 - 생성 */
    EntityManager em = emf.createEntityManager();

    /* 트랜잭션 - 획득 */
    EntityTransaction tx = em.getTransaction(); // 트랜잭션 API

    try {
      tx.begin(); /* 트랜잭션 - 시작 */
      logic(em); /* 비즈니스 로직 - 실행 */
      tx.commit(); /* 트랜잭션 - 커밋 */

    } catch (Exception e) {
      tx.rollback(); /* 트랜잭션 - 롤백 */
    } finally {
      em.close(); /* 엔티티 매니저 - 종료 */
    }
    emf.close(); /* 엔티티 매니저 팩토리 - 종료 */
  }

  /* 비즈니스 로직 */
  private static void logic(EntityManager em) {

    Team teamA= new Team("팀A");
    Team teamB = new Team("팀B");

    Member member1 = new Member("1번", 1);
    Member member2 = new Member("2번", 2);
    Member member3 = new Member("3번", 3);

    teamA.addMember(member1);
    teamA.addMember(member2);
    teamB.addMember(member3);
    member1.addTeam(teamA);
    member2.addTeam(teamA);
    member3.addTeam(teamB);

    em.persist(teamA);
    em.persist(teamB);
    em.persist(member1);
    em.persist(member2);
    em.persist(member3);

    // 엔티티 페치 조인
    String jpql = "select m from start.Member m join fetch m.team";
    List<Member> members = em.createQuery(jpql, Member.class).getResultList();
    for (Member member : members) {
      // 페치 조인으로 회원과 팀을 함께 조회해서 지연 로딩 발생 X
      System.out.println("username: " + member.getUsername() + ", team: " + member.getTeam());
    }

    // 컬렉션 페치 조인
    jpql = "select t from Team t join fetch t.members where t.name = '팀A'";
    List<Team> teams = em.createQuery(jpql, Team.class).getResultList();
    for (Team team : teams) {
      System.out.println("team: " + team);

      /**
       * 팀A의 member1, member2가 존재하고
       * 여기서 컬렉션 페치 조인 실행시 팀 A가 2번 조회된다.
       *
       * team: start.Team@3d0035d2
       * member: Member(id=3, username=1번, age=1, team=start.Team@3d0035d2)
       * member: Member(id=4, username=2번, age=2, team=start.Team@3d0035d2)
       * team: start.Team@3d0035d2
       * member: Member(id=3, username=1번, age=1, team=start.Team@3d0035d2)
       * member: Member(id=4, username=2번, age=2, team=start.Team@3d0035d2)
       */
      for (Member member : team.getMembers()) {
        System.out.println("member: " + member);
      }
    }

    // 따라서 위의 문제를 해결하기 위해 "DISTINCT" 를 사용하여 중복을 제거한다.
    jpql = "select distinct t from Team t join fetch t.members where t.name = '팀A'";
    teams = em.createQuery(jpql, Team.class).getResultList();
    for (Team team : teams) {
      System.out.println("team: " + team);

      for (Member member : team.getMembers()) {
        System.out.println("member: " + member);
      }
    }
  }
}
