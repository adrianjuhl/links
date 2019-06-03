package adrianjuhl;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class BookmarkFileItemFolder implements BookmarkFileItem {

  private String labelText;
  private SortedSet<BookmarkFileItem> folderBookmarkItems;

  public BookmarkFileItemFolder(String labelText, List<BookmarkFileItem> folderBookmarkItems) {
    this.labelText = labelText;
    this.folderBookmarkItems = new TreeSet<BookmarkFileItem>(new BookmarkFileItem.BookmarkFileItemFolderOrderComparator());
    if(folderBookmarkItems != null) {
      for(BookmarkFileItem bookmark : folderBookmarkItems) {
        this.folderBookmarkItems.add(bookmark);
      }
    }
  }

  public String labelText() {
    return labelText;
  }

  public void add(BookmarkFileItem item) {
    folderBookmarkItems.add(item);
  }

  @Override
  public String asNetscapeBookmarkItem() {
    StringBuilder sb = new StringBuilder();
    sb.append("<DT><H3 ADD_DATE=\"1538996246\" LAST_MODIFIED=\"1538996246\">").append(labelText()).append("</H3>\n");
    sb.append("<DL><p>\n");
    for(BookmarkFileItem bookmarkItem : folderBookmarkItems) {
      sb.append(bookmarkItem.asNetscapeBookmarkItem()).append("\n");
    }
    sb.append("</DL><p>\n");
    return sb.toString();
  }

  
}
