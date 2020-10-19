public class Sum implements IntUnaryFunction{
    /**
     * Return the result of applying this function to X.
     *
     * @param
     */

    public Sum(){
        result = 0;
    }

    @Override
    public int apply(int x) {
        result += x;
        return x;
    }

    public int result(){
        return result;
    }

    private int result;
}
