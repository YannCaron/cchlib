package com.googlecode.cchlib.i18n.unit.parts;

import javax.swing.JButton;
import org.apache.log4j.Logger;
import org.junit.Assert;
import com.googlecode.cchlib.i18n.annotation.I18nToolTipText;
import com.googlecode.cchlib.i18n.core.AutoI18nCore;
import com.googlecode.cchlib.i18n.core.I18nAutoCoreUpdatable;
import com.googlecode.cchlib.i18n.unit.PrepTestPart;
import com.googlecode.cchlib.i18n.unit.TestReferenceDeprecated;
import com.googlecode.cchlib.i18n.unit.util.TestUtils;

public class I18nToolTipTextPart implements I18nAutoCoreUpdatable, TestReferenceDeprecated
{
    private static final Logger LOGGER = Logger.getLogger( I18nToolTipTextPart.class );
    private static final String TOOLTIPTEXT_INIT = "my tool tip text 1";
    private static final String TOOLTIPTEXT_DEFAULT_BUNDLE = "OK(ToolTipText)";

    private static final String TEXT_INIT = "my button with tool tip text 1";
    private static final String TEXT_DEFAULT_BUNDLE = "OK(myButtonWithToolTipText1)";

    @I18nToolTipText private final JButton myButtonWithToolTipText1;

    public I18nToolTipTextPart()
    {
        this.myButtonWithToolTipText1 = new JButton( TEXT_INIT );
        this.myButtonWithToolTipText1.setToolTipText( TOOLTIPTEXT_INIT );
    }

    @Override // I18nAutoCoreUpdatable
    public void performeI18n( final AutoI18nCore autoI18n )
    {
        autoI18n.performeI18n( this, this.getClass() );
    }

    @Override
    @Deprecated
    public void beforePrepTest(final PrepTestPart prepTest)
    {
        TestUtils.preparePrepTest( prepTest, this );
    }

    @Override
    public void afterPrepTest()
    {
        Assert.assertEquals( TEXT_INIT, this.myButtonWithToolTipText1.getText() );
        Assert.assertEquals( TOOLTIPTEXT_INIT, this.myButtonWithToolTipText1.getToolTipText() );
    }

    @Override
    @Deprecated
    public void performeI18n()
    {
        Assert.assertEquals( TEXT_INIT, this.myButtonWithToolTipText1.getText() );
        Assert.assertEquals( TOOLTIPTEXT_INIT, this.myButtonWithToolTipText1.getToolTipText() );

        TestUtils.performeI18n( this );

        final String toolTipText = this.myButtonWithToolTipText1.getToolTipText();

        LOGGER.info( "TEST RESULT: getToolTipText() " + toolTipText );
        Assert.assertEquals( TOOLTIPTEXT_DEFAULT_BUNDLE, toolTipText );

        final String text = this.myButtonWithToolTipText1.getText();

        LOGGER.info( "TEST RESULT: getText() " + text );
        Assert.assertEquals( TEXT_DEFAULT_BUNDLE, text );
    }

    @Override
    public int getSyntaxeExceptionCount()
    {
        return 0;
    }

    @Override
    public int getMissingResourceExceptionCount()
    {
        return 2;
    }
}
