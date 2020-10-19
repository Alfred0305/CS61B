package gitlet;
import java.io.File;

/** Main method.
 * @author Alfred Wang */
public class Main {
    /** Usage: java gitlet.Main ARGS, where ARGS contains.
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        if (args.length == 0) {
            Utils.message("Please enter a command.");
            System.exit(0);
        }
        String command = args[0];
        if (validCommand(command)) {
            String[] operands = new String[args.length - 1];
            System.arraycopy(args, 1, operands, 0, operands.length);
            if (initialized()) {
                myCommand = getMyCommand();
                operate(command, operands);
                File hidden = new File(".gitlet/.hidden");
                Utils.writeObject(hidden, myCommand);
            } else {
                if (command.equals("init")) {
                    myCommand = new Command();
                    File hidden = new File(".gitlet/.hidden");
                    Utils.writeObject(hidden, myCommand);
                } else {
                    Utils.message("Not in an initialized Gitlet directory.");
                    System.exit(0);
                }
            }
        } else {
            Utils.message("No command with that name exists.");
            System.exit(0);
        }
    }


    /** Do operation based on command and operands.
     *  @param command assume valid command
     *  @param operands do nothing if invalid */
    private static void operate(String command, String[] operands) {
        if (command.equals("add")) {
            if (operands.length != 1) {
                Utils.message("Incorrect operands.");
                System.exit(0);
            }
            myCommand.add(operands[0]);
        } else if (command.equals("commit")) {
            if (operands.length != 1) {
                Utils.message("Incorrect operands.");
                System.exit(0);
            }
            myCommand.commit(operands[0]);
        } else if (command.equals("init")) {
            Utils.message("A Gitlet version-control system "
                    + "already exists in the current directory.");
            System.exit(0);
        } else if (command.equals("rm")) {
            myCommand.rm(operands);
        } else if (command.equals("log")) {
            if (operands.length != 0) {
                Utils.message("Incorrect operands.");
                System.exit(0);
            }
            myCommand.log();
        } else if (command.equals("global-log")) {
            if (operands.length != 0) {
                Utils.message("Incorrect operands.");
                System.exit(0);
            }
            myCommand.globalLog();
        } else if (command.equals("checkout")) {
            myCommand.checkout(operands);
        } else if (command.equals("find")) {
            if (operands.length != 1) {
                Utils.message("Incorrect operands.");
                System.exit(0);
            }
            myCommand.find(operands[0]);
        } else if (command.equals("status")) {
            if (operands.length != 0) {
                Utils.message("Incorrect operands.");
                System.exit(0);
            }
            myCommand.status();
        } else if (command.equals("branch")) {
            myCommand.branch(operands);
        } else if (command.equals("rm-branch")) {
            myCommand.rmBranch(operands);
        } else if (command.equals("reset")) {
            myCommand.reset(operands);
        } else if (command.equals("merge")) {
            if (operands.length != 1) {
                Utils.message("Incorrect operands.");
                System.exit(0);
            }
            myCommand.merge(operands[0]);
        }
    }
    /** A helper function return command. */
    private static Command getMyCommand() {
        File hidden = new File(".gitlet/.hidden");
        return Utils.readObject(hidden, Command.class);
    }
    /** Check if Str is a valid command.
     *  @param str first String in args
     *  @return boolean */
    private static boolean validCommand(String str) {
        for (String cmd : CMDS) {
            if (str.equals(cmd)) {
                return true;
            }
        }
        return false;
    }

    /** Check if current directory has been initialized.
     *  @return boolean */
    public static boolean initialized() {
        String dir = System.getProperty("user.dir");
        File tmpDir = new File(dir + "/.gitlet");
        if (tmpDir.exists()) {
            return true;
        }
        return false;
    }

    /** An array of strings containing all valid commands. */
    private static final String[] CMDS
            = new String[] {"init", "add", "commit",
                "rm", "log", "global-log",
                "find", "status", "checkout",
                "branch", "rm-branch", "reset",
                "merge"};

    /**private field to store my command. */
    private static Command myCommand;
}
