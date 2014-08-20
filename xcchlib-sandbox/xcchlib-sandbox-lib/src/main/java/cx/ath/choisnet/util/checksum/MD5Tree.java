/*
** -----------------------------------------------------------------------
** Nom           : cx/ath/choisnet/util/checksum/MD5Tree.java
** Description   :
** Encodage      : ANSI
**
**  2.01.021 2005.10.25 Claude CHOISNET - Version initiale
**  2.02.004 2005.12.02 Claude CHOISNET
**                      Ajout d'un test (anti NULL) dans addFolder()
**                      Ajout de l'interface AppendFileListener et
**                      prise en compte
**  2.02.011 2005.12.15 Claude CHOISNET
**                      Bug min: fermeture du flux d'acces aux fichiers.
**  2.02.037 2005.10.25 Claude CHOISNET
**                      Ajout de l'interface Loader et externalisation
**                      du chargeur.
**                      Le buffer par defaut passe de 8ko e 1Mo
**  3.00.003 2006.02.14 Claude CHOISNET
**                      Les classes statiques sont egalement Serializable
**  3.01.003 2006.03.01 Claude CHOISNET
**                      Les inner-class NullExceptionHandler et
**                      NullAppendFileListener sont maintenant static
** -----------------------------------------------------------------------
**
** cx.ath.choisnet.util.checksum.MD5Tree
**
*/
package cx.ath.choisnet.util.checksum;

import cx.ath.choisnet.util.EmptyIterator;
import java.io.File;

