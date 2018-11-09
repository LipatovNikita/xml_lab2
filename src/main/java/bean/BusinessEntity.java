package bean;

import lombok.Data;

import java.util.List;

@Data
public class BusinessEntity extends GenericEntity {
   private String name;
   private BusinessEntityRegistryStatus registryStatus;
   private List<CodeOkved> codeOkvedList;
}
