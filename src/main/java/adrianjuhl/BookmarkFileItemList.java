package adrianjuhl;

import java.util.List;

public interface BookmarkFileItemList<T extends BookmarkFileItem> {

  public List<T> asList();

}
