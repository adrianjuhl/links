package adrianjuhl;

import java.util.List;

public interface BookmarkFileItemBookmarkList extends BookmarkFileItemList<BookmarkFileItemBookmark> {

  public List<BookmarkFileItemBookmark> asList();

  public void add(BookmarkFileItemBookmark bookmark); 

}
