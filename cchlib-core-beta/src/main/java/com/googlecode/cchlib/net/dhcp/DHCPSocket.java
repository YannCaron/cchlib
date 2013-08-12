package com.googlecode.cchlib.net.dhcp;

import com.googlecode.cchlib.NeedDoc;
import com.googlecode.cchlib.NeedTestCases;

/**
 *
 *
 */
@NeedDoc
@NeedTestCases
public class DHCPSocket //extends DatagramSocket
{
    /** */
    @NeedDoc
    public static final int CLIENT_PORT = 68;
    /** */
    @NeedDoc
    public static final int SERVER_PORT = 67;
    /** */
    @NeedDoc
    public static final int DEFAULT_PACKET_SIZE = 1500;
    /** */
    @NeedDoc
    public static final int DEFAULT_SOTIME_OUT = 3000;
    /** */
    private int packetSize;
//
//    /**
//     *
//     * @param port
//     * @param laddr
//     * @throws SocketException
//     */
//    public DHCPSocket( final int port, final InetAddress laddr )
//        throws SocketException
//    {
//        super(port, laddr);
//
//        init();
//    }
//
//    /**
//     *
//     * @param port
//     * @throws java.net.SocketException
//     */
//    public DHCPSocket(int port)
//        throws java.net.SocketException
//    {
//        super(port);
//
//        init();
//    }
//
//    private void init()
//        throws java.net.SocketException
//    {
//        super.setSoTimeout(3000);
//
//        this.packetSize = 1500;
//    }
//
//    public void setMTU(int packetSize)
//    {
//        this.packetSize = packetSize;
//    }
//
//    public int getMTU()
//    {
//        return packetSize;
//    }
//
//    public synchronized void send(DHCPMessage inMessage)
//        throws IOException
//    {
//        super.send(inMessage.toDatagramPacket());
//    }
//
//    public synchronized boolean receive(DHCPMessage message)
//        throws IOException
//    {
//        DHCPParameters params = receive();
//
//        if(params != null) {
//            message.setDHCPParameters(params);
//
//            return true;
//        }
//        else {
//            return false;
//        }
//    }
//
//    public synchronized DHCPParameters receive()
//        throws IOException, SocketTimeoutException
//    {
////        try {
//            DatagramPacket incoming = new DatagramPacket(new byte[packetSize], packetSize);
//
//            super.receive( incoming );
//
//            return DHCPParameters.newInstance(incoming.getData());
////        }
////        catch(SocketTimeoutException e) {
////            Sys tem.er r.println((new StringBuilder()).append("java.net.SocketTimeoutException: ").append(e).toString());
////        }
////
////        return null;
//    }
}
