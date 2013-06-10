package com.googlecode.cchlib.apps.duplicatefiles;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Window;
import java.io.File;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import javax.swing.JFileChooser;
import com.googlecode.cchlib.apps.duplicatefiles.prefs.Preferences;
import com.googlecode.cchlib.i18n.AutoI18nConfig;
import com.googlecode.cchlib.i18n.resources.I18nResourceBundleName;
import com.googlecode.cchlib.swing.filechooser.JFileChooserInitializer;

/** 
 * Fake class for tests
 */
public class FakeDFToolKit implements DFToolKit
{
    private static final long serialVersionUID = 1L;
    private JFileChooserInitializer jFileChooserInitializer;
    private DefaultDFToolKit delegator;

    public FakeDFToolKit()
    {
        delegator = new DefaultDFToolKit(null);
    }

    @Override
    public void initJFileChooser()
    {
        Window win = getMainFrame();
        getJFileChooserInitializer( win , win );
    }

    @Override
    public JFileChooserInitializer getJFileChooserInitializer( 
        final Window parentWindow, 
        final Component refComponent
        )
    {
        if( jFileChooserInitializer == null ) {
            jFileChooserInitializer = new JFileChooserInitializer();
            }
        return jFileChooserInitializer;
    }

    @Override
    public JFileChooser getJFileChooser(         
        final Window parentWindow, 
        final Component refComponent
        )
    {
        return getJFileChooserInitializer( parentWindow, refComponent ).getJFileChooser();
    }

    @Override
    public void beep()
    {
        delegator.beep();
    }

    @Override
    public void openDesktop( File file )
    {
        // fake
        throw new UnsupportedOperationException();
    }

    @Override
    public Locale getValidLocale()
    {
        // fake
        return Locale.ENGLISH;
    }

    @Override
    public void sleep( long ms )
    {
        delegator.sleep( ms );
    }

    @Override
    public Preferences getPreferences()
    {
        // fake (no pref here)
        return null;
    }

    @Override
    public Frame getMainFrame()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setEnabledJButtonCancel( boolean b )
    {
        // fake
    }

    @Override
    public boolean isEnabledJButtonCancel()
    {
        // fake
        return false;
    }

    @Override
    public void initComponentsJPanelConfirm()
    {
        // fake
    }

    @Override
    public Resources getResources()
    {
        return delegator.getResources();
    }

    @Override
    public List<File> getRootDirectoriesList()
    {
        return Collections.emptyList();
    }

//    @Override
//    public Package getPackageMessageBundleBase()
//    {
//        return delegator.getPackageMessageBundleBase();
//    }
//
//    @Override
//    public String getMessageBundleBaseName()
//    {
//        return delegator.getMessageBundleBaseName();
//    }

    @Override
    public I18nResourceBundleName getI18nResourceBundleName()
    {
        return delegator.getI18nResourceBundleName();
    }

    @Override
    public EnumSet<AutoI18nConfig> getAutoI18nConfig()
    {
        return delegator.getAutoI18nConfig();
    }
}
