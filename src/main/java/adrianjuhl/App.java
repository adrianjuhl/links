package adrianjuhl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
      //System.out.println("doc:\n" + doc.toString());
      List<BookmarkFileItem> bukuBookmarkList = getBookmarks(doc);
      //Collection<BookmarkFileItemBookmark> bukuBookmarkItems = bukuBookmarkItems(doc);
      exportBookmarksFileForVivaldi(bukuBookmarkList);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void exportBookmarksFileForVivaldi(List<BookmarkFileItem> bukuBookmarkList) throws IOException {
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
    for(BookmarkFileItem bookmarkItem : bukuBookmarkList) {
      sb.append("    ").append(bookmarkItem.asNetscapeBookmarkItem()).append("\n");
    }
    List<BookmarkFileItem> tagFilteredBookmarks = filterByTagValue(bukuBookmarkList, "target_system_id:target_system_id_A");
    for(BookmarkFileItem bookmarkItem : tagFilteredBookmarks) {
      sb.append("  filterednew:  ").append(bookmarkItem.asNetscapeBookmarkItem()).append("\n");
    }
    BookmarkFileItemFolder folderTest = new BookmarkFileItemFolder("bookmarkfolder", tagFilteredBookmarks);
    sb.append(folderTest.asNetscapeBookmarkItem());
    
    SortedSet<String> folderPaths = getBookmarkMenuFolderPaths(bukuBookmarkList);
    for(String folderPath : folderPaths) {
      System.out.println("folderPath: " + folderPath);
      
    }
    
    Map<String,BookmarkFileItemFolder> parentPathToFolderMap = new HashMap<>();
    BookmarkFileItemFolder browserBookmarkRootFolder = new BookmarkFileItemFolder("bookmarks", null);
    parentPathToFolderMap.put(null, browserBookmarkRootFolder);
    
    browserBookmarkRootFolder.add(folderTest);
    for(String folderPath : folderPaths) {
      String parentPath;
      int substringIndex = folderPath.lastIndexOf("/");
      if(substringIndex < 0) {
        parentPath = null;
      } else {
        parentPath = folderPath.substring(0, substringIndex);
      }
      String folderLabel = folderPath.substring(substringIndex + 1);
      System.out.println("folderLabel: " + folderLabel + "  - parentPath: " + parentPath);
      BookmarkFileItemFolder folder = new BookmarkFileItemFolder(folderLabel, null);
      parentPathToFolderMap.put(folderPath, folder);
      List<BookmarkFileItem> filteredBookmarks = filterByTagValue(bukuBookmarkList, "bookmark_menu_folder_path:" + folderPath);
      for(BookmarkFileItem folderItem : filteredBookmarks) {
        folder.add(folderItem);
      }
      parentPathToFolderMap.get(parentPath).add(folder);
    }
    sb.append("browser bookmarks menu structure start\n");
    sb.append(browserBookmarkRootFolder.asNetscapeBookmarkItem()).append("\n");
    sb.append("browser bookmarks menu structure end\n");
    

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

//  private class TagFilteredBookmarkFileItemBookmarkList implements BookmarkFileItemBookmarkList {
//    private BookmarkFileItemBookmarkList bookmarkList;
//    public TagFilteredBookmarkFileItemBookmarkList(BookmarkFileItemBookmarkList unfilteredBookmarkList, String filterTag) {
//      this.bookmarkList = new BookmarkFileItemBookmarkArrayList();
//      for(BookmarkFileItemBookmark bookmark : unfilteredBookmarkList.asList()) {
//        if(bookmark.tags().contains(filterTag)) {
//          this.bookmarkList.add(bookmark);
//        }
//      }
//    }
//    @Override
//    public List<BookmarkFileItemBookmark> asList() {
//      return bookmarkList.asList();
//    }
//    @Override
//    public void add(BookmarkFileItemBookmark bookmark) {
//      this.bookmarkList.add(bookmark);
//    }
//  }

  private SortedSet<String> getBookmarkMenuFolderPaths(List<BookmarkFileItem> bookmarkFileItems) {
    TreeSet<String> folderPaths = new TreeSet<>();
    for(BookmarkFileItem bookmarkFileItem : bookmarkFileItems) {
      if(bookmarkFileItem instanceof BookmarkFileItemBookmark) {
        BookmarkFileItemBookmark bookmark = (BookmarkFileItemBookmark)bookmarkFileItem;
        for(String tag : bookmark.tags()) {
          if(tag.startsWith("bookmark_menu_folder_path:")) {
            String path = tag.substring("bookmark_menu_folder_path:".length());
            String pathBuild = "";
            for(String s : path.split("/")) {
              if(pathBuild.isEmpty()) {
                pathBuild = s;
              } else {
                pathBuild = pathBuild + "/" + s;
              }
              folderPaths.add(pathBuild);
            }
          }
        }
      }
    }
    return folderPaths;
  }

  private List<BookmarkFileItem> getBookmarks(Document doc) {
    List<BookmarkFileItem> bookmarksList = new ArrayList<BookmarkFileItem>();
    Elements bookmarksDocDtElements = doc.select("body dl dt dl dt");
    for(Element e : bookmarksDocDtElements) {
      Element bukuBookmarkAnchorElement = e.select("a").first();
      List<String> tags = new ArrayList<>();
      String tagsAttr = bukuBookmarkAnchorElement.attr("tags");
      for(String tag : tagsAttr.split(",")) {
        tags.add(tag);
      }
      BookmarkFileItemBookmark bukuBookmarkItem = new BookmarkFileItemBookmark(bukuBookmarkAnchorElement.attr("href"), bukuBookmarkAnchorElement.text(), tags);
      bookmarksList.add(bukuBookmarkItem);
    }
    return bookmarksList;
  }

  private List<BookmarkFileItem> filterByTagValue(List<BookmarkFileItem> unfilteredBookmarkFileItems, String filterTag) {
    List<BookmarkFileItem> filteredBookmarkFileItems = new ArrayList<BookmarkFileItem>();
    for(BookmarkFileItem bookmarkFileItem : unfilteredBookmarkFileItems) {
      if(bookmarkFileItem instanceof BookmarkFileItemBookmark) {
        BookmarkFileItemBookmark bookmark = (BookmarkFileItemBookmark)bookmarkFileItem;
        if(bookmark.tags().contains(filterTag)) {
          filteredBookmarkFileItems.add(bookmark);
        }
      } else if(bookmarkFileItem instanceof BookmarkFileItemFolder) {
        filteredBookmarkFileItems.add(bookmarkFileItem);
      }
    }
    return filteredBookmarkFileItems;
  }

}
