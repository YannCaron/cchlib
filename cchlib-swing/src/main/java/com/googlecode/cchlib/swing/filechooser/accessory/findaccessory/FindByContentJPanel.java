package com.googlecode.cchlib.swing.filechooser.accessory.findaccessory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import org.apache.log4j.Logger;

//TODO: Optimizations

/**
 * Implements user interface and generates FindFilter for selecting
 * files by content.
 * <P>
 * <b>WARNING:</B> The FindFilter inner class for this object does
 * not implement an efficient string search algorithm. Efficiency was
 * traded for code simplicity.
 */
class FindByContentJPanel extends JPanel implements FindFilterFactory
{
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger( FindByContentJPanel.class );

    /**
     * Find for the first occurrence of the text in this field.
     */
    protected JTextField    contentField = null;
    protected JCheckBox     ignoreCaseCheck = null;


    /**
        Constructs a user interface and a FindFilterFactory for searching
        files containing specified text.
    */
    FindByContentJPanel()
    {
        super();

        setLayout(new BorderLayout());

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));

        // Name
        JLabel l = new JLabel("File contains...",SwingConstants.LEFT);
        l.setForeground(Color.black);
        l.setFont(new Font("Helvetica",Font.PLAIN,10));
        p.add(l);

        contentField = new JTextField();
        contentField.setForeground(Color.black);
        contentField.setFont(new Font("Helvetica",Font.PLAIN,10));
        p.add(contentField);

        // ignore case
        ignoreCaseCheck = new JCheckBox("ignore case",true);
        ignoreCaseCheck.setForeground(Color.black);
        ignoreCaseCheck.setFont(new Font("Helvetica",Font.PLAIN,9));
        p.add(ignoreCaseCheck);

        add(p,BorderLayout.NORTH);
    }

    @Override
    public FindFilter createFindFilter ()
    {
        return new ContentFilter(contentField.getText(),
                                    ignoreCaseCheck.isSelected());
    }

    /**
        Implements a simple content filter.
    */
    static class ContentFilter implements FindFilter
    {
        protected String    content = null;
        protected boolean   ignoreCase = true;

        ContentFilter (String s, boolean ignore)
        {
            content = s;
            ignoreCase = ignore;
        }

        @Override
        public boolean accept (File f, FindProgressCallback callback)
        {
            if (f == null) {
                return false;
                }
            if (f.isDirectory()) {
                return false;
                }
            if ((content == null) || (content.length() == 0)) {
                return true;
                }

            boolean             result  = false;
            BufferedInputStream in      = null;
            LocatorStream       locator = null;

            try {
                long fileLength = f.length();
                in = new BufferedInputStream(new FileInputStream(f));
                byte[] contentBytes = null;
                if( ignoreCase ) {
                    contentBytes = content.toLowerCase().getBytes();
                    }
                else {
                    contentBytes = content.getBytes();
                    }

                locator = new LocatorStream(contentBytes);
                long counter = 0;
                int callbackCounter = 20; // Only call back every 20 bytes
                int c = -1;

                while((c = in.read()) != -1) {
                    counter++;
                    int matchChar = c;
                    if (ignoreCase) {
                        matchChar = (int)Character.toLowerCase((char)c);
                        }
                    locator.write(matchChar);

                    // This search could be time consuming, especially since
                    // this algorithm is not exactly the most efficient.
                    // Report progress to search monitor and abort
                    // if method returns false.
                    if (callback != null) {
                        if (--callbackCounter <= 0) {
                            if (!callback.reportProgress(this, f,counter,fileLength)) {
                                return false;
                                }
                            callbackCounter = 20;
                            }
                        }
                    }
                }
            catch( LocatedException e ) { // $codepro.audit.disable logExceptions
                result = true;
                }
            catch (Throwable e) {
                logger.warn( "ContentFilter error", e );
                }
            finally {
                try { if (locator != null) { locator.close(); } } catch (IOException e){} // $codepro.audit.disable emptyCatchClause, logExceptions
                try { if (in != null) { in.close(); } } catch (IOException e){} // $codepro.audit.disable emptyCatchClause, logExceptions
                }

            return result;
        }

        /**
            Thrown when a LocatorStream object finds a byte array.
        */
        class LocatedException extends IOException
        {
            private static final long serialVersionUID = 1L;

            public LocatedException(String msg)
            {
                super(msg);
            }

            public LocatedException(long location)
            {
                super(String.valueOf(location));
            }
        }

        /**
         * Locate an array of bytes on the output stream. Throws
         * a LocatedStream exception for every occurrence of the byte
         * array.
         */
        class LocatorStream extends OutputStream
        {
            protected byte[]                locate = null;
            protected Vector<MatchStream>   matchMakers = new Vector<MatchStream>();
            protected long                  mark = 0;

            LocatorStream (byte[] b)
            {
                locate = b;
            }

            @Override
            public void write (int b) throws IOException
            {
                if( locate == null ) {
                    throw new IOException("NULL locator array");
                    }
                if( locate.length == 0 ) {
                    throw new IOException("Empty locator array");
                    }

                long foundAt = -1;

                for( int i=matchMakers.size()-1; i>=0; i-- ) {
                    @SuppressWarnings("resource")
                    MatchStream m = matchMakers.elementAt(i);

                    try {
                        m.write(b);
                        }
                    catch (MatchMadeException e) { // $codepro.audit.disable logExceptions
                        foundAt = m.getMark();
                        matchMakers.removeElementAt(i);
                        }
                    catch (IOException e) { // $codepro.audit.disable logExceptions
                        // Match not made. Remove current matchMaker stream.
                        matchMakers.removeElementAt(i);
                        }
                    }

                if( b == locate[0] ) {
                    @SuppressWarnings("resource")
                    MatchStream m = new MatchStream(locate,mark);
                    m.write(b); // This will be accepted
                    matchMakers.addElement(m);
                    }
                mark++;

                if (foundAt >= 0) {
                    throw new LocatedException(foundAt);
                    }
            }

            /**
                Thrown when the bytes written match the byte pattern.
            */
            class MatchMadeException extends IOException
            {
                private static final long serialVersionUID = 1L;

                public MatchMadeException (String msg)
                {
                    super(msg);
                }

                public MatchMadeException (long mark)
                {
                    super(String.valueOf(mark));
                }
            }

            /**
             * Accept "output" as long as it matches a specified array of bytes.
             * Throw a MatchMadeException when the bytes written equals the
             * match array.
             * Throw an IOException when a byte does not match. Ignore
             * everything after a match is made.
             */
            class MatchStream extends OutputStream
            {
                protected long      mark = -1;
                protected int       pos = 0;
                protected byte[]    match = null;
                protected boolean   matchMade = false;

                MatchStream (byte[] b, long m)
                {
                    mark = m;
                    match = b;
                }

                @Override
                public void write (int b) throws IOException
                {
                    if (matchMade) {
                        return;
                    }
                    if (match == null) {
                        throw new IOException("NULL match array");
                    }

                    if (match.length == 0) {
                        throw new IOException("Empty match array");
                    }

                    if (pos >= match.length) {
                        throw new IOException("No match");
                    }

                    if (b != match[pos]) {
                        throw new IOException("No match");
                    }

                    pos++;
                    if (pos >= match.length)
                    {
                        matchMade = true;
                        throw new MatchMadeException(mark);
                    }
                }

                public long getMark ()
                {
                    return mark;
                }
            }
        }

    }

}
