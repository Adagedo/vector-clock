package code.adagedo.clock;

public class Constraints {

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     *
     * @param expression
     *          a boolean expression
     * @throws IllegalArgumentException
     *           if {@code expression} is false
     */
    public static void  validateArgs(final boolean expression){
        if(!expression) throw new IllegalArgumentException();
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param ref
     *          an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException
     *           if {@code reference} is null
     */
    public static <T> T checkNotNull(final T ref){
        if(ref == null)throw new NullPointerException();
        return ref;
    }

    /**
     * private constructor
     */
    private Constraints(){}

}
