package bean;

import lombok.Data;

import java.util.Date;

@Data
public class GenericEntity {
   private String id;
   private int status;
   private Date createDate;
}
