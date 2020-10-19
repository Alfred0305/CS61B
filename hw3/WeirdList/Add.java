class Add implements IntUnaryFunction {
    /**
     * Return the result of applying this function to X.
     *
     * @param
     */


    public Add(int n){
        addOn = n;
    }

    @Override
    public int apply(int x) {
        return x + addOn;
    }

    private int addOn;
}
