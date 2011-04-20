/*
**
*/
package alpha.cx.ath.choisnet.utils;

/**
 * TODO: doc!
 * @author Claude CHOISNET
 * @param <T> 
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