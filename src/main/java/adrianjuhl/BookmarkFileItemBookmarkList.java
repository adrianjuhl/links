package adrianjuhl;

import java.util.List;

public interface BookmarkFileItemBookmarkList extends BookmarkFileItemList {

  public List<BookmarkFileItem> asList();

  public void add(BookmarkFileItemBookmark bookmark); 

}
