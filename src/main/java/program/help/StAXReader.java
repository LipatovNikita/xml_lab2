package program.help;

import bean.BusinessEntity;
import bean.BusinessEntityRegistryStatus;
import bean.CodeOkved;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class StAXReader {

   public List<BusinessEntity> readXMLFromFile(String xmlFileName) {
      /* Создаём корневой элемент XML-документа */
      List<BusinessEntity> businessEntities = new ArrayList<>();

      /* Создаём экземпляр собственного fileResolver'в, который вернёт путь к xml-файлу
      (к слову, получать этот путь можем как угодно, но выносить подобное в отдельный метод или класс - хороший тон) */
      FileResolver fileResolver = new FileResolver();
      String xmlAbsoluteFilePath = fileResolver.getXmlFileAbsolutePath(xmlFileName);

      /* Непосредственно наша inputFactory, которая будет последовательно идти по XML-документу
      и считывать элементы, встречающиеся у неё на пути */
      XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

      /* Поехали... */
      try (FileInputStream fileInputStream = new FileInputStream(xmlAbsoluteFilePath)) {
         XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(fileInputStream);
         /* Алгоритм основан на принципе - "опускайся ниже... по иерархии xml-документа", для этого используется
         переменная level, которая и отвечает собственно за уровень вложенности элементов */
         int level = 1;
         do {
            XMLEvent xmlEvent = xmlEventReader.peek();
            switch (xmlEvent.getEventType()) {
               case XMLStreamConstants.START_DOCUMENT:
                  level++;
                  break;
               case XMLStreamConstants.START_ELEMENT:
                  level++;
                  String elementName = xmlEvent.asStartElement().getName().toString();
                  elementName = elementName.substring(elementName.indexOf('}') + 1, elementName.length());
                  if (elementName.equals("businessEntity")) {
                     /* Опускаемся ниже в иерархии XML-документа, а именно на уровень с отдельными элементами списка */
                     BusinessEntity businessEntity = readBusinessEntityFromFile(xmlEventReader);
                     businessEntities.add(businessEntity);
                     level--;
                  }
                  break;
               case XMLStreamConstants.END_ELEMENT:
                  level--;
                  break;
               case XMLStreamConstants.END_DOCUMENT:
                  level--;
                  break;
            }
            xmlEventReader.nextEvent();
         } while (level > 1);
      } catch (Exception exception) {
         System.err.println("Error during parse XML");
         System.err.println(exception.getMessage());
      }
      return businessEntities;
   }

   private BusinessEntity readBusinessEntityFromFile(XMLEventReader xmlEventReader) throws XMLStreamException, ParseException {
      BusinessEntity businessEntity = new BusinessEntity();
      List<CodeOkved> codesOkved = new ArrayList<>();

      int level = 1;
      do {
         XMLEvent xmlEvent = xmlEventReader.peek();
         switch (xmlEvent.getEventType()) {
            case XMLStreamConstants.START_ELEMENT:
               level++;
               String elementName = xmlEvent.asStartElement().getName().toString();
               elementName = elementName.substring(elementName.indexOf('}') + 1, elementName.length());
               switch (elementName) {
                  case "id":
                     xmlEventReader.nextEvent();
                     XMLEvent xmlIdEvent = xmlEventReader.nextEvent();
                     businessEntity.setId(xmlIdEvent.asCharacters().getData());
                     level--;
                     break;
                  case "status":
                     xmlEventReader.nextEvent();
                     XMLEvent xmlStatusEvent = xmlEventReader.nextEvent();
                     businessEntity.setStatus(Integer.parseInt(xmlStatusEvent.asCharacters().getData()));
                     level--;
                     break;
                  case "businessEntityName":
                     xmlEventReader.nextEvent();
                     XMLEvent xmlNameEvent = xmlEventReader.nextEvent();
                     businessEntity.setName(xmlNameEvent.asCharacters().getData());
                     level--;
                     break;
                  case "businessEntityRegistryStatus":
                     xmlEventReader.nextEvent();
                     XMLEvent xmlRegistryStatusEvent = xmlEventReader.nextEvent();
                     businessEntity.setRegistryStatus(BusinessEntityRegistryStatus.valueOf(xmlRegistryStatusEvent.asCharacters().getData()));
                     level--;
                     break;
                  case "createDate":
                     xmlEventReader.nextEvent();
                     XMLEvent xmlDateEvent = xmlEventReader.nextEvent();
                     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d'T'H:m");
                     businessEntity.setCreateDate(dateFormat.parse(xmlDateEvent.asCharacters().getData()));
                     level--;
                     break;
                  case "codeOkved":
                     /* И ещё ниже опускаемся в иерархии XML-документа... */
                     CodeOkved codeOkved = readCodeOkvedFromFile(xmlEventReader);
                     codesOkved.add(codeOkved);
                     level--;
                     break;
               }
               break;
            case XMLStreamConstants.END_ELEMENT:
               level--;
               break;
         }
         xmlEventReader.nextEvent();
      } while (level > 1);
      businessEntity.setCodeOkvedList(codesOkved);
      return businessEntity;
   }

   private CodeOkved readCodeOkvedFromFile(XMLEventReader xmlEventReader) throws XMLStreamException, ParseException {
      CodeOkved codeOkved = new CodeOkved();

      int level = 1;
      do {
         XMLEvent xmlEvent = xmlEventReader.peek();
         switch (xmlEvent.getEventType()) {
            case XMLStreamConstants.START_ELEMENT:
               level++;
               String elementName = xmlEvent.asStartElement().getName().toString();
               elementName = elementName.substring(elementName.indexOf('}') + 1, elementName.length());
               switch (elementName) {
                  case "id":
                     xmlEventReader.nextEvent();
                     XMLEvent xmlIdEvent = xmlEventReader.nextEvent();
                     codeOkved.setId(xmlIdEvent.asCharacters().getData());
                     level--;
                     break;
                  case "status":
                     xmlEventReader.nextEvent();
                     XMLEvent xmlStatusEvent = xmlEventReader.nextEvent();
                     codeOkved.setStatus(Integer.parseInt(xmlStatusEvent.asCharacters().toString()));
                     level--;
                     break;
                  case "code":
                     xmlEventReader.nextEvent();
                     XMLEvent xmlCodeEvent = xmlEventReader.nextEvent();
                     codeOkved.setCode(xmlCodeEvent.asCharacters().toString());
                     level--;
                     break;
                  case "createDate":
                     xmlEventReader.nextEvent();
                     XMLEvent xmlDateEvent = xmlEventReader.nextEvent();
                     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d'T'H:m");
                     codeOkved.setCreateDate(dateFormat.parse(xmlDateEvent.asCharacters().getData()));
                     level--;
                     break;
               }
               break;
            case XMLStreamConstants.END_ELEMENT:
               level--;
               break;
         }
         xmlEventReader.nextEvent();
      } while (level > 1);
      return codeOkved;
   }
}