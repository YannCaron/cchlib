package com.googlecode.cchlib.i18n.unit.strings.errors;

import javax.swing.JButton;
import org.junit.Assert;
import com.googlecode.cchlib.i18n.annotation.I18nCustomMethod;
import com.googlecode.cchlib.i18n.annotation.I18nString;
import com.googlecode.cchlib.i18n.core.AutoI18nCore;
import com.googlecode.cchlib.i18n.core.I18nAutoCoreUpdatable;

public class I18nStringWithErrorsTestContener implements I18nAutoCoreUpdatable
{
    @I18nString private Object thisNotAString1;
    @I18nString private JButton thisNotAString2;
    @I18nString(id="I18nStringWithErrorsTestContener_GlobalStringID2",method="customizeString1") private String myGlobalStringIDMethod1 = I18nStringWithErrorsTestReference.INIT_myGlobalStringIDMethod1;

    public I18nStringWithErrorsTestContener()
    {
        // Nothing !
    }

    @I18nCustomMethod
    public void customizeString1()
    {
        // Should be something like :
        myGlobalStringIDMethod1 = I18nStringWithErrorsTestReference.DEFAULT_BUNDLE_myGlobalStringIDMethod1;

        // But there is a syntax exception, and should not be called
        Assert.fail();
    }

    @Override // I18nAutoCoreUpdatable
    public void performeI18n( final AutoI18nCore autoI18n )
    {
        autoI18n.performeI18n( this, this.getClass() );
    }

    final String getMyGlobalStringIDMethod1()
    {
        return myGlobalStringIDMethod1;
    }

    final void setMyGlobalStringIDMethod1( final String myGlobalStringIDMethod1 )
    {
        this.myGlobalStringIDMethod1 = myGlobalStringIDMethod1;
    }
}