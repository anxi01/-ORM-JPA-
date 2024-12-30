package start;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "MEMBER", uniqueConstraints = {@UniqueConstraint(
    name = "NAME_AGE_UNIQUE",
    columnNames = {"NAME", "AGE"}
)})
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Member {

  @Id
  @GeneratedValue
  @Column(name = "ID")
  private Long id;

  @Column(name = "NAME", nullable = false, length = 10)
  private String username;

  private Integer age;

  @ManyToOne
  @JoinColumn(name = "team_id")
  private Team team;

  public Member(String username, int age) {
    this.username = username;
    this.age = age;
  }

  public void addTeam(Team team) {
    this.team = team;
  }
}
class UserDTO {
  private String username;
  private int age;

  public UserDTO(String username, int age) {
    this.username = username;
    this.age = age;
  }
}
