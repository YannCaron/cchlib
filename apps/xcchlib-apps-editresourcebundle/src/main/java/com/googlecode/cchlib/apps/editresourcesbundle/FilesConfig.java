package com.googlecode.cchlib.apps.editresourcesbundle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Properties;
import java.util.Set;
import com.googlecode.cchlib.apps.editresourcesbundle.files.CustomProperties;
import com.googlecode.cchlib.apps.editresourcesbundle.files.DefaultCustomProperties;
import com.googlecode.cchlib.apps.editresourcesbundle.files.FileObject;
import com.googlecode.cchlib.apps.editresourcesbundle.files.FormattedCustomProperties;
import com.googlecode.cchlib.apps.editresourcesbundle.prefs.Preferences;
import cx.ath.choisnet.util.FormattedProperties;

public class FilesConfig implements Serializable
{
    @FunctionalInterface
    private interface Loader {
        void load( FilesConfig filesConfig ) throws IOException;
    }

    public enum FileType
    {
        PROPERTIES( filesConfig -> filesConfig.privateLoadProperties()),
        FORMATTED_PROPERTIES( filesConfig -> filesConfig.privateLoadFormattedProperties()),
        ;

        private Loader loader;

        private FileType( final Loader loader )
        {
            this.loader = loader;
        }

        public void loadSwitchFileType( final FilesConfig filesConfig )
            throws IOException
        {
            this.loader.load( filesConfig );
        }
    }

    private static final long serialVersionUID = 3L;

    private FileObject[] fileObjects;
    private FileType fileType
          = FileType.FORMATTED_PROPERTIES;
    private Set<FormattedProperties.Store> formattedPropertiesStore
          = EnumSet.allOf( FormattedProperties.Store.class );
    private boolean useLeftHasDefault
          = false;
    private boolean showLineNumbers
          = false;

    private int numberOfFiles;

    /**
     * Build default {@link FilesConfig} (no file selected yet)
     *
     * @param preferences Current {@link Preferences}
     */
    public FilesConfig( final Preferences preferences )
    {
        setNumberOfFiles( preferences.getNumberOfFiles() );
     }

    /**
     * Build {@link FilesConfig} based on a existing one
     *
     * @param filesConfig {@link FilesConfig} to copy.
     */
    public FilesConfig( final FilesConfig filesConfig )
    {
        setNumberOfFiles( filesConfig.numberOfFiles );

        this.fileObjects              = Arrays.copyOf( filesConfig.fileObjects, this.numberOfFiles );
        this.fileType                 = filesConfig.fileType;
        this.formattedPropertiesStore = filesConfig.formattedPropertiesStore;
        this.useLeftHasDefault        = filesConfig.useLeftHasDefault;
        this.showLineNumbers          = filesConfig.showLineNumbers;
    }

    public FilesConfig setNumberOfFiles( final int numberOfFiles )
    {
        if( numberOfFiles < 1 ) {
            throw new IllegalArgumentException( "Should be at least one file: numberOfFiles=" + numberOfFiles );
        }

        this.numberOfFiles = numberOfFiles;

        if( this.fileObjects == null ) {
            this.fileObjects   = new FileObject[ numberOfFiles ];
            }
        else {
            final FileObject[] oldArray = this.fileObjects;

            this.fileObjects = new FileObject[ numberOfFiles ];

            final int min = (numberOfFiles > oldArray.length) ? oldArray.length : numberOfFiles;

            System.arraycopy( oldArray, 0, this.fileObjects, 0, min );
            }

        return this;
    }

    public int getNumberOfFiles()
    {
        return this.numberOfFiles ;
    }

    public void clear()
    {
        this.fileObjects = new FileObject[ this.numberOfFiles ];
    }

    /**
     * Returns the asked FileObject
     *
     * @param index
     *            Index for the {@link CustomProperties}
     * @return the asked FileObject
     */
    public FileObject getFileObject( final int index )
    {
        return this.fileObjects[ index ];
    }

    public FilesConfig setFileObject(
        final FileObject fileObject,
        final int        index
        )
    {
        this.fileObjects[ index ] = fileObject;

        return this;
    }

    /**
     * Returns the asked {@link CustomProperties}
     *
     * @param index
     *            Index for the {@link CustomProperties}
     * @return the asked {@link CustomProperties}
     */
    public CustomProperties getCustomProperties( final int index )
    {
        final FileObject fileObject = this.fileObjects[ index ];

        if( fileObject == null ) {
            throw new IllegalStateException( "value not define for index=" + index );
        }
        return fileObject.getCustomProperties();
    }

    /**
     * @return the leftFileObject
     */
    public FileObject getLeftFileObject()
    {
        return getFileObject( 0 );
     }

    /**
     * @return the fileType
     */
    public FileType getFileType()
    {
        return this.fileType;
    }

    /**
     * @param fileType the fileType to set
     */
    public void setFileType( final FileType fileType )
    {
        this.fileType = fileType;
    }

    public boolean isUseLeftHasDefault()
    {
        return this.useLeftHasDefault;
    }

