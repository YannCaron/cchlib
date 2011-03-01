/**
 * 
 */
package alpha.cx.ath.choisnet.net;

import java.net.InetAddress;

/**
 * 
 * @author Claude CHOISNET
 */
public class PingerIOException extends PingerException 
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private InetAddress inetAddress;

    public PingerIOException( String message, Throwable cause, InetAddress inetAddress )
    {
        super( message, cause );
        
        this.inetAddress = inetAddress;
    }

    public InetAddress getInetAddress()
    {
        return this.inetAddress;
    }

    @Override
    public String getHostname()
    {
        return this.inetAddress.getHostName();
    }
}