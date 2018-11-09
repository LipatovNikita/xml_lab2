package program;

import bean.BusinessEntity;
import program.help.FileResolver;
import program.help.StAXReader;

import java.util.List;

public class Main {
   public static void main(String[] args) {
      StAXReader xmlReader = new StAXReader();
      List<BusinessEntity> businessEntities = xmlReader.readXMLFromFile("businessEntityList.xml");

      FileResolver fileResolver = new FileResolver();
      fileResolver.writeResultHtmlFile(businessEntities);
   }
}
