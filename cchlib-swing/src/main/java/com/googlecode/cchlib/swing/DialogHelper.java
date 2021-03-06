package com.googlecode.cchlib.swing;

import java.awt.Window;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.WindowConstants;
import com.googlecode.cchlib.lang.ExceptionHelper;
import com.googlecode.cchlib.resources.ResourcesLoader;
import com.googlecode.cchlib.resources.ResourcesLoaderException;

public final class DialogHelper
{
    private DialogHelper()
    {
        // All static
    }

    /**
     * Open a message exception dialog, and wait for user input
     *
     * @param title         the title of the Dialog.
     * @param exception     the exception to display stack trace
     */
    public static void showMessageExceptionDialog(
            final String    title,
            final Throwable exception
        )
    {
        showMessageExceptionDialog(
            null,
            title,
            exception
            );
    }

    /**
     * Open a message exception dialog, and wait for user input
     *
     * @param parentWindow  the Window from which the dialog is displayed
     *                      or null if this dialog has no owner.
     * @param title         the title of the Dialog.
     * @param exception     the exception to display stack trace
     */
    public static void showMessageExceptionDialog(
        final Window    parentWindow,
        final String    title,
        final Throwable exception
        )
    {
        final AbstractButton[] buttons = {
                buildJButtonIconOrText(
                        ResourcesLoader.OK_ICON_16x16,
                        "OK"
                        )
                };

        showMessageExceptionDialog(
                parentWindow,
                title,
                exception,
                buttons
                );
    }

    /**
     * Open a message exception dialog, and wait for user input
     *
     * @param parentWindow
     *            the Window from which the dialog is displayed or null
     *            if this dialog has no owner.
     * @param title
     *            the title of the Dialog.
     * @param exception
     *            the exception to display stack trace
     * @param buttonsText
     *            String by references of buttons text.
     * @return index of button select by user.
     */
    public static int showMessageExceptionDialog(
        final Window    parentWindow,
        final String    title,
        final Throwable exception,
        final String... buttonsText
        )
    {
        final AbstractButton[] buttons = new AbstractButton[ buttonsText.length ];

        for( int i=0; i<buttons.length; i++ ) {
            buttons[ i ] = new JButton( buttonsText[ i ] );
            }

        return showMessageExceptionDialog( parentWindow, title, exception, buttons );
    }

    /**
     * Open a message exception dialog, and wait for user input
     *
     * @param parentWindow
     *            the Window from which the dialog is displayed or null
     *            if this dialog has no owner.
     * @param title
     *            the title of the Dialog.
     * @param exception
     *            the exception to display stack trace
     * @param buttons
     *            Buttons array for user input
     * @return index of button select by user.
     */
    public static int showMessageExceptionDialog(
        final Window            parentWindow,
        final String            title,
        final Throwable         exception,
        final AbstractButton[]  buttons
        )
    {
        final String         message = createHTMLMessage( exception );
        final CustomDialogWB dialog  = new CustomDialogWB(
                parentWindow,
                buttons
                );

        if( title != null ) {
            dialog.setTitle( title );
            }

        dialog.setMessage( message );
        dialog.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
        dialog.setModal( true );
        dialog.setVisible( true );

        return dialog.getSelectedButtonIndex();
    }

    private static String createHTMLMessage( final Throwable exception )
    {
        final StringBuilder msg = new StringBuilder();

        msg.append( "<html><b>" );
        msg.append( exception.getLocalizedMessage() );
        msg.append( "</b><br/>\n" );

        for( final String line : ExceptionHelper.getStackTraceLines( exception ) ) {
            msg.append( "<pre>" );
            msg.append( line );
            msg.append( "</pre>\n" );
            }

        msg.append( "</html>" );

        return msg.toString();
    }

    private static JButton buildJButtonIconOrText(
        final String iconResourceName,
        final String textIfError
        )
    {
        final JButton button = new JButton();

        setAbstractButtonIconOrText( button, iconResourceName, textIfError );

        return button;
    }

    private static void setAbstractButtonIconOrText(
        final AbstractButton button,
        final String         iconResourceName,
        final String         textIfError
        )
    {
        try {
            button.setIcon(
                ResourcesLoader.getImageIcon( iconResourceName )
                );
            }
        catch( final ResourcesLoaderException cause ) {
            button.setText( textIfError );

            SafeSwingUtilities.printStackTrace( cause );
            }
    }
}
