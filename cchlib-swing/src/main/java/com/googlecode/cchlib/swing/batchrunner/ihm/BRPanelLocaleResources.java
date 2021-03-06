package com.googlecode.cchlib.swing.batchrunner.ihm;

import java.io.Serializable;
import com.googlecode.cchlib.swing.filechooser.WaitingJFileChooserInitializer;


/**
 * Text for localization of {@link BRPanel}
 *
 * @since 1.4.8
 * @see DefaultBRLocaleResources
 * @see DefaultBRLocaleResourcesBuilder
 */
public interface BRPanelLocaleResources extends Serializable
{
    /**
     * @return localized text for Add source files button
     */
    String getTextAddSourceFile();
    /**
     * @return localized text for set destination folder button
     */
    String getTextSetDestinationFolder();
    /**
     * @return localized text for clear source files button
     */
    String getTextClearSourceFileList();
    /**
     * @return localized text for start batch button
     */
    String getTextDoAction();
    /**
     * @return localized text for dialog title of {@link WaitingJFileChooserInitializer}.
     */
    String getTextJFileChooserInitializerTitle();
    /**
     * @return localized text for dialog text of {@link WaitingJFileChooserInitializer}.
     */
    String getTextJFileChooserInitializerMessage();
    /**
     * @return localized text for message when try to start batch,
     *         but there is no source file define
     */
    String getTextNoSourceFile();
    /**
     * @return localized text for message when try to start batch,
     *         but there is no destination folder set
     */
    String getTextNoDestinationFolder();

    /**
     * @return localized text for message when batch is running
     */
    String getTextWorkingOn_FMT();

    /**
     * @return localized text for Exception dialog when an unexpected
     *         exception occur.
     */
    String getTextUnexpectedExceptionTitle();


    String getTextExitRequestTitle();
    String getTextExitRequestMessage();
    String getTextExitRequestYes();
    String getTextExitRequestNo();
}
