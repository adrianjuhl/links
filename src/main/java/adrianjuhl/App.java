package adrianjuhl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
      System.out.println("doc:\n" + doc.toString());
      Collection<BookmarkItem> bukuBookmarkItems = bukuBookmarkItems(doc);
      exportBookmarksFileForVivaldi(bukuBookmarkItems);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void exportBookmarksFileForVivaldi(Collection<BookmarkItem> bukuBookmarkItems) throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append(bookmarkFileHeader());
    sb.append("<DL><p>\n");
    sb.append("  <DT><H3>BookmarksFromExportForVivaldi</H3>\n");
    sb.append("  <DL><p>\n");
    sb.append("    <DT><A HREF=\"http://google.com.au/\">google.com.au/</A>\n");
    for(BookmarkItem bukuBookmarkItem : bukuBookmarkItems) {
      sb.append("    ").append(bukuBookmarkItem.asNetscapeBookmarkItem()).append("\n");
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

  private class BookmarkItem {
    private String url;
    private String name;
    private List<String> tags;
    public BookmarkItem(String url, String name, List<String> tags) {
      this.url = url;
      this.name = name;
      this.tags = new ArrayList<>();
      for(String tag : tags) {
        this.tags.add(tag);
      }
    }
    public String name() {
      return name;
    }
    public String url() {
      return url;
    }
    public String asNetscapeBookmarkItem() {
      return "<DT><A HREF=\"" + url() + "\" TAGS=\"" + tags.get(0) + "\">" + name() + "</A>";
    }
  }

  private Collection<BookmarkItem> bukuBookmarkItems(Document doc) {
    List<BookmarkItem> bookmarkItems = new ArrayList<>();
    Elements bookmarksDtElements = doc.select("body dl dt dl dt");
    for(Element e : bookmarksDtElements) {
      Element bukuBookmarkAnchorElement = e.select("a").first();
      List<String> tags = new ArrayList<>();
      String tagsAttr = bukuBookmarkAnchorElement.attr("tags");
      for(String tag : tagsAttr.split(",")) {
        tags.add(tag);
      }
      BookmarkItem bukuBookmarkItem = new BookmarkItem(bukuBookmarkAnchorElement.attr("href"), bukuBookmarkAnchorElement.text(), tags);
      bookmarkItems.add(bukuBookmarkItem);
    }
    return bookmarkItems;
  }

}
