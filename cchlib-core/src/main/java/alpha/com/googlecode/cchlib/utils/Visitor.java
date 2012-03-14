package alpha.com.googlecode.cchlib.utils;

/**
 * TODOC
 */
public interface Visitor<T>
{
    /**
     * Invoked for each entry in the contender.
     *
     * @param entry current entry
     * @return the visit result
     */
    public VisitResult visite(T entry);
}