/**
** <p>
** Conteneur pour une arboressance d'empreintes : "Message Digest"
** </p>
**
** <p>
** Cette classe permet d'avoir une vue par empreintes d'une ou de plusieurs
** arboressances.
** </p>
**
** @author Claude CHOISNET
** @since   2.01.021
** @version 3.01.003
**
** @see cx.ath.choisnet.util.duplicate.MD5Collection
*/
public class MD5Tree
    implements java.io.Serializable
{
/** serialVersionUID */
private static final long serialVersionUID = 2L;

/** Taille par defaut du buffer */
private static final int DEFAULT_BUFFER_SIZE = 1048576; // 1 Mo

/** Noeud racine */
private MD5TreeNode rootNode;

    /**
    ** Gestion des erreurs lors de l'ajout de nouvelles entrees.
    */
    public interface ExceptionHandler
    {
        /** Methode appellee lors d'un probleme d'E/S */
        public void handleIOException( File file, java.io.IOException e )
            throws java.io.IOException;

//      public void handleException( java.io.Exception e )
//          throws java.io.Exception;
    }

    /**
    ** Interface e utiliser pour suivre les traitements.
    */
    public interface AppendFileListener
    {
        /**
        ** Methode appellee lors du traitement d'un fichier
        **
        ** @param file Object file correspondant au fichier en cours de
        **             traitement.
        */
        public void appendFile( File file ); // - - - - - - - - - - - - - -

        /**
        ** Methode appellee lors du traitement d'un dossier
        **
        ** @param file Object file correspondant au dossier en cours de
        **             traitement.
        */
        public void appendFolder( File file ); // - - - - - - - - - - - - -
    }

    /**
    ** Interface devant etre implementee par les classes prendant
    ** en charge l'initialisation d'un object {@link MD5Tree}
    **
    ** @since   2.02.037
    */
    public interface Loader
    {
        /**
        ** Creation d'une nouvelle instance prete e etre utilisee.
        **
        ** @param appendFileListener    Ecouteur
        ** @param errorHandler          Gestionnaire d'erreur.
        ** @param bufferSize            Taille du buffer utilise pour
        **                              le calcul MD5.
        */
        public Loader newInstance( // - - - - - - - - - - - - - - - - - - -
            AppendFileListener  appendFileListener,
            ExceptionHandler    errorHandler,
            int                 bufferSize
            );

        /**
        ** Ajout du dossier principal (cette methode peut eventuellement
        ** etre appellee recursivement).
        **
        ** @param node      Noeud courant.
        ** @param folder    Objet File correspondant au dossier e analyser
        **                  (recursivement).
        */
        public void addFolder( // - - - - - - - - - - - - - - - - - - - - -
            MD5TreeNode node,
            final File  folder
            )
            throws java.io.IOException;

    }


/**
** Construction d'un MD5Tree
*/
public MD5Tree() // -------------------------------------------------------
{
 this.rootNode = null;
}

/**
** Chargement d'un MD5Tree e partir d'un dossier donne
**
** @return un reference vers l'objet lui-meme.
*/
public MD5Tree load( // ---------------------------------------------------
    final File                  rootFolderFile,
    final Loader                loader,
    final AppendFileListener    appendFileListener,
    final ExceptionHandler      errorHandler,
    final int                   bufferSize
    )
    throws java.io.IOException
{
 this.rootNode = new MD5TreeNode( null, rootFolderFile );

 loader.newInstance(
    appendFileListener,
    errorHandler,
    bufferSize
    ).addFolder( this.rootNode, rootFolderFile );

 return this;
}

/**
** Chargement d'un MD5Tree e partir d'un dossier donne
**
** @return un reference vers l'objet lui-meme.
*/
public MD5Tree load( // ---------------------------------------------------
    final File                  rootFolderFile,
    final AppendFileListener    appendFileListener,
    final ExceptionHandler      errorHandler,
    final int                   bufferSize
    )
    throws java.io.IOException
{
 this.rootNode = new MD5TreeNode( null, rootFolderFile );

 return load(
        rootFolderFile,
        new MD5TreeNIOLoader(),
        appendFileListener,
        errorHandler,
        bufferSize
        );
}

/**
** Chargement d'un MD5Tree e partir d'un dossier donne
**
** @return un reference vers l'objet lui-meme.
*/
public MD5Tree load( // ---------------------------------------------------
    final File                  rootFolderFile,
    final AppendFileListener    appendFileListener,
    final ExceptionHandler      errorHandler
    )
    throws java.io.IOException
{
 return load( rootFolderFile, appendFileListener, errorHandler, DEFAULT_BUFFER_SIZE );
}

/**
** Chargement d'un MD5Tree e partir d'un dossier donne
**
** @return un reference vers l'objet lui-meme.
*/
public MD5Tree load( // ---------------------------------------------------
    final File              rootFolderFile,
    final ExceptionHandler  errorHandler
    )
    throws java.io.IOException
{
 return load(
            rootFolderFile,
            new NullAppendFileListener(),
            errorHandler,
            DEFAULT_BUFFER_SIZE
            );
}

/**
**
*/
public MD5TreeNode getRootNode() // ---------------------------------------
{
 return this.rootNode;
}

/**
**
*/
public Iterable<MD5TreeNode> nodes() // -----------------------------------
{
 if( this.rootNode == null ) {
    return new EmptyIterator<MD5TreeNode>();
    }

 return this.rootNode.nodes();
}

    /**
    ** Cette classe se contente de reproduire les exceptions.
    */
    final static class NullExceptionHandler
        implements ExceptionHandler, java.io.Serializable
    {
        /** serialVersionUID */
        private static final long serialVersionUID = 1L;

        /**
        ** Se contente de reproduire l'exception.
        */
        @Override
        public void handleIOException( File file, java.io.IOException e )
            throws java.io.IOException
        {
            throw e;
        }
    }

    /**
    ** Cette classe ne fait rien lors de l'ajout d'un fichier ou d'un
    ** dossier.
    */
    public static class NullAppendFileListener
        implements AppendFileListener, java.io.Serializable

    {
        /** serialVersionUID */
        private static final long serialVersionUID = 1L;

        /**
        ** Ne fait rien
        */
        @Override
        public void appendFile( File file )
        {
        }

        /**
        ** Ne fait rien
        */
        @Override
        public void appendFolder( File file )
        {
        }

    }

} // class

