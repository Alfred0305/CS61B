package gitlet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/** A class handles commit object.
 *  @author Alfred Wang */
public class Commit implements Serializable {
    /** A default constructor. */
    public Commit() {
        _message = "initial commit";
        _timeStamp = "Thu Jan 1 00:00:00 1970 -0800";
        _files = null;
        _parents = null;
        _id = hashString();
    }

    /** A constructor.
     *  @param message message
     *  @param files tracking files
     *  @param parent parent commit id */
    public Commit(String message,
                  HashMap<String, String> files, String parent) {
        _message = message;
        _timeStamp = DATE_FORMAT.format(new Date()) + " -0800";
        _files = files;
        _parents = parent;
        _id = hashString();
    }

    /** Assign an unique code for each commit.
     *  @return string */
    public String hashString() {
        if (_files != null && _parents != null) {
            return Utils.sha1(_message, _timeStamp.toString(),
                    _files.toString(), _parents.toString());
        }
        return Utils.sha1(_message, _timeStamp.toString());
    }

    /** Return its ID. */
    public String getId() {
        return _id;
    }

    /** Return message. */
    public String getMessage() {
        return _message;
    }

    /** Return time stamp. */
    public String getTimeStamp() {
        return _timeStamp;
    }

    /** Return parent. */
    public String getParent() {
        return _parents;
    }

    /** Return all files. */
    public HashMap<String, String> allFiles() {
        return _files;
    }

    /** private field to store message. */
    private String _message;
    /** private field to store time stamp. */
    private String _timeStamp;
    /** private field to store files. */
    private HashMap<String, String> _files;
    /** private field to store parents. */
    private String _parents;
    /** private field to store id. */
    private String _id;

    /** Define a format for timestamp. */
    public static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");
}
