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
    Member member1 = new Member("1번", 1);
    Member member2 = new Member("2번", 2);
    Member member3 = new Member("3번", 3);

    teamA.addMember(member1);
    teamA.addMember(member2);
    teamA.addMember(member3);
    member1.addTeam(teamA);
    member2.addTeam(teamA);
    member3.addTeam(teamA);

    em.persist(teamA);
    em.persist(member1);
    em.persist(member2);
    em.persist(member3);

    String teamName = "팀A";
    List<Member> members = em.createQuery("select m from start.Member m "
            + "inner join m.team t where t.name = :teamName", Member.class)
        .setParameter("teamName", teamName)
        .getResultList();

    List<Object[]> result = em.createQuery("select m, t from start.Member m join m.team t")
        .getResultList();

    for (Object[] row : result) {
      Member member = (Member) row[0];
      Team team = (Team) row[1];
      System.out.println("Member : " + member);
      System.out.println("Team : " + team.getName());
    }
  }
}
