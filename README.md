
# vagrant

    vagrant up
    vagrant ssh
    # To print all bookmarks
    buku --print
    # To run bukuserver
    cd /vagrant
    ./run_bukuserver.sh
    # then browse to http://192.168.33.10:5000/


# Export

To export the bookmarks to a bookmark html file ready to be imported into a browser:

    $ vagrant ssh
    $ buku.export.bookmarks.to.html
    Note: the previous command is an alias for: $ buku --export /vagrant/data/bookmarks_from_buku_new.html
    
    Then on the host:
    $ mvn clean package exec:java -Dexec.mainClass="adrianjuhl.App"
    
    The bookmark html file will then exist at data/bookmarks_export_for_browsers.html
    Then import this file into the browser.

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
Bookmarks have the following properties in the app: Title (Name/Label), Address (URL), Nickname, Description
Nickname and Description are not exported.
Example export line (HTML):

    <DT><A HREF="http://127.0.0.1/blahbookmark" ADD_DATE="1540289599">127.0.0.1</A>
    The 'DT A HREF' is the Address (URL)
    The 'DT A text' is the Title (Name/Label)


    target_user_id$TAG_ELEMENT_SEPARATOR$<user_id>$TAG_ELEMENT_SEPARATOR$browser_bookmark_menu_path$TAG_ELEMENT_SEPARATOR$<path>

    ABMT=<context_id>~%~<path>


Restriction: when a bookmark file with tags is imported into buku, tag values are converted to lower-case. This effectively prevents being able to use the browser as the tool to edit/maintain tags for bookmarks as the import of ABMT tags back into buku will be converted to lower-case with will alter the menu labels.
