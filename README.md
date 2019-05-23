
# vagrant

    vagrant up
    vagrant ssh
    # To print all bookmarks
    buku --print


# Export

    mvn exec:java -Dexec.mainClass="adrianjuhl.App"

# Notes

The current vagrant up provisioning copies bookmarks.db from /vagrant/data/bookmarks.db (i.e. in /data in the repo) to ~/.local/share/buku/ for usage by 

# Resources

https://docs.microsoft.com/en-us/previous-versions/windows/internet-explorer/ie-developer/platform-apis/aa753582(v=vs.85)
https://sixtwothree.org/posts/homesteading-a-decades-worth-of-shared-links

# TODO
* Determine how each kind of browser imports and exports with respect to tags and folder structures.

Chrome
Bookmarks have only the following properties: Name, URL
Example export line (HTML):
<DT><A HREF="http://127.0.0.1/blahbookmark" ADD_DATE="1540287654">127.0.0.1</A>
A HREF is the URL
A text is the Name

Firefox
Bookmarks have the following properties: Name, Location (URL), Tags (comma separated values), Keyword (free text)
Example export line (HTML):
<DT><A HREF="http://127.0.0.1/blahbookmark" ADD_DATE="1540288083" LAST_MODIFIED="1540288328" SHORTCUTURL="keyword" TAGS="tag,2018sep22">blahbookmark</A>
A HREF is the URL
A text is the Name
A SHORTCUTURL is the keyword text
A TAGS are the tag values, comma separate

Vivaldi
Bookmarks have the following properties: Title (Name), Address (URL), Nickname, Description
Nickname and Description are not exported.
Example export line (HTML):
<DT><A HREF="http://127.0.0.1/blahbookmark" ADD_DATE="1540289599">127.0.0.1</A>
A HREF is the URL
A text is the Name

