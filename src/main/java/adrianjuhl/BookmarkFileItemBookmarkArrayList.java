package adrianjuhl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BookmarkFileItemBookmarkArrayList implements BookmarkFileItemBookmarkList {

  private List<BookmarkFileItemBookmark> bookmarksList;

  public BookmarkFileItemBookmarkArrayList() {
    this.bookmarksList = new ArrayList<BookmarkFileItemBookmark>();
  }

  public BookmarkFileItemBookmarkArrayList(List<BookmarkFileItemBookmark> bookmarksList) {
    this.bookmarksList = new ArrayList<BookmarkFileItemBookmark>();
    if(bookmarksList != null) {
      for(BookmarkFileItemBookmark bookmark : bookmarksList) {
        this.bookmarksList.add(bookmark);
      }
    }
  }

  public BookmarkFileItemBookmarkArrayList(Document doc) {
    this.bookmarksList = new ArrayList<BookmarkFileItemBookmark>();
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
  }

  @Override
  public List<BookmarkFileItem> asList() {
    return null; //bookmarksList;
  }

  public void add(BookmarkFileItemBookmark bookmark) {
    this.bookmarksList.add(bookmark);

  }
}
