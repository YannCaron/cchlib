/*
** -----------------------------------------------------------------------
** Nom           : cx/ath/choisnet/util/duplicate/MD5Collection.java
** Description   :
** Encodage      : ANSI
**
**  3.01.042 2006.05.24 Claude CHOISNET
**                      Reprise de la classe:
**                          cx.ath.choisnet.util.checksum.MD5Collection
**                      sous le nom:
**                          cx.ath.choisnet.util.duplicate.MD5Collection
** -----------------------------------------------------------------------
**
** cx.ath.choisnet.util.duplicate.MD5Collection
**
*/
package cx.ath.choisnet.util.duplicate;

import cx.ath.choisnet.util.checksum.MD5TreeEntry;
import java.util.Map;
import java.util.Set;

/**
** <p>
** Vue par empreintes d'un ensemble de fichiers
** <br/>
** <br/>
** Cette classe permet d'avoir une vue d'une arboressance de
** fichi�e bas�e sur une liste de nom de r�pertoires et sur
** l'empreinte des fichiers ({@link MD5TreeEntry}) auquel est
** attach�e une liste de noms.
** <br/>
** <br/>
** Voir �galement {@link MD5FileCollection}
** </p>
**
** @author Claude CHOISNET
** @since   3.01.042
** @version 3.01.042
**
** @see MD5FileCollection
** @see DuplicateFileLayer
** @see DuplicateLayer
** @see cx.ath.choisnet.util.checksum.MD5TreeEntry
** @see cx.ath.choisnet.util.duplicate.DuplicateLayer
*/
public interface MD5Collection
    extends
        Comparable<MD5Collection>,
        java.io.Serializable
{

/**
** Retourne un ensemble, non modifiable, de chaines repr�sentant le
** le nom relatif de l'ensemble des dossiers connus de cette collection.
**
** @return un objet Set<String> non null, mais �ventuellement vide.
*/
public Set<String> getFolderFilenames(); // -------------------------------

/**
** Retourne un dictionnaire, non modifiable, des fichiers sous forme d'un
** object Map contenant un couple form� de l'empreinte du fichier {@link MD5TreeEntry}
** et d'un ensemble de cha�ne repr�sentant le nom relatif de chacune des
** instances de ce fichier (il doit y avoir au moins une entr�e).
**
** @return un Map<MD5TreeEntry,? extends Set<String>> non null, et non vide.
*/
public Map<MD5TreeEntry,? extends Set<String>> getEntryFilenames(); // ----

/**
** Retourne l'ensemble des noms de fichier relatifs correspondant �
** l'empreinte donn�e.
**
** @param md5 Empreinte recherch�e
**
** @return un Set<String> si au moins un fichier correspond au MD5TreeEntry
** donn�, retourne null autrement.
*/
public Set<String> getEntryFilenames( MD5TreeEntry md5 ); // --------------

/**
** Retourne le nombre de fichiers contenu dans la collection.
**
** @return un int correspondant au nombre de fichiers (hors dossier) �tant
**         d�fini dans la collection.
*/
public int getEntryCount(); // --------------------------------------------

} // class
