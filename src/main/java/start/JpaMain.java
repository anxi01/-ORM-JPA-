package start;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import model.entity.Member;

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

    Member member1 = new Member("1번");
    Member member2 = new Member("2번");
    Member member3 = new Member("3번");

    em.persist(member1);
    em.persist(member2);
    em.persist(member3);

    TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);

    List<Member> resultList = query.getResultList();

    resultList.forEach(s -> System.out.println("member = " + s));
  }
}
