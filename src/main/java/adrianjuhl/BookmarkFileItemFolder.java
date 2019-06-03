package adrianjuhl;

import java.util.ArrayList;
import java.util.List;

public class BookmarkFileItemFolder implements BookmarkFileItem {

  private String labelText;
  private BookmarkFileItemList<? extends BookmarkFileItem> folderBookmarkItems;

  public BookmarkFileItemFolder(String labelText, BookmarkFileItemList<? extends BookmarkFileItem> folderBookmarkItems) {
    this.labelText = labelText;
    this.folderBookmarkItems = folderBookmarkItems;
  }

  private String labelText() {
    return labelText;
  }

  @Override
  public String asNetscapeBookmarkItem() {
    StringBuilder sb = new StringBuilder();
    sb.append("<DT><H3 ADD_DATE=\"1538996246\" LAST_MODIFIED=\"1538996246\">").append(labelText()).append("</H3>\n");
    sb.append("<DL><p>\n");
    for(BookmarkFileItem bookmarkItem : folderBookmarkItems.asList()) {
      sb.append(bookmarkItem.asNetscapeBookmarkItem()).append("\n");
    }
    sb.append("</DL><p>\n");
    return sb.toString();
  }

  
}
