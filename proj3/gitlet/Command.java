package gitlet;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** A class handles all commands.
 * @author Alfred Wang
 * */
public class Command implements Serializable {
    /** Initialize a Command. */
    public Command() {
        File gitlet = new File(".gitlet");
        gitlet.mkdir();
        File stage = new File(".gitlet/stage");
        stage.mkdir();
        File commits = new File(".gitlet/commits");
        commits.mkdir();

        Commit initial = new Commit();
        _latestCommitId = new HashMap<>();
        _untrackedFiles = new ArrayList<>();
        _trackingFileIds = new HashMap<>();
        _allCommits = new HashMap<>();
        _removedFiles = new ArrayList<>();
        _branches = new HashMap<>();
        _head = "master";
        Branch bran = new Branch();
        bran.setHead(initial.getId());
        bran.setParent(initial.getId());
        _branches.put(_head, bran);
        _latestCommitId.put(_head, initial.getId());
        _allCommits.put("initial commit", initial);
        File initialFile = new File(
                ".gitlet/commits/" + initial.getId());
        Utils.writeObject(initialFile, initial);
    }

    /** Staging an already-staged file overwrites the previous.
     *  If the current version is same to the current commit
     *  remove it from the staging area
     *  @param str filename */
    public void add(String str) {
        File tmp = new File(str);
        if (!tmp.exists()) {
            Utils.message("File does not exist.");
            System.exit(0);
        } else {
            if (_removedFiles.contains(str)) {
                _removedFiles.remove(str);
            }
            String fileID = getFileID(tmp);
            String head = _branches.get(_head).getHead();
            Commit last = getCommit(head);
            File staging = new File(".gitlet/stage/" + fileID);
            if (last.allFiles() == null
                    || !last.allFiles().containsKey(str)
                    || !last.allFiles().get(str).equals(fileID)) {
                _trackingFileIds.put(str, fileID);
                Utils.writeContents(staging, Utils.readContents(tmp));
            } else {
                if (_trackingFileIds.containsKey(str)) {
                    _trackingFileIds.remove(str);
                }
            }
            _branches.get(_head).setTracking(_trackingFileIds);
        }
    }

    /** Commit current changes.
     *  @param message message */
    public void commit(String message) {
        if (_trackingFileIds.size() == 0
                && _removedFiles.size() == 0) {
            Utils.message("No changes added to the commit.");
            System.exit(0);
        }
        if (message.equals("")) {
            Utils.message("Please enter a commit message.");
            System.exit(0);
        }

        String head = _branches.get(_head).getHead();
        Commit last = getCommit(head);
        Commit newCommit = new Commit(
                message, _trackingFileIds, last.getId());
        String newId = newCommit.getId();
        File newFile = new File(".gitlet/commits/" + newId);
        Utils.writeObject(newFile, newCommit);
        while (_allCommits.containsKey(message)) {
            message = message + 10 * 10 * 10 * Math.random();
        }
        _allCommits.put(message, newCommit);
        _branches.get(_head).setHead(newId);
        _trackingFileIds = new HashMap<>();
        _branches.get(_head).setTracking(new HashMap<>());
        _untrackedFiles = new ArrayList<>();
        _removedFiles = new ArrayList<>();
        _latestCommitId.put(_head, newId);
    }

    /** Unstage the file if it is currently staged.
     *  and remove the file from the working directory
     * @param args name of the file want to remove*/
    public void rm(String[] args) {
        if (args.length != 1) {
            Utils.message("Incorrect operands.");
            System.exit(0);
        }

        String fileName = args[0];
        if (_trackingFileIds.containsKey(fileName)) {
            String fileId = _trackingFileIds.get(fileName);
            File staging = new File(".gitlet/stage/" + fileId);
            staging.delete();
            _trackingFileIds.remove(fileName);
        } else {
            File staging = getFile(fileName);
            if (staging != null && staging.exists()) {
                File tmp = new File(fileName);
                if (tmp.exists()) {
                    tmp.delete();
                }
                _removedFiles.add(fileName);
            } else {
                Utils.message("No reason to remove the file.");
                System.exit(0);
            }
        }
    }

    /** Print out commits in current branch. */
    public void log() {
        Commit last = getCommit(_latestCommitId.get(_head));
        System.out.println("===");
        System.out.println("commit " + last.getId());
        System.out.println("Date: " + last.getTimeStamp());
        System.out.println(last.getMessage());
        System.out.println();
        while (last.getParent() != null) {
            last = getCommit(last.getParent());
            System.out.println("===");
            System.out.println("commit " + last.getId());
            System.out.println("Date: " + last.getTimeStamp());
            System.out.println(last.getMessage());
            System.out.println();
        }
    }

