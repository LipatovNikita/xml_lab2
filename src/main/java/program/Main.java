package program;

import bean.BusinessEntity;
import program.help.FileResolver;
import program.help.StAXReader;
import program.help.XMLValidator;

import java.util.List;

public class Main {
   public static void main(String[] args) {
      XMLValidator xmlValidator = new XMLValidator();
      FileResolver fileResolver = new FileResolver();
      StAXReader xmlReader = new StAXReader();

      if (xmlValidator.validateXMLSchema(fileResolver.getXmlFileAbsolutePath("businessEntityList.xml"),
              fileResolver.getXmlFileAbsolutePath("schema.xsd"))) {
         List<BusinessEntity> businessEntities = xmlReader.readXMLFromFile("businessEntityList.xml");
         fileResolver.writeResultHtmlFile(businessEntities);
      }
   }
}