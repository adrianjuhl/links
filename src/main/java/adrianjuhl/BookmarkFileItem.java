package adrianjuhl;

import java.util.Comparator;

public interface BookmarkFileItem extends BookmarkHtmlProducer {

  public class BookmarkFileItemFolderOrderComparator implements Comparator<BookmarkFileItem> {
    @Override
    public int compare(BookmarkFileItem itemA, BookmarkFileItem itemB) {
      int result = 0;
      if(itemA instanceof BookmarkFileItemFolder) {
        BookmarkFileItemFolder folderA = (BookmarkFileItemFolder)itemA;
        if(itemB instanceof BookmarkFileItemFolder) {
          BookmarkFileItemFolder folderB = (BookmarkFileItemFolder)itemB;
          result = folderA.labelText().compareTo(folderB.labelText());
        } else if(itemB instanceof BookmarkFileItemBookmark) {
          result = -1;
        }
      } else if(itemA instanceof BookmarkFileItemBookmark) {
        BookmarkFileItemBookmark bookmarkA = (BookmarkFileItemBookmark)itemA;
        if(itemB instanceof BookmarkFileItemFolder) {
          result = 1;
        } else if(itemB instanceof BookmarkFileItemBookmark) {
          BookmarkFileItemBookmark bookmarkB = (BookmarkFileItemBookmark)itemB;
          result = bookmarkA.labelText().compareTo(bookmarkB.labelText());
        }
      }
      return result;
    }
  }

}
