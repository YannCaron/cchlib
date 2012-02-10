package paper.reflexion;

/**
 *
 */
public abstract class ParentClass
{
    public final static String aPublicFinalStaticField = "**aPublicFinalStaticField**";
    protected final String aProtectedFinalField = "**aProtectedFinalField**";
    protected String aProtectedField = "**aProtectedField**";
    private Long aLong;
    private String aString;
    private int aInt;

    /**
     *
     */
    public ParentClass()
    {
        this.aLong      = 10L;
        this.aString    = "FOO";
        this.aInt       = 20;

        doNothing(); // Just to hide warning !
        try {
            doNullPointerException();
            }
        catch( NullPointerException ignore ) {}
    }

    private void doNothing()
    {
        // Well, nothing !
    }

    private void doNullPointerException()
    {
        throw new NullPointerException( "Fake exception" );
    }

    public String doFoo( final String value )
    {
        final String prev = this.getAString();
        this.setAString( value );
        return prev;
    }

    private String getAString()
    {
        return this.aString;
    }

    private void setAString( final String value )
    {
        this.aString = value;
    }

    protected abstract int aProtectedAbstractMethod();

    protected int aProtectedMethod()
    {
        return - this.aInt;
    }

    public void aPublicMethod(
        final int       intValue,
        final Long      longObject,
        final String    stringObject
        )
    {
        this.aInt       = intValue;
        this.aLong      = longObject;
        this.aString    = stringObject;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("ParentClass [aProtectedFinalField=");
        builder.append(aProtectedFinalField);
        builder.append(", aProtectedField=");
        builder.append(aProtectedField);
        builder.append(", aLong=");
        builder.append(aLong);
        builder.append(", aString=");
        builder.append(aString);
        builder.append(", aInt=");
        builder.append(aInt);
        builder.append("]");
        return builder.toString();
    }

}
