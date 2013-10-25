/*
** -----------------------------------------------------------------------
** Nom           : cx/ath/choisnet/util/CollectionFilter.java
** Description   :
**
**  3.01.032 2006.04.25 Claude CHOISNET - Version initiale
** -----------------------------------------------------------------------
**
** cx.ath.choisnet.util.CollectionFilter
**
*/
package cx.ath.choisnet.util;

import java.util.Collection;

/**
** Objectif de l'interface traiter des algorithmes g�n�riques de filtrage.
**
** @author Claude CHOISNET
** @since   3.01.032
** @version 3.01.032
**
** @see cx.ath.choisnet.util.duplicate.DuplicateLayer
** @see cx.ath.choisnet.util.impl.CollectionFilterImpl
** @see cx.ath.choisnet.util.IteratorFilter
** @see cx.ath.choisnet.util.Selectable
*/
public interface CollectionFilter<T>
{

/**
** Filtre une collection d'�l�ment pour obtenir un sous ensemble de
** cette liste.
**
** @param elements Collections d'�l�ments, ce param�tre ne doit pas �tre
**        null, mais la collection peut �ventuellement �tre vide.
**
** @return un Collections d'�l�ments, sous ensemble de la collection don�e,
**         ne retourne jamais null, mais le r�sultat peut �ventullement
**         �tre une collection vide.
*/
public Collection<T> apply( Collection<T> elements ); // ------------------

} // interface
