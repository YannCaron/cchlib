package cx.ath.choisnet.swing.introspection;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Set;
import javax.annotation.Nonnull;
import com.googlecode.cchlib.lang.reflect.AccessibleRestorer;
import com.googlecode.cchlib.lang.reflect.SerializableField;

/**
 * This object is created by {@link SwingIntrospector}
 *
 * @param <FRAME> NEEDDOC
 */
@SuppressWarnings("squid:S00119") // Type one char only ! Why ?
public class SwingIntrospectorItem<FRAME>
    implements Serializable
{
    private static final long serialVersionUID = 2L;

    /** @serial */
    private final SerializableField sField;
    /** @serial */
    private int index;
    /** @serial */
    private final boolean isRoot;

    private final boolean forceFieldAccess;

    /**
     * Create a SwingIntrospectorItem
     *
     * @param bean NEEDDOC
     * @param field NEEDDOC
     * @param attributes NEEDDOC
     */
    public SwingIntrospectorItem(
        @Nonnull final Bean                          bean,
        @Nonnull final Field                         field,
        @Nonnull final Set<SwingIntrospector.Attrib> attributes
        )
    {
        this.sField           = new SerializableField( field );
        this.forceFieldAccess = ! attributes.contains( SwingIntrospector.Attrib.ONLY_ACCESSIBLE_PUBLIC_FIELDS );

        if( bean.isIndexed() ) {
            this.index = bean.getIndex();
        }
        else {
            this.index = -1;
        }

        this.isRoot = bean.isRoot();
    }

    /**
     ** Note: to get real object, use getFieldObject(Object)
     * @return Field object
     ** @see #getFieldObject(Object)
     */
    public Field getField()
    {
        return this.sField.getField();
    }

    /**
     * @return the index
     */
    public int getIndex()
    {
        return this.index;
    }

    /**
     * @return the isRoot
     */
    public boolean isRoot()
    {
        return this.isRoot;
    }

    /**
     * NEEDDOC
     *
     * @param objectToInspect
     *            NEEDDOC
     * @return Object to populate corresponding to current Field
     * @throws SwingIntrospectorIllegalAccessException
     *             if any
     */
    public Object getFieldObject( final FRAME objectToInspect )
        throws SwingIntrospectorIllegalAccessException
    {
        final AccessibleRestorer accessible;

        if( this.forceFieldAccess ) {
            accessible = new AccessibleRestorer( getField() );
        } else {
            accessible = null;
        }

        try {
            return getField().get( objectToInspect );
        }
        catch( final IllegalArgumentException | IllegalAccessException e ) {
            throw new SwingIntrospectorIllegalAccessException( e );
        }
        finally {
            if( accessible != null ) {
                accessible.restore();
            }
        }
    }

    @Override
    public String toString()
    {
        final StringBuilder builder = new StringBuilder();

        builder.append( "SwingIntrospectorItem [field=" );
        builder.append( this.sField.getField().getName() );
        builder.append( ", index=" );
        builder.append( this.index );
        builder.append( ", isRoot=" );
        builder.append( this.isRoot );
        builder.append( ']' );

        return builder.toString();
    }
}