    /** Print out all commits. */
    public void globalLog() {
        for (Commit last : _allCommits.values()) {
            System.out.println("===");
            System.out.println("commit " + last.getId());
            System.out.println("Date: " + last.getTimeStamp());
            System.out.println(last.getMessage());
            System.out.println();
        }
    }

    /** Print out commit IDs with given message.
     *  @param message message */
    public void find(String message) {
        int check = 0;
        for (String str : _allCommits.keySet()) {
            if (str.contains(message)) {
                System.out.println(_allCommits.get(str).getId());
                check++;
            }
        }
        if (check == 0) {
            Utils.message("Found no commit with that message.");
            System.exit(0);
        }
    }

    /** Print out the status.
     * last two sections are extra credit */
    public void status() {
        System.out.println("=== Branches ===");
        for (String branchName : _branches.keySet()) {
            if (branchName.equals("master")) {
                if (_head.equals("master")) {
                    System.out.println("*master");
                } else {
                    System.out.println("master");
                }
            }
        }
        for (String branchName : _branches.keySet()) {
            if (!branchName.equals("master")) {
                if (_head.equals(branchName)) {
                    System.out.println("*" + branchName);
                } else {
                    System.out.println(branchName);
                }
            }
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        for (String fileName : _trackingFileIds.keySet()) {
            System.out.println(fileName);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        for (String fileName : _removedFiles) {
            System.out.println(fileName);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();

        updateUntrack();
        System.out.println("=== Untracked Files ===");
        for (String fileName : _untrackedFiles) {
            System.out.println(fileName);
        }
        System.out.println();
    }
    /** Checkout in three ways.
     *  @param args arguments */
    public void checkout(String[] args) {
        if (args.length == 1) {
            checkoutBranch(args[0]);
        } else {
            String commitId = null;
            String fileName = null;
            if (args.length == 2 && args[0].equals("--")) {
                fileName = args[1];
                commitId = _branches.get(_head).getHead();
            } else if (args.length == 3 && args[1].equals("--")) {
                commitId = fullCommitId(args[0]);
                fileName = args[2];
            } else {
                Utils.message("Incorrect operands");
                System.exit(0);
            }

            Commit comm = getCommit(commitId);
            HashMap<String, String> allFiles = comm.allFiles();
            if (allFiles.containsKey(fileName)) {
                String fileId = allFiles.get(fileName);
                File oldVersion = new File(".gitlet/stage/" + fileId);
                File recover = new File(fileName);
                String contents = Utils.readContentsAsString(oldVersion);
                Utils.writeContents(recover, contents);
            } else {
                Utils.message("File does not exist in that commit.");
                System.exit(0);
            }
        }
    }
    /** A helper function for checkout.
     *  @param str branch name */
    private void checkoutBranch(String str) {
        updateUntrack();
        if (_untrackedFiles.size() != 0) {
            Utils.message("There is an untracked file in the way;"
                    + " delete it or add it first.");
            System.exit(0);
        }
        String branchName = str;

        if (_head.equals(branchName)) {
            Utils.message("No need to check out the current branch.");
            System.exit(0);
        }
        if (!_branches.keySet().contains(branchName)) {
            Utils.message("No such branch exists.");
            System.exit(0);
        }

        List<String> allFiles = Utils.plainFilenamesIn(
                System.getProperty("user.dir"));
        HashMap<String, String> filesInOldBranch = new HashMap<>();
        for (String fileName : allFiles) {
            File tmp = new File(fileName);
            if (!fileName.equals(".DS_Store")) {
                filesInOldBranch.put(fileName, getFileID(tmp));
                tmp.delete();
            }
        }
        _branches.get(_head).setAllFiles(filesInOldBranch);
        _branches.get(_head).setTracking(_trackingFileIds);
        _head = branchName;
        HashMap<String, String> filesInBranch;
        filesInBranch = _branches.get(_head).getAllFiles();
        if (filesInBranch != null) {
            for (String fileName : filesInBranch.keySet()) {
                File tmp = new File(fileName);
                File content = new File(".gitlet/stage/"
                        + filesInBranch.get(fileName));
                Utils.writeContents(tmp, Utils.readContents(content));
            }
        }
        _trackingFileIds = _branches.get(_head).getTracking();
    }

    /** Construct a branch with given name.
     *  @param args arguments */
    public void branch(String[] args) {
        if (args.length != 1) {
            Utils.message("Incorrect operands.");
            System.exit(0);
        }
        String branchName = args[0];

        if (_branches.keySet().contains(branchName)) {
            Utils.message("A branch with that name already exists.");
            System.exit(0);
        } else {
            Branch bran = new Branch();
            bran.setHead(_latestCommitId.get(_head));
            bran.setParent(_latestCommitId.get(_head));
            HashMap<String, String> filesInBranch =
                    getCommit(_latestCommitId.get(_head)).allFiles();
            bran.setAllFiles(filesInBranch);
            bran.setTracking(new HashMap<>());
            _branches.put(branchName, bran);
        }
    }

    /** Remove given branch.
     *  @param args arguments */
    public void rmBranch(String[] args) {
        if (args.length != 1) {
            Utils.message("Incorrect operands.");
            System.exit(0);
        }
        String branchName = args[0];

        if (_branches.keySet().equals(branchName)) {
            Utils.message("A branch with that name does not exists.");
            System.exit(0);
        } else if (_head.equals(branchName)) {
            Utils.message("Can not remove the current branch.");
            System.exit(0);
        } else {
            _branches.remove(branchName);
        }
    }

    /** Reset files to a condition in given Commit.
     *  @param args arguments */
    public void reset(String[] args) {
        if (args.length != 1) {
            Utils.message("Incorrect operands.");
            System.exit(0);
        }
        String commitId = fullCommitId(args[0]);

        updateUntrack();
        if (_untrackedFiles.size() != 0) {
            Utils.message("There is an untracked file in the way;"
                    + " delete it or add it first.");
            System.exit(0);
        }

        List<String> allFiles = Utils.plainFilenamesIn(
                System.getProperty("user.dir"));
        for (String fileName : allFiles) {
            File tmp = new File(fileName);
            if (!fileName.equals(".DS_Store")) {
                tmp.delete();
            }
        }

        HashMap<String, String> filesInCommit;
        filesInCommit = getCommit(commitId).allFiles();
        if (filesInCommit != null) {
            for (String fileName : filesInCommit.keySet()) {
                File tmp = new File(fileName);
                File content = new File(".gitlet/stage/"
                        + filesInCommit.get(fileName));
                Utils.writeContents(tmp, Utils.readContents(content));
            }
        }
        _trackingFileIds = new HashMap<>();
        _untrackedFiles = new ArrayList<>();
        _removedFiles = new ArrayList<>();
        _latestCommitId.put(_head, commitId);
    }

    /** Merge files from given branch.
     *  @param branchName branch name */
    public void merge(String branchName) {
        canMerge(branchName);
        String ancestorId = findAncestor(branchName, _head);
        Commit ancestorCommit
                = getCommit(ancestorId);
        Commit currentBranch
                = getCommit(_latestCommitId.get(_head));
        Commit givenBranch
                = getCommit(_latestCommitId.get(branchName));
        HashMap<String, String> ancestorFiles
                = ancestorCommit.allFiles();
        HashMap<String, String> currentFiles
                = _branches.get(_head).getAllFiles();
        HashMap<String, String> givenFiles
                = _branches.get(branchName).getAllFiles();

        for (String fileName : ancestorFiles.keySet()) {
            File tmp = new File(fileName);
            if (tmp.exists()) {
                if (modified(fileName, givenFiles, ancestorFiles)
                        && !modified(fileName, currentFiles, ancestorFiles)) {
                    checkout(new String[]{givenBranch.getId(), "--", fileName});
                    add(fileName);
                } else if (!modified(fileName, givenFiles, ancestorFiles)
                        && modified(fileName, currentFiles, ancestorFiles)) {
                    continue;
                } else if (!modified(fileName, currentFiles, ancestorFiles)
                        && !givenFiles.keySet().contains(fileName)) {
                    tmp.delete();
                }  else if (modified(fileName, givenFiles, ancestorFiles)
                        && modified(fileName, currentFiles, ancestorFiles)
                        && (!givenFiles.keySet().contains(fileName)
                        || !givenFiles.get(fileName).equals(getFileID(tmp)))) {
                    conflictMerge();
                }
            }
        }

        for (String fileName : givenFiles.keySet()) {
            if (!ancestorFiles.keySet().contains(fileName)
                    && !currentFiles.keySet().contains(fileName)) {
                checkout(new String[]{givenBranch.getId(), "--", fileName});
                add(fileName);
            }
        }

        commit("Merged " + branchName + " into " + _head + ".");
    }

    /** A helper function. */
    private void conflictMerge() {

    }

    /** A helper function to check.
     *  whether the file has been modified
     *  @param fileName file name
     *  @param now current files
     *  @param old files to compare with
     *  @return boolean */
    private boolean modified(String fileName,
                     HashMap<String, String> now,
                     HashMap<String, String> old) {
        if (now.containsKey(fileName)
                && old.containsKey(fileName)) {
            String fileId1 = now.get(fileName);
            String fileId2 = old.get(fileName);
            if (!fileId1.equals(fileId2)) {
                return true;
            }
        }
        return false;
    }

    /** A helper function.
     * return the id of latest common ancestor commit
     * @param branch1 first branch name
     * @param branch2 second branch name */
    private String findAncestor(String branch1, String branch2) {
        if (branch1.equals(branch2)) {
            Utils.message("Cannot merge a branch with itself.");
            System.exit(0);
        }
        ArrayList<String> heads1 = new ArrayList<>();
        ArrayList<String> heads2 = new ArrayList<>();

        String parent1 = _latestCommitId.get(branch1);
        String parent2 = _latestCommitId.get(branch2);

        while (parent1 != null) {
            heads1.add(parent1);
            parent1 = getCommit(parent1).getParent();
        }
        while (parent2 != null) {
            heads2.add(parent2);
            parent2 = getCommit(parent2).getParent();
        }
        for (String commit : heads1) {
            if (heads2.contains(commit)) {
                return commit;
            }
        }
        return "";
    }

    /** A helper function to check whether it can merge.
     * @param branchName branch name */
    private void canMerge(String branchName) {
        if (_trackingFileIds.size() != 0 || _removedFiles.size() != 0) {
            Utils.message("You have uncommitted changes.");
            System.exit(0);
        }
        if (!_branches.containsKey(branchName)) {
            Utils.message("A branch with that name does not exist.");
            System.exit(0);
        }
        updateUntrack();
        if (_untrackedFiles.size() != 0) {
            Utils.message("There is an untracked file in the way;"
                    + " delete it or add it first.");
            System.exit(0);
        }
        String ancestor = findAncestor(branchName, _head);
        if (ancestor.equals(_latestCommitId.get(branchName))) {
            Utils.message("Given branch is an "
                    + "ancestor of the current branch.");
            System.exit(0);
        }
        if (ancestor.equals(_latestCommitId.get(_head))) {
            Utils.message("Current branch fast-forwarded.");
            System.exit(0);
        }
    }
    /** A helper function to return a commit.
     * @param id A commit id
     * @return a Commit object */
    private Commit getCommit(String id) {
        File tmp = new File(".gitlet/commits/" + id);
        if (tmp.exists()) {
            return Utils.readObject(tmp, Commit.class);
        } else {
            Utils.message("No commit with that id exists.");
            System.exit(0);
        }
        return null;
    }

    /** A helper function to return a file ID.
     * @param file A file name
     * @return a file ID */
    private String getFileID(File file) {

        return Utils.sha1(file.getName(),
                Utils.readContentsAsString(file));
    }

    /** A helper function that updates untrack files. */
    private void updateUntrack() {
        List<String> allFiles = Utils.plainFilenamesIn(
                System.getProperty("user.dir"));
        _untrackedFiles = new ArrayList<>();
        for (String fileName : allFiles) {
            File tmp = new File(fileName);
            String fileId = getFileID(tmp);
            File staging = new File(".gitlet/stage/" + fileId);
            if (!_trackingFileIds.keySet().contains(fileName)
                    && !staging.exists()
                    && !_removedFiles.contains(fileName)
                    && !fileName.equals(".DS_Store")) {
                _untrackedFiles.add(fileName);
            }
        }
    }

    /** A helper function returns full commit ID.
     *  @param shortId short id */
    private String fullCommitId(String shortId) {
        for (Commit tmp : _allCommits.values()) {
            if (tmp.getId().startsWith(shortId)) {
                return tmp.getId();
            }
        }
        Utils.message("No commit with that id exists.");
        System.exit(0);
        return null;
    }

    /** A helper function for remove.
     *  return a file from stage area given file name
     *  @param fileName file name */
    private File getFile(String fileName) {
        String head = _branches.get(_head).getHead();
        Commit comm = getCommit(head);
        HashMap<String, String> allFiles = comm.allFiles();
        if (allFiles != null
                && allFiles.keySet().contains(fileName)) {
            String fileId = allFiles.get(fileName);
            File oldVersion = new File(".gitlet/stage/" + fileId);
            return oldVersion;
        }
        return null;
    }
    /** A pointer points to the current branch. */
    private String _head;

    /** A list keeps the file names of all untracked files. */
    private ArrayList<String> _untrackedFiles;

    /** A list keeps the file names of all removed Files. */
    private ArrayList<String> _removedFiles;

    /** A list keeps all branch names created. */
    private HashMap<String, Branch> _branches;

    /** A hashmap saves current tracking files. */
    private HashMap<String, String> _trackingFileIds;

    /** A hashmap saves all Commits that have been made.
     * Key: Message
     * Value: Commit ID */
    private HashMap<String, Commit> _allCommits;

    /** A hashmap saves the most recent commit IDs in each branch.
     * Key: Branch Name
     * Value: Commit ID */
    private HashMap<String, String> _latestCommitId;
}
