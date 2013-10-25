/*
** -----------------------------------------------------------------------
** Nom           : cx/ath/choisnet/html/util/HTMLFormEntry.java
** Description   :
**
**  3.02.034 2006.08.02 Claude CHOISNET - Version initiale
**                      Nom: cx.ath.choisnet.html.HTMLDocumentWriter
**  3.02.037 2006.08.07 Claude CHOISNET
**                      Nouveau nom: cx.ath.choisnet.html.util.HTMLDocumentWriter
** -----------------------------------------------------------------------
**
** cx.ath.choisnet.html.util.HTMLFormEntry
**
*/
package cx.ath.choisnet.html.util;

import javax.servlet.ServletRequest;
import java.util.Collection;
import cx.ath.choisnet.html.util.FormItem;

/**
** <p>
** Interface commune � tous les objets HTML d�pendant d'un formulaire,
** cette interface d�cris les m�thodes permettant de construire
** la page finale � partir des diff�rents �l�ments ainsi que de r�cup�rer
** les donn�es de cette m�me page lorsqu'il s'agit d'un formulaire.
** </p>
**
**
** @author Claude CHOISNET
** @since   3.02.034
** @version 3.02.037
*/
public abstract interface HTMLFormEntry<T extends HTMLObjectInterface,U>
    extends
        HTMLObjectInterface<T>,
        HTMLWritable,
        java.io.Serializable
{
/**
** Initialise la valeur (par d�faut) de l'entr�e du formulaire � partir
** de son type natif
**
** @throws UnsupportedOperationException
*/
public T setCurrentValue( U value ) // ------------------------------------
    throws UnsupportedOperationException;

/**
** Lecture des donn�es la lecture des donn�es depuis la page HTML (formulaire)
**
** @return la valeur
**
** @throws UnsupportedOperationException
** @throws HTMLFormException
*/
public U getCurrentValue( ServletRequest request ) // ---------------------
    throws UnsupportedOperationException, HTMLFormException;

/**
**
*/
public T append( Collection<FormItem> a ); // -----------------------------

} // interface


