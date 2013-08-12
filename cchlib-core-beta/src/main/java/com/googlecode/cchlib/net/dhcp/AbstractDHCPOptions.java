package com.googlecode.cchlib.net.dhcp;
//
//import java.io.ByteArrayOutputStream;
//import java.io.DataInputStream;
//import java.io.IOException;
//import java.io.Serializable;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Set;
//import java.util.TreeMap;
//
///**
// *
// *
// */
//public abstract class AbstractDHCPOptions implements Serializable
//{
//    private static final long serialVersionUID = 1L;
//
//    private static final byte MAGIC_COOKIE[] = {
//        99, -126, 83, 99
//    };
//
//    public static final byte REQUESTED_IP = 50;
//    public static final byte LEASE_TIME = 51;
//    public static final byte MESSAGE_TYPE = 53;
//    public static final byte T1_TIME = 58;
//    public static final byte T2_TIME = 59;
//    public static final byte CLASS_ID = 60;
//    public static final byte CLIENT_ID = 61;
//    public static final byte END_OPTION = -1;
//    
//    public static final byte MESSAGE_TYPE_DHCPDISCOVER = 1;
//    public static final byte MESSAGE_TYPE_DHCPOFFER = 2;
//    public static final byte MESSAGE_TYPE_DHCPREQUEST = 3;
//    public static final byte MESSAGE_TYPE_DHCPDECLINE = 4;
//    public static final byte MESSAGE_TYPE_DHCPACK = 5;
//    public static final byte MESSAGE_TYPE_DHCPNAK = 6;
//    public static final byte MESSAGE_TYPE_DHCPRELEASE = 7;
//    public static final byte MESSAGE_TYPE_DHCPINFORM = 8;
//    private /*Map*/HashMap<Byte,DHCPOptionEntry> optionsTable;
//    //private static transient Properties prop;
//
//    public AbstractDHCPOptions()
//    {
//        optionsTable = new HashMap<Byte,DHCPOptionEntry>();
//    }
//
//    public AbstractDHCPOptions setOption(byte option, byte[] value)
//    {
//        setOption(option, new DHCPOptionEntry(value));
//
//        return this;
//    }
//
//    public AbstractDHCPOptions setOption(byte option, byte value)
//    {
//        setOption(option, new DHCPOptionEntry(value));
//
//        return this;
//    }
//
//    public AbstractDHCPOptions setOption(byte option, DHCPOptionEntry value)
//    {
//        optionsTable.put( Byte.valueOf( option ), value);
//
//        return this;
//    }
//
//    public AbstractDHCPOptions setOptions(AbstractDHCPOptions anOtherDHCPOptions)
//    {
//        setOptions( anOtherDHCPOptions.optionsTable.entrySet() );
//
//        return this;
//    }
//
//    public AbstractDHCPOptions setOptions(Set<Map.Entry<Byte,DHCPOptionEntry>> aCollection)
//    {
//        for( Map.Entry<Byte, DHCPOptionEntry> entry : aCollection ) {
//            setOption(
//                    entry.getKey().byteValue(),
//                    new DHCPOptionEntry( entry.getValue() )
//                    );
//            }
//        return this;
//    }
//
//    public void clear()
//    {
//        optionsTable.clear();
//    }
//
//    public void removeOption(Byte code)
//    {
//        optionsTable.remove(code);
//    }
//
//    public void removeOption(byte code)
//    {
//        removeOption( Byte.valueOf( code ) );
//    }
//
//    public DHCPOptionEntry getDHCPOptionEntry(byte code)
//    {
//        return optionsTable.get( Byte.valueOf( code ) );
//    }
//
//    public byte[] getOption(byte code)
//    {
//        DHCPOptionEntry entry = getDHCPOptionEntry(code);
//
//        if(entry != null) {
//            return entry.getOptionValue();
//            }
//        else {
//            return null;
//            }
//    }
//
//    public void init(DataInputStream dis) throws IOException
//    {
//        clear();
//
//        // Skip 4 bytes
//        dis.readByte();
//        dis.readByte();
//        dis.readByte();
//        dis.readByte();
//
//        byte code;
//
//        while((code = dis.readByte()) != -1) {
//            byte   length = dis.readByte();
//            byte[] datas  = new byte[length];
//
//            dis.readFully(datas);
//
//            setOption(code, new DHCPOptionEntry(datas));
//        }
//    }
//
//    public byte[] toByteArray()
//    {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//        for(int i = 0; i < MAGIC_COOKIE.length; i++) {
//            baos.write(MAGIC_COOKIE[i]);
//        }
//
//        byte[] content;
//
//        for(Iterator<Map.Entry<Byte,DHCPOptionEntry>> i$ = getOptionSet().iterator(); i$.hasNext(); baos.write(content, 0, content.length)) {
//            Map.Entry<Byte,DHCPOptionEntry> entry = i$.next();
//
//            baos.write( entry.getKey().byteValue() );
//            content = entry.getValue().getOptionValue();
//
//            baos.write( (byte)content.length );
//        }
//
//        baos.write(-1);
//
//        return baos.toByteArray();
//    }
//
//    public Set<Map.Entry<Byte, DHCPOptionEntry>> getOptionSet()
//    {
//        return Collections.unmodifiableSet((new TreeMap<Byte, DHCPOptionEntry>(optionsTable)).entrySet());
//    }
//
//    public String toHexString()
//    {
//        return  DHCPParameters.toHexString(toByteArray());
//    }
//
//    protected void cloneFrom( AbstractDHCPOptions source )
//    {
//        for( Map.Entry<Byte,DHCPOptionEntry> entry : source.optionsTable.entrySet() ) {
//            setOption(
//                    entry.getKey().byteValue(),
//                    entry.getValue().getClone()
//                    );
//            }
//    }
////    public AbstractDHCPOptions getClone()
////    {
////        AbstractDHCPOptions copy = newDHCPOptions();
////
////        for( Map.Entry<Byte,DHCPOptionEntry> entry : optionsTable.entrySet() ) {
////            copy.setOption(
////                    entry.getKey().byteValue(),
////                    entry.getValue().getClone()
////                    );
////            }
////
////        return copy;
////    }
////protected AbstractDHCPOptions getClone();
//
//    @Override
//    public String toString()
//    {
//        StringBuilder sb = new StringBuilder();
//
//        for( Map.Entry<Byte,DHCPOptionEntry> entry : getOptionSet() ) {
//            String comment = getOptionComment( entry.getKey().byteValue() );
//            char   type    = comment.charAt(2);
//            String displayValue;
//
//            switch(type) {
//            case 65:
//                displayValue = DHCPParameters.ip4AddrToString(entry.getValue().getOptionValue());
//                break;
//                
//            case 67:
//                displayValue = getSubComment( entry.getKey().byteValue(), entry.getValue().getOptionValue());
//                break;
//                
//            case 83:
//                displayValue = DHCPParameters.toString( entry.getValue().getOptionValue() );
//                break;
//                
//            default:
//                displayValue = entry.getValue().toString();
//                break;
//            }
//
//            sb.append(entry.getKey());
//            sb.append("\t=");
//            sb.append(displayValue);
//            sb.append(" # ");
//            sb.append(comment);
//            sb.append('\n');
//        }
//
//        return sb.toString();
//    }
//
//    public abstract String getProperty(String name);
//
//    public String getOptionComment(byte option)
//    {
//        String value = getProperty( "OPTION_NUM." + option );
//
//        if(value != null) {
//            return value;
//            }
//        else {
//            return "Unkown option " + option;
//            }
//    }
//
//    public String getSubComment(byte option, byte[] code)
//    {
//        if( code.length != 1 ) {
//            return "Bad size for data expected 1, found " + code.length;
//            }
//        else {
//            return getSubComment(option, code[0]);
//            }
//    }
//
//    public String getSubComment(byte option, byte code)
//    {
//        String value = getProperty( option + "." + code );
//
//        if(value != null) {
//            return value;
//            }
//        else {
//            return "Unkown option.code " + option + '.' + code;
//            }
//    }
//
//}
