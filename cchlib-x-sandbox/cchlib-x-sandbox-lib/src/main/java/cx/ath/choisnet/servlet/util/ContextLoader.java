/*
** -----------------------------------------------------------------------
** Nom           : cx/ath/choisnet/servlet/util/ContextLoader.java
** Description   :
** Encodage      : ANSI
**
**  3.02.042 2007.01.08 Claude CHOISNET - Version initiale
** -----------------------------------------------------------------------
**
** cx.ath.choisnet.servlet.util.ContextLoader
**
*/
package cx.ath.choisnet.servlet.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
**
** @author Claude CHOISNET
** @since   3.02.042
** @version 3.02.042
*/
public class ContextLoader
{

/**
** All methods statics
*/
private ContextLoader() // ------------------------------------------------
{
    // empty
}

/**
** Recherche de objet T � partir du request courant sur le contexte,
** si l'objet n'a pas �t� trouv� on le cr�e.
**
** @param request               Objet {@link javax.servlet.http.HttpServletRequest}
**                              valide de la requ�te en cours.
** @param aContextClass         Class de l'object attendu.
** @param aContextInitializator Initialisateur de l'objet.
**
** @see ContextInitializable
*/
public static <T> T getContext( // ----------------------------------------
    final HttpServletRequest        request,
    final Class<T>                  aContextClass,
    final ContextInitializable<T>   aContextInitializator
    )
    throws ContextInitializationException
{
 final String           attributeName   = aContextInitializator.getAttributeName();
 final ServletContext   servletContext  = getServletContext( request, aContextInitializator );

 try {
    final T context = aContextClass.cast( servletContext.getAttribute( attributeName ) );

    if( context != null ) {
        return context;
        }
    }
 catch( ClassCastException ignore ) {
    // ignore
    }

 final T context = aContextInitializator.newInstance( request );

 servletContext.setAttribute(
                    aContextInitializator.getAttributeName(),
                    context
                    );

 return context;
}

/**
** Recherche du context de l'application {@link ContextInitializable#getServletContextName()}
** � partir sur la liste des contextes disponibles trouv� � l'aide du request
** donn�e.
** <br/>
** Si le contexte n'est pas trouv� ou si la valeur de {@link ContextInitializable#getServletContextName()}
** est null, c'est le contexte li� au request qui sera utilis�.
**
** @param request               Objet {@link javax.servlet.http.HttpServletRequest}
**                              valide de la requ�te en cours.
** @param aContextInitializator Initialisateur de l'objet.
**
** @see ServletContext
** @see ServletContext#getContext(String)
** @see ContextInitializable#getServletContextName()
*/
public static ServletContext getServletContext( // --------------------------------
    final HttpServletRequest    request,
    final ContextInitializable  aContextInitializator
    )
{
 final String servletContextName = aContextInitializator.getServletContextName();

 if( servletContextName != null ) {
    final ServletContext thisServletContext
        = request.getSession().getServletContext().getContext(
            servletContextName
            );

    if( thisServletContext != null ) {
        return thisServletContext;
        }
    }

 //
 // On n'a pas r�cup�rer le contexte � partir de son nom, on prend celui
 // directement li� au request
 //
 return request.getSession().getServletContext();
}

} // class
