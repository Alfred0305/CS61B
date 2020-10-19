package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Alfred Wang
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine machine = readConfig();
        String temp = _input.nextLine();
        if (temp.charAt(0) != '*') {
            throw error("wrong heading to set machine");
        }
        String settings = temp.substring(2);
        setUp(machine, settings);
        while (_input.hasNextLine()) {
            machine.clean();
            if (temp.startsWith("*")) {
                settings = temp.substring(2);
                setUp(machine, settings);
            }
            temp = _input.nextLine();
            while (!temp.startsWith("*")) {
                String before = temp.replace(" ", "");
                String result = machine.convert(before);
                printMessageLine(result);
                if (!_input.hasNext()) {
                    break;
                }
                temp = _input.nextLine();
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String a = _config.nextLine();
            _alphabet = new Alphabet(a);
            _numRotors = _config.nextInt();
            _pawls = _config.nextInt();
            _allrotors = new ArrayList<>();
            while (_config.hasNext()) {
                _allrotors.add(readRotor());
            }
            return new Machine(_alphabet, _numRotors, _pawls, _allrotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name;
            if (_rotorName == null) {
                name = _config.next();
            } else {
                name = _rotorName;
                _rotorName = null;
            }

            String typeandNotch = _config.next();
            String cycles = _config.next();
            String temp = null;
            if (_config.hasNext()) {
                temp = _config.next();
            }
            while (temp != null && temp.startsWith("(")) {
                cycles += temp;
                if (_config.hasNext()) {
                    temp = _config.next();
                } else {
                    break;
                }
            }
            _rotorName = temp;
            if (typeandNotch.charAt(0) == 'M') {
                _allMoving.add(name);
                return new MovingRotor(name,
                        new Permutation(cycles, _alphabet),
                        typeandNotch.substring(1));
            } else if (typeandNotch.charAt(0) == 'N') {
                return new FixedRotor(name, new Permutation(cycles, _alphabet));
            } else {
                return new Reflector(name, new Permutation(cycles, _alphabet));
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String[] rotors = new String[1];
        int numMoving = 0;
        String[] sets = settings.split(" ");
        for (int i = 0; i < sets.length; i++) {
            int rotorSize = rotors.length - 1;
            for (Rotor r : _allrotors) {
                if (r.name().toUpperCase().equals(sets[i].toUpperCase())) {
                    rotors[i] = r.name();
                    String[] temp = rotors;
                    if (_allMoving.contains(r.name())) {
                        numMoving++;
                    }
                    rotors = new String[rotors.length + 1];
                    for (int j = 0; j < temp.length; j++) {
                        rotors[j] = temp[j];
                    }
                    break;
                }
            }
            if (rotorSize == rotors.length - 1) {
                if (rotors.length - 1 != _numRotors) {
                    throw error("illegal number of rotors");
                }
                if (numMoving != _pawls) {
                    throw error("Wrong number of moving rotors: " + numMoving);
                }
                String[] temp = rotors;
                rotors = new String[rotors.length - 1];
                for (int j = 0; j < temp.length - 1; j++) {
                    rotors[j] = temp[j];
                }
                for (int a = 0; a < rotors.length; a++) {
                    for (int b = 0; b < rotors.length; b++) {
                        if (a != b && rotors[a] == rotors[b]) {
                            throw error("repeated rotor name detected");
                        }
                    }
                }
                M.insertRotors(rotors);
                M.setRotors(sets[i]);
                String cycles = "";
                if (i + 1 < sets.length
                        && !sets[i + 1].startsWith("(")) {
                    M.setRings(sets[i + 1]);
                    i += 1;
                }
                for (int j = i + 1; j < sets.length; j++) {
                    cycles += sets[j];
                }
                M.setPlugboard(new Permutation(cycles, _alphabet));
                break;
            }
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        String result = "";
        for (int i = 0; i < msg.length(); i++) {
            if (i % 5 == 0 && i != 0) {
                result += " ";
            }
            result += (msg.charAt(i));
        }
        _output.println(result);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** Number of rotors.*/
    private int _numRotors;

    /** Number of MovingRotors.*/
    private int _pawls;

    /** Collection of all Rotors.*/
    private ArrayList<Rotor> _allrotors;

    /** A helper field to save rotor name.*/
    private String _rotorName;

    /** A list of names of all moving rotors.*/
    private ArrayList<String> _allMoving = new ArrayList<>();
}
