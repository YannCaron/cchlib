/*
** -----------------------------------------------------------------------
** Nom           : cx/ath/choisnet/lang/reflect/Explorable.java
** Description   :
** Encodage      : ANSI
**
**  3.02.026 2006.07.19 Claude CHOISNET - version initiale
** -----------------------------------------------------------------------
**
** cx.ath.choisnet.lang.reflect.Explorable
*/
package cx.ath.choisnet.lang.reflect;

import java.util.Map;

/**
** <P>Permet d'avoir une vue synth�tique d'un objet</P>
**
** @author Claude CHOISNET
** @since   3.02.026
** @version 3.02.026
**
** @see Mappable
** @see MappableHelper
** @see MappableHelperFactory
*/
public interface Explorable
{

/**
** <P>
** Retourne des couples de cha�nes (nomDeMethode,valeur) � partir des
** observateurs de l'objet courant, en g�n�ral, il s'agit des m�thodes
** sans param�tres commen�ant par 'get' ou 'is'; mais le choix des
** m�thodes explor�es est d�fini par un l'objet {@link MappableHelperFactory}
** qui initialisera 'mappableHelper'
** </P>
**
** @param mappableHelper Objet de prise en charge de la transformation.
**
** @see MappableHelper
** @see MappableHelperFactory
*/
public Map<String,String> toMap( MappableHelper mappableHelper ); // ------

} // class
