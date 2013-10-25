/*
** ------------------------------------------------------------------------
** Nom           : cx/ath/choisnet/io/PatternFileFilter.java
** Description   :
** Encodage      : ANSI
**
**  3.01.031 2006.04.25 Claude CHOISNET - Version initial
**  3.02.042 2007.01.08 Claude CHOISNET
**                      Ajout de: asFileFilter()
**                      Ajout de: asFilenameFilter()
** ------------------------------------------------------------------------
**
** cx.ath.choisnet.io.PatternFileFilter
**
*/
package cx.ath.choisnet.io;

import java.io.File;
import java.util.regex.Pattern;

/**
** <p>
** Permet de construire un {@link java.io.FileFilter} en utilisant
** les expressions r�guli�res.
** </p>
** La vue {@link java.io.FileFilter} permet de faire une recherche sur le
** nom complet du fichier ({@link #accept(File)}), alors que la vue
** {@link java.io.FilenameFilter} permet de faire une recherche uniquement
** bas�e sur le nom du fichier ({@link #accept(File,String)}).
**
** @author Claude CHOISNET
** @since   3.01.031
** @version 3.02.042
**
** @see FileFilterHelper
** @see Pattern
** @see File#getPath()
*/
public class PatternFileFilter
    implements
        java.io.FileFilter,
        java.io.FilenameFilter,
        java.io.Serializable
{
/** */
private static final long serialVersionUID = 1L;

/** */
private Pattern pattern;

/**
**
*/
public PatternFileFilter( // ----------------------------------------------
    final Pattern pattern
    )
{
 this.pattern = pattern;
}

/**
**
** @see Pattern#compile(String)
*/
public PatternFileFilter( // ----------------------------------------------
    final String regex
    )
{
 this( Pattern.compile( regex ) );
}

/**
** Retourne true si le chemin complet du fichier correspont � l'expression
** r�guli�re.
**
** @param file  Fichier � tester
**
** @see java.io.FileFilter#accept(File)
** @see File#getPath()
*/
public boolean accept( final File file ) // -------------------------------
{
 return this.pattern.matcher( file.getPath() ).matches();
}

/**
** Retourne true si le nom du fichier correspont � l'expression
** r�guli�re.
**
** @param dir   Objet File du dossier courant (ignor�)
** @param name  Nom du fichier � tester.
**
** @see java.io.FilenameFilter#accept(File,String)
*/
public boolean accept( final File dir, final String name ) // -------------
{
 return this.pattern.matcher( name ).matches();
}

/**
** Retourne un object {@link java.io.FileFilter}, cette m�thode est
** destin�e � lever les ambiguit�es.
**
** @see #asFilenameFilter()
**
** @since 3.02.042
*/
public java.io.FileFilter asFileFilter() // -------------------------------
{
 return this;
}

/**
** Retourne un object {@link java.io.FilenameFilter}, cette m�thode est
** destin�e � lever les ambiguit�es.
**
** @see #asFileFilter()
**
** @since 3.02.042
*/
public java.io.FilenameFilter asFilenameFilter() // -----------------------
{
 return this;
}

} // class

