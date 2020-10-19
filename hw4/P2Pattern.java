/** P2Pattern class
 *  @author Josh Hug & Vivant Sakore
 */

public class P2Pattern {
    /* Pattern to match a valid date of the form MM/DD/YYYY. Eg: 9/22/2019 */
    public static final String P1 = "([1-9]|0[1-9]|1[0-2])/([1-9]|[0-2][0-9]|3[0-1])/[1-9][0-9]{3}";

    /* Pattern to match 61b notation for literal IntLists. */
    public static final String P2 = "\\(([\\d]+, +)+[\\d]+\\)";

    /* Pattern to match a valid domain name. Eg: www.support.facebook-login.com */
    public static final String P3 = "([a-z]+\\.)+[^-. _][a-zA-Z\\d-]*[^-. _](\\.[a-z]+)*";

    /* Pattern to match a valid java variable name. Eg: _child13$ */
    public static final String P4 = "[^\\d]([\\da-zA-Z_$])*";

    /* Pattern to match a valid IPv4 address. Eg: 127.0.0.1 */
    public static final String P5 = "(([\\d]|[1-9][\\d]|0[\\d]|0[\\d]{2}|1[\\d]{2}|2[0-5]{2})\\.){3}([\\d]|[1-9][\\d]|0[\\d]|0[\\d]{2}|1[\\d]{2}|2[0-5]{2})";

}
