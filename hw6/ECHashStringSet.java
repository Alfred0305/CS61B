import java.util.LinkedList;
import java.util.List;

/** A set of String values.
 *  @author Alfred Wang
 */
class ECHashStringSet implements StringSet {

    public ECHashStringSet() {
        max = 5;
        min = 0.2;
        slot = new LinkedList[5];
        for (int i = 0; i < slot.length; i ++){
            slot[i] = new LinkedList<String>();
        }
    }

    @Override
    public void put(String s) {
        int i = hash(s.hashCode());
        slot[i].add(s);

        if (slot[i].size() > max) {
            resize();
        }
    }

    private void resize() {
        LinkedList<String>[] pre = slot;
        slot = new LinkedList[2 * slot.length];
        for (int i = 0; i < slot.length; i ++){
            slot[i] = new LinkedList<>();
        }
        for (LinkedList<String> b : pre) {
            for (String s : b){
                this.put(s);
            }
        }
    }


    @Override
    public boolean contains(String s) {
        int p = hash(s.hashCode());
        return slot[p].contains(s);
    }

    private int hash(int a) {
        int h = a & 0x7FFFFFFF;
        int r = h % slot.length;
        return r;
    }

    @Override
    public List<String> asList() {
        List<String> l = new LinkedList<>();
        for (LinkedList<String> b : slot) {
            for (String s : b) {
                l.add(s);
            }
        }
        return l;
    }

    private int max;
    private double min;
    private LinkedList<String>[] slot;
}
