package gitlet;

import java.io.Serializable;
import java.util.HashMap;
/** A branch class to construct branch objects.
 * @author Alfred Wang
 * */
public class Branch implements Serializable {

    /** A empty constructor. */
    public Branch() {
    }

    /** A constructor method.
     * @param name branch name
     * @param tracking tracking files */
    public Branch(String name, HashMap<String, String> tracking) {
        _name = name;
        _tracking = tracking;
    }

    /** A method to set a hashmap.
     *  contains which files are tracked
     *  @param tracking tracking files */
    public void setTracking(HashMap<String, String> tracking) {
        _tracking = tracking;
    }

    /** A method to set the head.
     * @param head Commit id */
    public void setHead(String head) {
        _head = head;
    }

    /** A method to set parent.
     * @param parent Commit id */
    public void setParent(String parent) {
        _parent = parent;
    }

    /** A method to set all files it contains.
     *  @param allFiles all files */
    public void setAllFiles(HashMap<String, String> allFiles) {
        _allFiles = allFiles;
    }

    /** A method return all files. */
    public HashMap<String, String> getAllFiles() {
        return _allFiles;
    }

    /** A method return all tracking files. */
    public HashMap<String, String> getTracking() {
        return _tracking;
    }

    /** A method return head. */
    public String getHead() {
        return _head;
    }
    /** A method return parent. */
    public String getParent() {
        return _parent;
    }

    /** A method return name. */
    public String getName() {
        return _name;
    }
    /** field to store name. */
    private String _name;
    /** field to store tracking files. */
    private HashMap<String, String> _tracking;
    /** field to store head. */
    private String _head;
    /** field to store parent. */
    private String _parent;
    /** field to store all files. */
    private HashMap<String, String> _allFiles;
}
