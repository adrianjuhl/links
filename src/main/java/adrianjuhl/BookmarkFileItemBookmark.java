package adrianjuhl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class BookmarkFileItemBookmark implements BookmarkFileItem {

  private String url;
  private String labelText;
  private Collection<String> tags;

  public BookmarkFileItemBookmark(String url, String labelText, Collection<String> tags) {
    this.url = url;
    this.labelText = labelText;
    this.tags = new ArrayList<>();
    for(String tag : tags) {
      this.tags.add(tag);
    }
  }
  public String labelText() {
    return labelText;
  }
  public String url() {
    return url;
  }
  public Collection<String> tags() {
    return tags;
  }
  public String asNetscapeBookmarkItem() {
    return "<DT><A HREF=\"" + url() + "\" TAGS=\"" + tags.stream().collect(Collectors.joining(",")) + "\">" + labelText() + "</A>";
  }

}
