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

public class App {

  public static final String TAG_ELEMENT_SEPARATOR = "TAG_ELEMENT_SEPARATOR";

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
      //Files.write(Paths.get("data/bookmarks_export.html"), bookmarksHtmlString.getBytes());
      //System.out.println("doc:\n" + doc.toString());
      List<BookmarkFileItem> bukuBookmarkList = getBookmarks(doc);
      //Collection<BookmarkFileItemBookmark> bukuBookmarkItems = bukuBookmarkItems(doc);
      exportBookmarksFileForBrowsers(bukuBookmarkList);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void exportBookmarksFileForBrowsers(List<BookmarkFileItem> bukuBookmarkList) throws IOException {
    //Collection<String> tags = new ArrayList<String>();
    //tags.add("target_folder_path:subfolderOne");
    //tags.add("target_system_id:target_system_id_A");
    //BookmarkFileItemBookmark bookmarkItemTest = new BookmarkFileItemBookmark("theurl", "bookmarkname", tags);
    //bukuBookmarkList.add(bookmarkItemTest);

    
    // Get the bookmarks for the user/context.
    String contextId = "adrian-work";
    String searchValue = "ABMT=" + contextId;
    List<BookmarkFileItem> userBookmarks = filterByTagStartsWith(bukuBookmarkList, searchValue);
    for(BookmarkFileItem bookmark : userBookmarks) {
      System.out.println("user bookmark: " + bookmark.asNetscapeBookmarkItem());
    }

    // Get the folder paths of the bookmarks.
    SortedSet<String> folderPaths = getBookmarkMenuFolderPaths(userBookmarks, contextId);
    for(String folderPath : folderPaths) {
      System.out.println("folderPath: " + folderPath);
    }

    // Construct the browser bookmarks folder content.
    Map<String,BookmarkFileItemFolder> parentPathToFolderMap = new HashMap<>();
    BookmarkFileItemFolder browserBookmarkRootFolder = new BookmarkFileItemFolder("bookmarks for " + contextId, null);
    parentPathToFolderMap.put(null, browserBookmarkRootFolder);
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
      String folderPathTagSearchValue = "ABMT=" + contextId + "~%~" + folderPath;
      List<BookmarkFileItem> filteredBookmarks = filterByTagValue(userBookmarks, folderPathTagSearchValue);
      for(BookmarkFileItem folderItem : filteredBookmarks) {
        System.out.println("filtered item for folderpath '" + folderPath + "' : " + folderItem.asNetscapeBookmarkItem());
        folder.add(folderItem);
      }
      parentPathToFolderMap.get(parentPath).add(folder);
    }
    
    StringBuilder sb = new StringBuilder();
    sb.append(bookmarkFileHeader());
    sb.append("<DL><p>\n");
    sb.append("  <DT><H3>BookmarksFromExportForVivaldi</H3>\n");
    sb.append("  <DL><p>\n");
    sb.append(browserBookmarkRootFolder.asNetscapeBookmarkItem());
    sb.append("  </DL><p>\n");
    sb.append("</DL><p>\n");
    Files.write(Paths.get("data/bookmarks_export_for_browsers.html"), sb.toString().getBytes());
  }

  private String bookmarkFileHeader() {
    return 
      "<!DOCTYPE NETSCAPE-Bookmark-file-1>\n" +
      "<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\">\n" +
      "<TITLE>Bookmarks</TITLE>\n" +
      "<H1>Bookmarks</H1>";
  }

  private SortedSet<String> getBookmarkMenuFolderPaths(List<BookmarkFileItem> bookmarkFileItems, String contextId) {
    String searchPrefix = "ABMT=" + contextId + "~%~";
    System.out.println("getBookmarkMenuFolderPaths searchPrefix: " + searchPrefix);
    TreeSet<String> folderPaths = new TreeSet<>();
    for(BookmarkFileItem bookmarkFileItem : bookmarkFileItems) {
      if(bookmarkFileItem instanceof BookmarkFileItemBookmark) {
        BookmarkFileItemBookmark bookmark = (BookmarkFileItemBookmark)bookmarkFileItem;
        for(String tag : bookmark.tags()) {
          if(tag.startsWith(searchPrefix)) {
            String path = tag.substring(searchPrefix.length());
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

  private List<BookmarkFileItem> filterByTagStartsWith(List<BookmarkFileItem> unfilteredBookmarkFileItems, String filterTag) {
    List<BookmarkFileItem> filteredBookmarkFileItems = new ArrayList<BookmarkFileItem>();
    for(BookmarkFileItem bookmarkFileItem : unfilteredBookmarkFileItems) {
      if(bookmarkFileItem instanceof BookmarkFileItemBookmark) {
        BookmarkFileItemBookmark bookmark = (BookmarkFileItemBookmark)bookmarkFileItem;
        for(String tag : bookmark.tags()) {
          if(tag.startsWith(filterTag)) {
            filteredBookmarkFileItems.add(bookmark);
          }
        }
      } else if(bookmarkFileItem instanceof BookmarkFileItemFolder) {
        filteredBookmarkFileItems.add(bookmarkFileItem);
      }
    }
    return filteredBookmarkFileItems;
  }

}
