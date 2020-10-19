import java.io.WriteAbortedException;

public class EMPTY extends WeirdList{

    public EMPTY(){
        super(0,null);
        WeirdList l = new WeirdList(0, null);

    }

    public int length(){
        return 0;
    }

    public String toString() {
        return "";
    }

    public WeirdList map(IntUnaryFunction func) {
        return new EMPTY();
    }
}
