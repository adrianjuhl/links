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
      //Elements elements = doc.getElementsByTag("H1");
      //for(Element e : elements) {
      //  System.out.println("e is " + e.toString());
      //}
      Files.write(Paths.get("data/bookmarks_export.html"), bookmarksHtmlString.getBytes());
      System.out.println("doc:\n" + doc.toString());
      BookmarkFileItemBookmarkList bukuBookmarkList = new BookmarkFileItemBookmarkArrayList(doc);
      //Collection<BookmarkFileItemBookmark> bukuBookmarkItems = bukuBookmarkItems(doc);
      exportBookmarksFileForVivaldi(bukuBookmarkList);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void exportBookmarksFileForVivaldi(BookmarkFileItemBookmarkList bukuBookmarkList) throws IOException {
    Collection<String> tags = new ArrayList<String>();
    tags.add("target_folder_path:subfolderOne");
    tags.add("target_system_id:target_system_id_A");
    BookmarkFileItemBookmark bookmarkItemTest = new BookmarkFileItemBookmark("theurl", "bookmarkname", tags);
    bukuBookmarkList.add(bookmarkItemTest);
    StringBuilder sb = new StringBuilder();
    sb.append(bookmarkFileHeader());
    sb.append("<DL><p>\n");
    sb.append("  <DT><H3>BookmarksFromExportForVivaldi</H3>\n");
    sb.append("  <DL><p>\n");
    sb.append("    <DT><A HREF=\"http://google.com.au/\">google.com.au/</A>\n");
    //BookmarkItems bookmarkItems = new BookmarkItems(bukuBookmarkItemsCollection);
    for(BookmarkFileItemBookmark bookmarkItem : bukuBookmarkList.asList()) {
      sb.append("    ").append(bookmarkItem.asNetscapeBookmarkItem()).append("\n");
    }
    BookmarkFileItemBookmarkList tagFilteredBookmarks = new TagFilteredBookmarkFileItemBookmarkList(bukuBookmarkList, "target_system_id:target_system_id_A");
    for(BookmarkFileItemBookmark bookmarkItem : tagFilteredBookmarks.asList()) {
      sb.append("  filterednew:  ").append(bookmarkItem.asNetscapeBookmarkItem()).append("\n");
    }
    BookmarkFileItemFolder folderTest = new BookmarkFileItemFolder("bookmarkfolder", tagFilteredBookmarks);
    sb.append(folderTest.asNetscapeBookmarkItem());
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

  private class TagFilteredBookmarkFileItemBookmarkList implements BookmarkFileItemBookmarkList {
    private BookmarkFileItemBookmarkList bookmarkList;
    public TagFilteredBookmarkFileItemBookmarkList(BookmarkFileItemBookmarkList unfilteredBookmarkList, String filterTag) {
      this.bookmarkList = new BookmarkFileItemBookmarkArrayList();
      for(BookmarkFileItemBookmark bookmark : unfilteredBookmarkList.asList()) {
        if(bookmark.tags().contains(filterTag)) {
          this.bookmarkList.add(bookmark);
        }
      }
    }
    @Override
    public List<BookmarkFileItemBookmark> asList() {
      return bookmarkList.asList();
    }
    @Override
    public void add(BookmarkFileItemBookmark bookmark) {
      this.bookmarkList.add(bookmark);
    }
  }

}
