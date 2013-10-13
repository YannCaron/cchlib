/*
** -----------------------------------------------------------------------
** Nom           : cx/ath/choisnet/net/dhcp/DHCPOptions.java
** Description   :
**
**  3.02.014 2006.06.21 Claude CHOISNET - Version initiale
**                      Adapt� du code de Jason Goldschmidt and Nick Stone
**                      edu.bucknell.net.JDHCP.DHCPOptions
**                      http://www.eg.bucknell.edu/~jgoldsch/dhcp/
**                      et bas� sur les RFCs 1700, 2131 et 2132
**  3.02.015 2006.06.22 Claude CHOISNET
**                      implemente java.io.Serializable
** -----------------------------------------------------------------------
**
** cx.ath.choisnet.net.dhcp.DHCPOptions
**
*/
package cx.ath.choisnet.net.dhcp;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

/**
**
** <pre>
** CHAMP   OCTETS  DESCRIPTION
**
** op          1   Code op�ration du message:/
**                 type du message. 1 = BOOTREQUEST, 2 = BOOTREPLY
**
** htype       1   Adresse mat�rielle, voir la section ARP dans le RFC "Assigned Numbers" ;
**                 par ex., '1' = Ethernet 10Mb.
**
** hlen        1   Longueur de l'adresse mat�rielle (par ex. '6' for Ethernet 10Mb).
**
** hops        1   Mis � z�ro par le client, utilis� de mani�re optionnelle par les agents
**                 de relais quand on d�marre via un agent de relais
**
** xid         4   Identifiant de transaction, un nombre al�atoire choisi par le client,
**                 utilis� par le client et le serveur pour associer les messages et les
**                 r�ponses entre un client et un serveur
**
** secs        2   Rempli par le client, les secondes s'�coulent depuis le processus
**                 d'acquisition ou de renouvellement d'adresse du client
**
** flags       2   Drapeaux (voir figure 2).
**
** ciaddr      4   Adresse IP des clients, rempli seulement si le client est dans un �tat
**                 AFFECT�, RENOUVELLEMENT ou REAFFECTATION
**                 et peut r�pondre aux requ�tes ARP
**
** yiaddr      4   'votre' (client) adresse IP.
**
** siaddr      4   Adresse IP du prochain serveur � utiliser pour le processus de d�marrage;
**                 retourn�e par le serveur dans DHCPOFFER et DHCPACK.
**
** giaddr      4   Adresse IP de l'agent de relais, utilis�e pour d�marrer via un agent de relais.
**
** chaddr     16   Adresse mat�rielle des clients (Address MAC).
**
** sname      64   Nom d'h�te du serveur optionnel, cha�ne de caract�res termin�e par
**                 un caract�re nul.
**
** fichier   128   Nom du fichier de d�marrage, cha�ne termin�e par un caract�re nul;
**                 nom "generic" ou nul dans le DHCPDISCOVER,
**                 nom du r�pertoire explicite dans DHCPOFFER.
**
** options   var   Champ de param�tres optionnels.
**
** </pre>
**
** @author Jason Goldschmidt
** @author Claude CHOISNET
** @version 3.02.014
*/
public class DHCPOptions
    implements java.io.Serializable
{
/** serialVersionUID */
private static final long serialVersionUID = 1L;

/** */
public static final byte[] MAGIC_COOKIE = { 0x63, (byte)0x82, 0x53, 0x63 };

/**
** <p>DHCP option constants</p>
**
**
*/
public static final byte REQUESTED_IP = 50;

/**
** <p>DHCP option constants</p>
**
**
*/
public static final byte LEASE_TIME = 51;

/**
** <p>DHCP option constants</p>
**
**
*/
public static final byte MESSAGE_TYPE = 53;

/**
** <p>DHCP option constants</p>
**
**
*/
public static final byte T1_TIME = 58;

/**
** <p>DHCP option constants</p>
**
**
*/
public static final byte T2_TIME = 59;

/**
** <p>DHCP option constants</p>
**
**
*/
public static final byte CLASS_ID = 60;

/**
** <p>DHCP option constants</p>
**
**
*/
public static final byte CLIENT_ID = 61;

/**
** <p>DHCP option constants</p>
**
** End option
*/
public static final byte END_OPTION = (byte)0xFF;

/**
** <p>DHCP option value for {@link #MESSAGE_TYPE} (RFC 2132)</p>
**
** Code for DHCPDISCOVER Message
*/
public static final byte MESSAGE_TYPE_DHCPDISCOVER = 1;

/**
** <p>DHCP option value for {@link #MESSAGE_TYPE} (RFC 2132)</p>
**
** Code for DHCPOFFER Message
*/
public static final byte MESSAGE_TYPE_DHCPOFFER = 2;

/**
** <p>DHCP option value for {@link #MESSAGE_TYPE} (RFC 2132)</p>
**
** Code for DHCPREQUEST Message
*/
public static final byte MESSAGE_TYPE_DHCPREQUEST = 3;

/**
** <p>DHCP option value for {@link #MESSAGE_TYPE} (RFC 2132)</p>
**
** Code for DHCPDECLINE Message
*/
public static final byte MESSAGE_TYPE_DHCPDECLINE = 4;

/**
** <p>DHCP option value for {@link #MESSAGE_TYPE} (RFC 2132)</p>
**
** Code for DHCPACK Message
*/
public static final byte MESSAGE_TYPE_DHCPACK = 5;

/**
** <p>DHCP option value for {@link #MESSAGE_TYPE} (RFC 2132)</p>
**
** Code for DHCPNAK Message
*/
public static final byte MESSAGE_TYPE_DHCPNAK = 6;

/**
** <p>DHCP option value for {@link #MESSAGE_TYPE} (RFC 2132)</p>
**
** Code for DHCPRELEASE Message
*/
public static final byte MESSAGE_TYPE_DHCPRELEASE = 7;

/**
** <p>DHCP option value for {@link #MESSAGE_TYPE} (RFC 2132)</p>
**
** Code for DHCPINFORM Message
*/
public static final byte MESSAGE_TYPE_DHCPINFORM = 8;

/**
**
*/
private Map<Byte,DHCPOptionEntry> optionsTable;

/**
**
*/
public DHCPOptions() // ---------------------------------------------------
{
 this.optionsTable = new HashMap<Byte,DHCPOptionEntry>();
}

/**
** Changes an existing option to new value
**
** @param option    The node's option code
** @param value     Content of node option
**
*/
public void setOption( final byte option, final byte[] value ) // ---------
{
 setOption( option, new DHCPOptionEntry( value ) );
}

/**
** Changes an existing option to new value
**
** @param option    The node's option code
** @param value     Content of node option (1 byte)
**
*/
public void setOption( final byte option, final byte value ) // -----------
{
 setOption( option, new DHCPOptionEntry( value ) );
}

/**
** Changes an existing option to new value
**
** @param option    The node's option code
** @param value     Content of node option
**
*/
public void setOption( // -------------------------------------------------
    final byte              option,
    final DHCPOptionEntry   value
    )
{
 this.optionsTable.put( new Byte( option ), value );
}

/**
** Changes or Append all options found in anOtherDHCPOptions
**
** @param anOtherDHCPOptions collection to append
**
** @see #setOptions(Collection)
*/
public void setOptions( // ------------------------------------------------
    final DHCPOptions anOtherDHCPOptions
    )
{
 setOptions( anOtherDHCPOptions.optionsTable.entrySet() );
}

/**
**
** @param aCollection collection to append
**
** @see #setOptions(DHCPOptions)
*/
public void setOptions( // ------------------------------------------------
    final Collection<Map.Entry<Byte,DHCPOptionEntry>> aCollection
    )
{
 for( Map.Entry<Byte,DHCPOptionEntry> entry : aCollection ) {
    setOption( entry.getKey(), entry.getValue().getClone() );
    }
}

/**
** Clear internal options list
*/
public void clear() // ----------------------------------------------------
{
 this.optionsTable.clear();
}

/**
** Removes option with specified bytecode
**
** @param code The code of option to be removed
*/
public void removeOption( final Byte code ) // ----------------------------
{
 this.optionsTable.remove( code );
}

/**
** Removes option with specified bytecode
**
** @param code The code of option to be removed
*/
public void removeOption( final byte code ) // ----------------------------
{
 removeOption( new Byte( code ) );
}

/**
** Fetches value of option by its option code
**
** @param code The node's option code
**
** @return a valid DHCPOptionEntry containing the value of option code.
**         null is returned if option is not set.
*/
public DHCPOptionEntry getDHCPOptionEntry( final byte code ) // -----------
{
 return this.optionsTable.get( new Byte( code ) );
}

/**
** Fetches value of option by its option code
**
** @param code The node's option code
**
** @return byte array containing the value of option entryCode.
**         null is returned if option is not set.
*/
public byte[] getOption( final byte code ) // -----------------------------
{
 final DHCPOptionEntry entry = getDHCPOptionEntry( code );

 if( entry != null ) {
    return entry.getOptionValue();
    }

 return null;
}

/**
** Clear internal options list, and converts an options byte array
** to a list (ignore 4 first bytes, vendor magic cookie)
**
** @param optionsArray The byte array representation of the options list
public void _init( final byte[] optionsArray ) // --------------------------
    throws ArrayIndexOutOfBoundsException
{
 clear();

 // Assume options valid and correct
 // ignore vendor magic cookie
 int pos = 4;

 while( optionsArray[ pos ] != END_OPTION ) { // until end option
    final byte  code    = optionsArray[ pos++ ];
    final byte  length  = optionsArray[ pos++ ];

    setOption(
        code,
        new DHCPOptionEntry( optionsArray, pos, length )
        );

    pos += length;  // increment position pointer
    }
}
*/

/**
** Clear internal options list, and converts an options DataInputStream
** stream to a list (ignore 4 first bytes, vendor magic cookie)
**
** @param dis DataInputStream representation of the options list
*/
public void init( final DataInputStream dis ) // --------------------------
    throws java.io.IOException
{
 clear();

 // Assume options valid and correct
 // ignore vendor magic cookie
 dis.readByte();
 dis.readByte();
 dis.readByte();
 dis.readByte();

 byte code;

 while( (code = dis.readByte()) != END_OPTION ) { // until end option
    final byte      length  = dis.readByte();
    final byte[]    datas   = new byte[ length ];

    dis.readFully( datas );

    setOption( code, new DHCPOptionEntry( datas ) );
    }
}

/**
** Converts a linked options list to a byte array
**
** @return array representation of optionsTable
*/
public byte[] toByteArray() // --------------------------------------------
{
 final ByteArrayOutputStream baos  = new ByteArrayOutputStream();

 //
 // insert vendor magic cookie
 //
 for( int i = 0; i<MAGIC_COOKIE.length; i++ ) {
    baos.write( MAGIC_COOKIE[ i ] );
    }

 for( Map.Entry<Byte,DHCPOptionEntry> entry : this.getOptionSet() ) {
    baos.write( entry.getKey() );

    final byte[] content = entry.getValue().getOptionValue();

    baos.write( (byte)content.length );
    baos.write( content, 0, content.length );
    }

 //
 // insert end option
 //
 baos.write( END_OPTION );

 return baos.toByteArray();
}

/**
**
*/
public Set<Map.Entry<Byte,DHCPOptionEntry>> getOptionSet() // -------------
{
 return Collections.unmodifiableSet(
    new TreeMap<Byte,DHCPOptionEntry>( this.optionsTable ).entrySet()
    );
}

/**
**
*/
public String toHexString() // --------------------------------------------
{
 return DHCPParameters.toHexString( this.toByteArray() );
}

/**
**
*/
public DHCPOptions getClone() // ------------------------------------------
{
 final DHCPOptions copy = new DHCPOptions();

 for( Map.Entry<Byte,DHCPOptionEntry> entry : this.optionsTable.entrySet() ) {
    copy.setOption(
        entry.getKey().byteValue(),
        entry.getValue().getClone()
        );
    }

 return copy;
}

/** */
private final static String messageFmtString = "OPT[{0,number,##0}]\t=";

/** */
private final static MessageFormat msgFmt= new MessageFormat( messageFmtString );

/** */
private final Object[] msgFmtObjects = new Object[ 1 ];

/**
**
*/
private String format( final byte optionNumber ) // -----------------------
{
 this.msgFmtObjects[ 0 ] = new Byte( optionNumber );

 return msgFmt.format( this.msgFmtObjects );
}

/**
**
*/
public String toString() // -----------------------------------------------
{
 final StringBuilder sb = new StringBuilder();

 for( Map.Entry<Byte,DHCPOptionEntry> entry : this.getOptionSet() ) {
    final String            comment = getOptionComment( entry.getKey().byteValue() );
    final char              type    = comment.charAt(2);
    final DHCPOptionEntry   value   =  entry.getValue();
    String                  displayValue;

    switch( type ) {
        case 'A' :
            displayValue    = DHCPParameters.ip4AddrToString( value.getOptionValue() );
            break;

        case 'C' :
            displayValue = getSubComment(
                            entry.getKey().byteValue(),
                            value.getOptionValue()
                            );
            break;

        case 'D' :
            displayValue = value.toString()
                            + "="
                            + DHCPParameters.byteToLong( value.getOptionValue() )
                            + " secs";
            break;

        case 'S' :
            displayValue    = DHCPParameters.toString( value.getOptionValue() );
            break;

        default:
            displayValue = value.toString();
            break;
        }

    sb.append( format( entry.getKey() ) );
    sb.append( displayValue );
    sb.append( " # " );
    sb.append( comment );
    sb.append( "\n" );
    }

 return sb.toString();
}

/** Calculer lors du premier appel */
private transient static Properties prop;

/**
**
*/
public String getProperty( final String name ) // -------------------------
{
 if( this.prop == null ) {
    final String ressourceName = "DHCPOptions.properties";

    this.prop = new Properties();

    final InputStream is = getClass().getResourceAsStream( ressourceName );

    try {
        this.prop.load( is );

        is.close();
        }
    catch( java.io.IOException e ) {
        throw new RuntimeException( "Can't read :" + ressourceName, e );
        }
    catch( NullPointerException e ) {
        throw new RuntimeException( "Can't find :" + ressourceName, e );
        }
    }

 return this.prop.getProperty( name );
}

/**
**
*/
public String getOptionComment( final byte option ) // --------------------
{
 final String value = getProperty( "OPTION_NUM." + option );

 if( value != null ) {
    final int end = value.indexOf( '\t' );

    if( end != -1 ) {
        return value.substring( 0, end );
        }
    else {
        return value;
        }
    }
 else {
    return "Unkown option " + option;
    }
}

/**
**
*/
public String getSubComment( final byte option, final byte[] code ) // ----
{
 if( code.length != 1 ) {
    return "Bad size for data expected 1, found " + code.length;
    }
 else {
    return getSubComment( option, code[ 0 ] );
    }
}

/**
**
*/
public String getSubComment( final byte option, final byte code ) // ------
{
 final String value = getProperty( option + "." + code );

 if( value != null ) {
    return value;
    }
 else {
    return "Unkown option.code " + option + "." + code;
    }
}

} // class

/**
** Removes option with specified bytecode
**
** @param entryCode The code of option to be removed
public void removeOption( final int entryCode ) // ------------------------
{
 removeOption( new Byte( (byte)entryCode ) );
}
*/

/*
** Returns true if option code is set in list; false otherwise
**
** @param entryCode The node's option code
**
** @return true if option is set, otherwise false
public boolean contains( final byte entryCode ) // ------------------------
{
 return this.optionsTable.containsKey( new Byte( entryCode ) );
}
**/

/**
** Determines if list is empty
**
** @return true if there are no options set, otherwise false
public boolean isEmpty() // -----------------------------------------------
{
 return this.optionsTable.isEmpty();
}
*/

