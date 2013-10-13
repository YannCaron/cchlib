/*
** -----------------------------------------------------------------------
** Nom           : cx/ath/choisnet/lang/reflect/Mappable.java
** Description   :
** Encodage      : ANSI
**
**  2.01.001 2005.10.24 Claude CHOISNET - version initiale
**  3.02.026 2006.07.19 Claude CHOISNET
**                      M�J Documentation
** -----------------------------------------------------------------------
**
** cx.ath.choisnet.lang.reflect.Mappable
*/
package cx.ath.choisnet.lang.reflect;

import java.util.Map;

/**
** <P>Permet d'avoir une vue synth�tique d'un objet</P>
**
** @author Claude CHOISNET
** @since   2.01.001
** @version 3.02.026
**
** @see Explorable
** @see MappableHelper
** @see MappableHelperFactory
*/
public interface Mappable
{

/**
** <P>
** Retourne des couples de cha�nes (nomDeMethode,valeur) � partir des
** observateurs de l'objet courant (en g�n�ral, il s'agit des m�thodes
** sans param�tres commen�ant par 'get' ou 'is'). Si c'est c'est la classe
** {@link MappableHelper} qui prend en charge la construction du r�sultat,
** le choix de m�thode explor�e sera d�fini par un objet {@link MappableHelperFactory}.
** </P>
**
** @see MappableHelper
** @see MappableHelperFactory
*/
public Map<String,String> toMap(); // -------------------------------------

} // class


