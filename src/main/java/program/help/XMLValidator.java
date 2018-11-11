package program.help;

import org.xml.sax.ErrorHandler;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;

public class XMLValidator {

   public boolean validateXMLSchema(String xmlDocumentPath, String xsdSchemaPath) {

      try {
         /* Наша фабрика для схемы, которая поможет загрузить ещё из xsd (по поводу XMLConstants.W3C_XML_SCHEMA_NS_URI
         - это необходимо для определения языка схемы, то есть если у вас в схеме в качестве namespace есть что-то типа
         xmlns:xsd="http://www.w3.org/2001/XMLSchema" */
         SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
         ErrorHandler errorHandler = new CustomErrorHandler();
         schemaFactory.setErrorHandler(errorHandler);

         /* Загружаем схему */
         Schema schema = schemaFactory.newSchema(new File(xsdSchemaPath));

         /* Создаём валидатор */
         Validator xsdValidator = schema.newValidator();

         /* Тут всё просто, если валидацию пройдёт успешно, то выйдем из блока try - catch, вернув true,
         иначе поймаем ошибку, вернув false */
         xsdValidator.validate(new StreamSource(new File(xmlDocumentPath)));
         return true;
      } catch (Exception exception) {
         System.err.println(exception.getMessage());
         return false;
      }
   }
}