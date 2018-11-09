package program.help;

import bean.BusinessEntity;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class FileResolver {

   public String getXmlFileAbsolutePath(String xmlFileName) {
      return getClass().getClassLoader().getResource(xmlFileName).getPath();
   }

   public void writeResultHtmlFile(List<BusinessEntity> businessEntities) {
      try {
         File htmlFile = new File("result.html");
         if (!htmlFile.createNewFile()) {
            System.err.println("Error during create new result .html file");
         } else {
            Document document = createSimpleResultHtmlFile();
            Element table = appendTableToResultHtmlPage(document);
            appendInfoToTable(businessEntities, table);

            FileUtils.writeStringToFile(htmlFile, document.outerHtml(), "UTF-8");
         }
      } catch (IOException exception) {
         exception.printStackTrace();
      }
   }

   private Document createSimpleResultHtmlFile() {
      Document document = Jsoup.parse("<html></html>");
      document.head().appendElement("meta").attr("charset", "UTF-8");
      document.head().appendElement("link").attr("rel", "stylesheet").attr("href", "styles.css");
      return document;
   }

   private Element appendTableToResultHtmlPage(Document resultHtmlPage) {
      Element table = resultHtmlPage.body().appendElement("table").addClass("result-table");
      Element tableTitles = table.appendElement("thead").appendElement("tr");
      tableTitles.appendElement("th").appendText("Идентификатор");
      tableTitles.appendElement("th").appendText("Статус записи");
      tableTitles.appendElement("th").appendText("Наименование");
      tableTitles.appendElement("th").appendText("Статус в реестре");
      tableTitles.appendElement("th").appendText("Дата создания");
      tableTitles.appendElement("th").appendText("Коды ОКВЭД");
      return table;
   }

   private void appendInfoToTable(List<BusinessEntity> businessEntities, Element table) {
      Element tableBody = table.appendElement("tbody");
      businessEntities.forEach(businessEntity -> {
         Element tableRow = tableBody.appendElement("tr");
         tableRow.appendElement("th").appendText(businessEntity.getId());
         tableRow.appendElement("th").appendText(String.valueOf(businessEntity.getStatus()));
         tableRow.appendElement("th").appendText(businessEntity.getName());
         tableRow.appendElement("th").appendText(businessEntity.getRegistryStatus().toString());
         tableRow.appendElement("th").appendText(new SimpleDateFormat("yyyy-M-d H:m").format(businessEntity.getCreateDate()));
         tableRow.appendElement("th").appendText(businessEntity.getCodeOkvedList().isEmpty() ? "Нет записей" : getCodesOkvedFromEntity(businessEntity));
      });
   }

   private String getCodesOkvedFromEntity(BusinessEntity entity) {
      StringBuilder stringBuilder = new StringBuilder();
      entity.getCodeOkvedList().forEach(codeOkved -> {
         stringBuilder.append(String.format("%s; ", codeOkved.getCode()));
      });
      return stringBuilder.toString();
   }
}