    /**
     * @param useLeftHasDefault the useLeftHasDefault to set
     */
    public void setUseLeftHasDefault( final boolean useLeftHasDefault )
    {
        this.useLeftHasDefault = useLeftHasDefault;
    }

    public Set<FormattedProperties.Store> getFormattedPropertiesStore()
    {
        return this.formattedPropertiesStore;
    }

    public void setFormattedPropertiesStore(
        final Set<FormattedProperties.Store> storeOptions
        )
    {
        this.formattedPropertiesStore = storeOptions;
    }

    public void add( final FormattedProperties.Store storeOption )
    {
        this.formattedPropertiesStore.add( storeOption );
    }

    public void remove( final FormattedProperties.Store storeOption )
    {
        this.formattedPropertiesStore.remove( storeOption );
    }

    public boolean isShowLineNumbers()
    {
        return this.showLineNumbers;
    }

    public void setShowLineNumbers(final boolean showLineNumbers)
    {
        this.showLineNumbers = showLineNumbers;
    }

    /**
     * @return true if all (both?) files are defined and exists
     */
    public boolean isFilesExists()
    {
        for( final FileObject entry : this.fileObjects ) {
            if( entry == null ) {
                return false;
                }
            if( ! entry.getFile().isFile() ) {
                return false;
                }
            }

        return true;
    }

    public void load() throws IOException
    {
        this.fileType.loadSwitchFileType( this );
    }

    @SuppressWarnings({"squid:RedundantThrowsDeclarationCheck"})
    private void privateLoadProperties() throws FileNotFoundException, IOException
    {
        privateLoadProperties( null, 0 );

        final Properties def = isUseLeftHasDefault() ?
            DefaultCustomProperties.class.cast(
                this.fileObjects[ 0 ].getCustomProperties()
                ).getProperties()
            :
            null;

        for( int i = 1; i<this.numberOfFiles; i++ ) {
            privateLoadProperties( def, i);
            }
    }

    @SuppressWarnings({"squid:RedundantThrowsDeclarationCheck"})
    private void privateLoadProperties(
            final Properties defaults,
            final int        index
            )
            throws  FileNotFoundException,
                    IOException
    {
        /*final CustomProperties cprop = */
        new DefaultCustomProperties( this.getFileObject( index ), defaults );
    }

    @SuppressWarnings({"squid:RedundantThrowsDeclarationCheck"})
    private void privateLoadFormattedProperties() throws FileNotFoundException, IOException
    {
        privateLoadFormattedProperties( null, 0 );

        final FormattedProperties def  =
            isUseLeftHasDefault() ?
                FormattedCustomProperties.class.cast(
                    this.fileObjects[ 0 ].getCustomProperties()
                    ).getFormattedProperties()
                :
                null;

        for( int i = 1; i<this.numberOfFiles; i++ ) {
            privateLoadFormattedProperties( def, i );
            }
    }

    @SuppressWarnings({"squid:RedundantThrowsDeclarationCheck"})
    private void privateLoadFormattedProperties(
        final FormattedProperties defaults,
        final int                 index
        )
        throws  FileNotFoundException,
                IOException
    {
        final FormattedCustomProperties cprop = new FormattedCustomProperties(
                this.getFileObject( index ),
                defaults,
                this.formattedPropertiesStore
                );
        cprop.load();
    }

    //Serializable
    private void writeObject(final java.io.ObjectOutputStream out)
        throws IOException
    {
        out.defaultWriteObject();
    }

    //Serializable
    private void readObject(final java.io.ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();

        // Restore transient fields !
        //load();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + Arrays.hashCode( this.fileObjects );
        result = (prime * result)
                + ((this.fileType == null) ? 0 : this.fileType.hashCode());
        result = (prime
                * result)
                + ((this.formattedPropertiesStore == null) ? 0
                        : this.formattedPropertiesStore.hashCode());
        result = (prime * result) + this.numberOfFiles;
        result = (prime * result) + (this.showLineNumbers ? 1231 : 1237); // $codepro.audit.disable numericLiterals
        result = (prime * result) + (this.useLeftHasDefault ? 1231 : 1237); // $codepro.audit.disable numericLiterals
        return result;
    }

    @Override
    public boolean equals( final Object obj ) // $codepro.audit.disable cyclomaticComplexity
    {
        if( this == obj ) {
            return true;
        }
        if( obj == null ) {
            return false;
        }
        if( !(obj instanceof FilesConfig) ) {
            return false;
        }
        final FilesConfig other = (FilesConfig)obj;
        if( !Arrays.equals( this.fileObjects, other.fileObjects ) ) {
            return false;
        }
        if( this.fileType != other.fileType ) {
            return false;
        }
        if( this.formattedPropertiesStore == null ) {
            if( other.formattedPropertiesStore != null ) {
                return false;
            }
        } else if( !this.formattedPropertiesStore
                .equals( other.formattedPropertiesStore ) ) {
            return false;
        }
        if( this.numberOfFiles != other.numberOfFiles ) {
            return false;
        }
        if( this.showLineNumbers != other.showLineNumbers ) {
            return false;
        }
        if( this.useLeftHasDefault != other.useLeftHasDefault ) {
            return false;
        }
        return true;
    }
}
