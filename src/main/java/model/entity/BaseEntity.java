package model.entity;

import java.util.Date;
import javax.persistence.MappedSuperclass;
import lombok.Data;

@MappedSuperclass
@Data
public class BaseEntity {

  private Date createdDate; //등록일
  private Date lastModifiedDate; //수정일
}
