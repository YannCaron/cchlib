/*
** -----------------------------------------------------------------------
** Nom           : cx/ath/choisnet/system/win32/EnvArc.java
** Description   :
** Encodage      : ANSI
**
**  1.00.___ 2005.04.25 Claude CHOISNET - Version initiale
**  1.10.___ 2005.04.29 Claude CHOISNET
**                      Prise en charge du nom complet des cl�s
**  1.20.___ 2005.04.29 Claude CHOISNET
**                      Amm�lioration de la prise en compte des erreurs.
**  1.30.___ 2005.05.13 Claude CHOISNET
**                      Correction de setRegInteger( String, String, int )
**  3.02.017 2006.06.28 Claude CHOISNET
**                      @Deprecated
** -----------------------------------------------------------------------
**
** cx.ath.choisnet.system.win32.EnvArc
**
*/
package cx.ath.choisnet.system.win32;

//import com.ice.jni.registry.RegistryKey;
//import com.ice.jni.registry.Registry;
//import com.ice.jni.registry.RegistryValue;
//import com.ice.jni.registry.RegStringValue;
//import com.ice.jni.registry.RegDWordValue;
//import com.ice.jni.registry.RegistryValue;
//
///**
//**
//** @author Claude CHOISNET
//** @version 1.20
//**
//** @deprecated use {@link cx.ath.choisnet.system.EnvArc} instead
//*/
//@Deprecated
//public class EnvArc
//{
//
///** */
//private final static String SYSTEM_ENVIRONMENT_BASE = "HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\Control\\Session Manager\\Environment";
//
///**
//**
//*/
//public EnvArc() // --------------------------------------------------------
//{
// // empty
//}
//
///**
//**
//*/
//protected RegistryValue getRegistryValue( // ------------------------------
//    final RegKey    regKey,
//    final String    regValue
//    )
//{
// try {
//    return regKey.getRegistryKeyReadOnly().getValue( regValue );
//    }
// catch( com.ice.jni.registry.NoSuchKeyException e ) {
//    // logger.error( "getRegistryValue( \"" + regKey + "\", \"" + regValue + "\" ) : " + e );
//
//    return null;
//    }
// catch( com.ice.jni.registry.RegistryException e ) {
//    // logger.error( "getRegistryValue( \"" + regKey + "\", \"" + regValue + "\" )", e );
//
//    return null;
//    }
//}
//
///**
//** @return le contenu de la cl� si elle existe et si c'est une cha�ne.
//*/
//public String getRegString( // --------------------------------------------
//    final String regName,
//    final String regValue
//    )
//{
//
// try {
//    RegistryValue aValue = getRegistryValue( new RegKey( regName ), regValue );
//
//    if( aValue != null ) {
//        return ((RegStringValue)aValue).getData();
//        }
//     else {
//        // logger.warn( "getRegString( \"" + regName + "\", \"" + regValue + "\" ) : valeur non trouv�e" );
//        }
//    }
// catch( ClassCastException e ) {
//    // logger.error( "getRegString( \"" + regName + "\", \"" + regValue + "\" ) : ", e );
//    }
//
// return null;
//}
//
///**
//**
//*/
//public void setRegString( // ------------------------------------------
//    final String    regName,
//    final String    regValue,
//    final String    regData
//    )
//{
// final RegKey regKey = new RegKey( regName );
//
// try {
//    final RegistryKey myKey = regKey.getRegistryKeyReadWrite();
//
//    myKey.setValue( new RegStringValue( myKey, regValue, regData ) );
//    }
// catch( com.ice.jni.registry.RegistryException e ) {
//    final String msg = "setRegString( \"" + regName + "\", \"" + regValue + "\", \"" + regData + "\" ) : ";
//
//    // logger.error( msg, e );
//
//    throw new RuntimeException( msg, e );
//    }
//
//}
//
///**
//**
//*/
//public Integer getRegInteger( // ------------------------------------------
//    final String regName,
//    final String regValue
//    )
//{
// try {
//    RegistryValue aValue = getRegistryValue( new RegKey( regName ), regValue );
//
//    if( aValue != null ) {
//        return new Integer( ((RegDWordValue)aValue).getData() );
//        }
//    }
// catch( ClassCastException e ) {
//    // logger.error( "getRegInteger( \"" + regName + "\", \"" + regValue + "\" ) : ", e );
//    }
//
// return null;
//}
//
///**
//**
//*/
//public void setRegInteger( // ------------------------------------------
//    final String    regName,
//    final String    regValue,
//    final int       regData
//    )
//{
// final RegKey regKey = new RegKey( regName );
//
// try {
//    // final RegistryKey myKey = Registry.HKEY_LOCAL_MACHINE.openSubKey( regName, RegistryKey.ACCESS_WRITE );
//    final RegistryKey myKey = regKey.getRegistryKeyReadWrite();
//
//    myKey.setValue( new RegDWordValue( myKey, regValue, RegDWordValue.REG_DWORD, regData ) );
//    }
// catch( com.ice.jni.registry.RegistryException e ) {
//    final String msg = "setRegString( \"" + regName + "\", \"" + regValue + "\", \"" + regData + "\" ) : ";
//
//    // logger.error( msg, e );
//
//    throw new RuntimeException( msg, e );
//    }
//}
//
///**
//**
//*/
//public String getProcessVar( String name ) // -----------------------------
//{
// try {
//    Process proc = Runtime.getRuntime().exec( "Cmd /C Set "+ name );
//
//    java.util.Properties prop = new java.util.Properties();
//    prop.load( proc.getInputStream() );
//
//    return prop.getProperty( name );
//    }
// catch( java.io.IOException e ) {
//    // logger.error( "Can't getVar( \"" + name + "\" ) : ", e );
//
//    //
//    // ignore !!!
//    //
//    return null;
//    }
//}
//
///**
//** @return le contenu de la variable 'name' ou une cha�ne vide si elle
//**         n'existe pas.
//*/
//public String getVar( String name ) // ------------------------------------
//{
// final String value = getRegString( SYSTEM_ENVIRONMENT_BASE, name );
//
// return value == null ? "" : value;
//}
//
///**
//**
//*/
//public void setVar( String name, String value ) // ------------------------
//{
// setRegString( SYSTEM_ENVIRONMENT_BASE, name, value );
//}
//
///**
//**
//public void setVar( String name, String value ) // ------------------------
//{
// try {
//    Process proc = Runtime.getRuntime().exec( "Cmd /C SetX "+ name + " \"" + value + "\" -m" );
//    }
// catch( java.io.IOException e ) {
//    //
//    // ignore !!!
//    //
//    logger.error( "Can't setVar '" + name + "'", e );
//    }
//}
//*/
//    class RegKey
//    {
//        final String        regKeyName;
//        final RegistryKey   registryTop;
//        final String        regName;
//
//        /**
//        **
//        */
//        public RegKey( String regKeyName ) // - - - - - - - - - - - - - - -
//        {
//            this.regKeyName = regKeyName;
//
//            int separator = regKeyName.indexOf( "\\" );
//
//            if( separator <= 0 ) {
//                throw new RuntimeException( "Bad format : \"" + regKeyName + "\"" );
//                }
//
//            final String begin  = regKeyName.substring( 0, separator ).toUpperCase();
//            final String end    = regKeyName.substring( separator + 1 );
//
//            //System.out.println( "regKeyName = " + regKeyName );
//            //System.out.println( "begin = " + begin );
//            //System.out.println( "end = " + end );
//
//            this.regName = end;
//
//            if( begin.equals( "HKEY_LOCAL_MACHINE" ) ||  begin.equals( "HKLM" ) ) {
//                registryTop = Registry.HKEY_LOCAL_MACHINE;
//                }
//            else if( begin.equals( "HKEY_CLASSES_ROOT" ) ||  begin.equals( "HKCR" ) ) {
//                registryTop = Registry.HKEY_CLASSES_ROOT;
//                }
//            else if( begin.equals( "HKEY_CURRENT_CONFIG" ) ||  begin.equals( "HKCC" ) ) {
//                registryTop = Registry.HKEY_CURRENT_CONFIG;
//                }
//            else if( begin.equals( "HKEY_CURRENT_USER" ) ||  begin.equals( "HKCU" ) ) {
//                registryTop = Registry.HKEY_CURRENT_USER;
//                }
//            else if( begin.equals( "HKEY_DYN_DATA" ) ||  begin.equals( "HKDD" ) ) {
//                registryTop = Registry.HKEY_DYN_DATA;
//                }
//            else if( begin.equals( "HKEY_PERFORMANCE_DATA" ) ||  begin.equals( "HKPD" ) ) {
//                registryTop = Registry.HKEY_PERFORMANCE_DATA;
//                }
//            else if( begin.equals( "HKEY_USERS" ) ) {
//                registryTop = Registry.HKEY_USERS;
//                }
//            else {
//                throw new RuntimeException( "Unkwon TopRegistry : \"" + begin + "\"" );
//                }
//        }
//
//        /** */
//        public RegistryKey getParentRegistryKey() // - - - - - - - - - - -
//        {
//            return this.registryTop;
//        }
//
//        /** */
//        public String getRegistryName() // - - - - - - - - - - - - - - - -
//        {
//            return this.regName;
//        }
//
//        /**
//        **
//        */
//        public RegistryKey getRegistryKeyReadOnly() // - - - - - - - - - -
//            throws com.ice.jni.registry.RegistryException
//        {
//            return this.registryTop.openSubKey( this.regName );
//        }
//
//        /**
//        **
//        */
//        public RegistryKey getRegistryKeyReadWrite() // - - - - - - - - - -
//            throws com.ice.jni.registry.RegistryException
//        {
//            return this.registryTop.openSubKey( this.regName, RegistryKey.ACCESS_WRITE );
//        }
//
//        /**
//        **
//        */
//        public String toString() // - - - - - - - - - - - - - - - - - - - -
//        {
//            return regKeyName;
//        }
//
//    }
//
//} // class
