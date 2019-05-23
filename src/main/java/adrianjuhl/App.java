package adrianjuhl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Hello world!
 *
 */
public class App {

  public static void main( String[] args ) {
    //System.out.println( "Hello World!" );
    App app = new App();
    app.go();
  }

  private void go() {
    System.out.println( "Hello World!" );
    String bookmarksHtmlString;
    try {
      bookmarksHtmlString = new String(Files.readAllBytes(Paths.get("data/bookmarks_from_buku.html")));
      //System.out.println("File contents: " + bookmarksHtmlString);
      Document doc = Jsoup.parse(bookmarksHtmlString);
      Elements elements = doc.getElementsByTag("H1");
      //for(Element e : elements) {
      //  System.out.println("e is " + e.toString());
      //}
      Files.write(Paths.get("data/bookmarks_export.html"), bookmarksHtmlString.getBytes());
      exportBookmarksFileForVivaldi(doc);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void exportBookmarksFileForVivaldi(Document doc) throws IOException {
    //System.out.println("doc:\n" + doc.toString());
    Elements bookmarksDtElements = doc.select("body dl dt dl dt");
    Elements bookmarksDdElements = doc.select("body dl dt dl dd");
    System.out.println("bookmarksDtElements size is " + bookmarksDtElements.size());
    System.out.println("bookmarksDdElements size is " + bookmarksDdElements.size());
    StringBuilder sb = new StringBuilder();
    sb.append(bookmarkFileHeader());
    sb.append("<DL><p>\n");
    sb.append("  <DT><H3>BookmarksFromExportForVivaldi</H3>\n");
    sb.append("  <DL><p>\n");
    sb.append("    <DT><A HREF=\"http://google.com.au/\">google.com.au/</A>\n");
    int i=0;
    for(Element e : bookmarksDtElements) {
      Element descr = e.nextElementSibling();
      String descrString = "";
      if(descr != null && descr.is("dd")) {
        descrString = descr.toString();
      } else {
        descrString = "No corresponding dd element";
      }
      //System.out.println("pair: " + ++i + " is " + e.toString() + " - " + descrString);
      sb.append("    ").append(bookmarkFileVivaldiBookmark(e, descr)).append("\n");
    }
    sb.append("  </DL><p>\n");
    sb.append("</DL><p>\n");
    Files.write(Paths.get("data/bookmarks_export_for_vivaldi.html"), sb.toString().getBytes());
  }

  private String bookmarkFileHeader() {
    return 
      "<!DOCTYPE NETSCAPE-Bookmark-file-1>\n" +
      "<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\">\n" +
      "<TITLE>Bookmarks</TITLE>\n" +
      "<H1>Bookmarks</H1>";
  }

  private String bookmarkFileVivaldiBookmark(Element dtElement, Element ddElement) {
    Element dtAnchorElement = dtElement.select("a").first();
    String href = dtAnchorElement.attr("href");
    String anchorText = "UNNAMED_BOOKMARK";
    if(ddElement != null) {
      anchorText = ddElement.text();
    }
    return "<DT><A HREF=\"" + href + "\">" + anchorText + "</A>";
  }

}
