package com.googlecode.cchlib.util.properties;


//NOT public
class BeanAnnotationOnFieldsHeadLess implements BeanAnnotationHeadLess
{
    @Populator protected String    aString;
    @Populator private   int       aInt;
    @Populator private   float     aFloat;
    @Populator public    boolean[] someBooleans;

    public BeanAnnotationOnFieldsHeadLess()
    {
        // Empty
    }

    public BeanAnnotationOnFieldsHeadLess(
        final String    aString,
        final int       aInt,
        final float     aFloat,
        final boolean[] booleans
        )
    {
        this();

        this.aString        = aString;
        this.aInt           = aInt;
        this.aFloat         = aFloat;
        this.someBooleans   = booleans;
    }

    @Override
    public String toString()
    {
        return BeanAnnotationHeadLess.toString( this );
    }

    @Override
    public final String getaString()
    {
        return this.aString;
    }
    @Override
    public final void setaString(final String aString)
    {
        this.aString = aString;
    }

    @Override
    public final int getaInt()
    {
        return this.aInt;
    }
    @Override
    public final void setaInt(final int aInt)
    {
        this.aInt = aInt;
    }

    @Override
    public final float getaFloat()
    {
        return this.aFloat;
    }
    @Override
    public final void setaFloat(final float aFloat)
    {
        this.aFloat = aFloat;
    }

    @Override
    public final boolean[] getSomeBooleans()
    {
        return this.someBooleans;
    }
    @Override
    public final void setSomeBooleans(final boolean[] someBooleans)
    {
        this.someBooleans = someBooleans;
    }
}