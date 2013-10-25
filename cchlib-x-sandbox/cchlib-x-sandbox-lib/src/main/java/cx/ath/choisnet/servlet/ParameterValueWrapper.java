/*
** -----------------------------------------------------------------------
** Nom           : cx/ath/choisnet/servlet/ParameterValueWrapper.java
** Description   :
** Encodage      : ANSI
**
**  2.02.030 2005.12.21 Claude CHOISNET - Version initiale
**  2.02.033 2005.12.23 Claude CHOISNET
**                      Ajout de asList( HttpServletRequest, String )
**                      Mes m�thodes asList() peuvebt retourn�e null.
** -----------------------------------------------------------------------
**
** cx.ath.choisnet.servlet.ParameterValueWrapper
**
**
*/
package cx.ath.choisnet.servlet;

import cx.ath.choisnet.util.Wrappable;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
**
** @author Claude CHOISNET
** @version 2.02.030
** @since   2.02.033
**
** @see cx.ath.choisnet.util.WrapperHelper
** @see cx.ath.choisnet.servlet.ParameterValue
*/
public class ParameterValueWrapper<T>
{
/** */
private final Wrappable<String,T> wrapper;

/**
**
*/
public ParameterValueWrapper( Wrappable<String,T> wrapper ) // ------------
{
 this.wrapper = wrapper;
}

/**
** <p>
** Retourne les valeurs du param�tre sous forme d'une liste typ�e.
** </p>
**
** @return une liste contenant les valeurs du param�tre s'il existe,
**         null autrement
*/
public List<T> asList( // -------------------------------------------------
    final HttpServletRequest    request,
    final String                paramName
    )
{
 return asList( request.getParameterValues( paramName ) );
}

/**
** <p>
** Retourne les valeurs du param�tre sous forme d'une liste typ�e.
** </p>
** <p>
** Cette m�thode s'appuit sur {@link ParameterValue#toArray()} pour
** retrouver les valeurs du param�tres.
** </p>
**
** @return une liste contenant les valeurs du param�tre s'il existe,
**         null autrement
*/
public List<T> asList( final ParameterValue paramValue ) // ---------------
{
 return asList( paramValue.toArray() );
}

/**
** <p>
** Transforme un tableau de cha�ne en sont �quivalant sous forme
** de liste en accort avec le wrapper courant.
** </p>
** @param values tableau de cha�nes � traiter.
**
** @return une liste contenant les valeurs wrapp�es, ou null si le param�tre
**         values est null.
*/
private List<T> asList( final String[] values ) // ------------------------
{
 if( values == null ) {
    return null;
    }

 final int      len     = values.length;
 final List<T>  list    = new java.util.ArrayList<T>( len );

 for( int i = 0; i<len; i++ ) {
    list.add( this.wrapper.wrappe( values[ i ] ) );
    }

 return list;
}

} // class

