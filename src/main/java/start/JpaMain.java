package start;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

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

    Member member1 = new Member("1번", 1);
    Member member2 = new Member("2번", 2);
    Member member3 = new Member("3번", 3);

    em.persist(member1);
    em.persist(member2);
    em.persist(member3);

    // 객체 변환 작업
    // 1. new 명령어 사용 전
    List<Object[]> resultList = em.createQuery("select m.username, m.age from start.Member m").getResultList();

    List<UserDTO> userDTOS = new ArrayList<>();
    for (Object[] row : resultList) {
      UserDTO userDTO = new UserDTO((String) row[0], (Integer) row[1]);
      userDTOS.add(userDTO);
    }

    // 2. new 명령어 사용 후
    TypedQuery<UserDTO> query = em.createQuery("select new start.JpaMain.UserDTO(m.username, m.age) from start.Member m", UserDTO.class);
    List<UserDTO> newUserDTOs = query.getResultList();

    System.out.println(userDTOS == newUserDTOs);
  }

  public static class UserDTO {
    private String username;
    private int age;

    public UserDTO(String username, int age) {
      this.username = username;
      this.age = age;
    }
  }
}
