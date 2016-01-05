package com.googlecode.cchlib.apps.duplicatefiles.gui.panels.result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.AbstractListModel;
import javax.swing.ListCellRenderer;
import org.apache.log4j.Logger;
import com.googlecode.cchlib.apps.duplicatefiles.KeyFileState;
import com.googlecode.cchlib.apps.duplicatefiles.KeyFiles;
import com.googlecode.cchlib.util.HashMapSet;
import com.googlecode.cchlib.util.MapSetHelper;

//NOT public
class JPanelResultListModelImpl extends AbstractListModel<KeyFiles> implements JPanelResultListModel
{

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger( JPanelResultListModelImpl.class );

    private Map<String, Set<KeyFileState>> duplicateFiles;
    private SortMode sortMode;
    private SelectFirstMode selectFirstMode;

    private final List<KeyFiles> duplicatesFileCacheList = new ArrayList<>();


    public JPanelResultListModelImpl()
    {
        this.duplicateFiles = new HashMapSet<>();
        this.sortMode = SortMode.FILESIZE;
        this.selectFirstMode = SelectFirstMode.QUICK;

        updateCache();
    }

    @Override
    public void updateCache()
    {
        final int prevLastIndex;

        if( this.duplicatesFileCacheList.size() == 0 ) {
            prevLastIndex = 0;
            }
        else {
            prevLastIndex = this.duplicatesFileCacheList.size() - 1;
            }

        this.duplicatesFileCacheList.clear();

//        for( final Map.Entry<String,Set<KeyFileState>> e : getDuplicateFiles().entrySet() ) {
//            final Collection<KeyFileState> files     = this.selectFirstMode.sort( e.getValue() );
//            final KeyFileState             firstFile = this.selectFirstMode.getFileToDisplay( files );
//
//            duplicatesFileCacheList.add(
//                new DefaultKeyFiles( e.getKey(), files, firstFile )
//                );
//            }
        getDuplicateFiles().entrySet().stream().forEach( e -> {
            final Collection<KeyFileState> files     = this.selectFirstMode.sort( e.getValue() );
            final KeyFileState             firstFile = this.selectFirstMode.getFileToDisplay( files );

            this.duplicatesFileCacheList.add(
                new DefaultKeyFiles( e.getKey(), files, firstFile )
                );
            });

        if( this.sortMode != null ) {
            try {
                Collections.sort( this.duplicatesFileCacheList, this.sortMode );
                }
            catch( final IllegalArgumentException e ) {
                LOGGER.error( "Can not sort : sortMode = " + this.sortMode, e );
                }
            }

        super.fireIntervalRemoved( this, 0, prevLastIndex );
        super.fireContentsChanged( this, 0, this.duplicatesFileCacheList.size() );
    }

    @Override
    public void setDuplicateFiles(
            final Map<String, Set<KeyFileState>> duplicateFiles
            )
    {
        this.duplicateFiles  = duplicateFiles;
    }

    @Override
    public SortMode getSortMode()
    {
        return this.sortMode;
    }

    @Override
    public SelectFirstMode getSelectFirstMode()
    {
        return this.selectFirstMode;
    }

    @Override
    public void setSortMode( final SortMode sortMode )
    {
        this.sortMode = sortMode;
    }

    @Override
    public void setSelectFirstMode( final SelectFirstMode selectFirstMode )
    {
        this.selectFirstMode = selectFirstMode;
    }

    @Override
    public int getSize()
    {
        return this.duplicatesFileCacheList.size();
    }

    @Override
    public KeyFiles getElementAt( final int index )
    {
        return this.duplicatesFileCacheList.get( index );
    }

    @Override
    public Set<KeyFileState> getStateSet( final String key )
    {
        return getDuplicateFiles().get( key );
    }

    @Override
    public Set<Map.Entry<String,Set<KeyFileState>>> getStateEntrySet()
    {
        return this.getDuplicateFiles().entrySet();
    }

    @Override
    public Iterable<KeyFileState> getAllDuplicates()
    {
        //return getDuplicateFiles()::iterator;
        return MapSetHelper.valuesIterable( this.duplicateFiles );
    }

    /**
     * @return the duplicateFiles
     */
    @Override
    public Map<String, Set<KeyFileState>> getDuplicateFiles()
    {
        return this.duplicateFiles;
    }

    private KeyFileStateListModelImpl listModelKeptIntact;
    private ListCellRenderer<KeyFileState> listCellRendererlKeptIntact;
    private KeyFileStateListModelImpl listModelWillBeDeleted;
    private ListCellRenderer<KeyFileState> listCellRendererlWillBeDeleted;
    private String key;

    //not public
    KeyFileStateListModel getKeptIntactListModel()
    {
        if( this.listModelKeptIntact == null ) {
            this.listModelKeptIntact = new KeyFileStateListModelImpl();
            }
        return this.listModelKeptIntact;
    }

    //not public
    ListCellRenderer<? super KeyFileState> getKeptIntactListCellRenderer()
    {
        if( this.listCellRendererlKeptIntact == null ) {
            this.listCellRendererlKeptIntact = new KeyFileStateListCellRenderer();
            }
        return this.listCellRendererlKeptIntact;
    }

    //not public
    KeyFileStateListModel getWillBeDeletedListModel()
    {
        if( this.listModelWillBeDeleted == null ) {
            this.listModelWillBeDeleted = new KeyFileStateListModelImpl();
            }
        return this.listModelWillBeDeleted;
    }

    //not public
    ListCellRenderer<KeyFileState> getWillBeDeletedListCellRenderer()
    {
        if( this.listCellRendererlWillBeDeleted == null ) {
            this.listCellRendererlWillBeDeleted = new KeyFileStateListCellRenderer();
            }
        return this.listCellRendererlWillBeDeleted;
    }

    @Override
    public void clearKeepDelete()
    {
        this.listModelKeptIntact.clear();
        this.listModelWillBeDeleted.clear();
    }

    @Override
    public void setKeepDelete(
        final String            key,
        final Set<KeyFileState> s
        )
    {
        if( LOGGER.isTraceEnabled() ) {
            LOGGER.trace( "setKeepDelete: " + s );
            }
        this.key = key;

        final SortedSet<KeyFileState> ss = new TreeSet<>();

        ss.addAll( s );

        this.listModelWillBeDeleted.clear();
        this.listModelKeptIntact.clear();

        for( final KeyFileState sf : ss ) {
            if( sf.isSelectedToDelete() ) {
                this.listModelWillBeDeleted.private_add( sf );
                  }
            else {
                this.listModelKeptIntact.private_add( sf );
                  }
              }

        this.listModelWillBeDeleted.private_fireAddedAll();
        this.listModelKeptIntact.private_fireAddedAll();

        ss.clear();
    }

    @Override
    public void clearSelected()
    {
        //
        // Update global list
        //
        for( final KeyFileState kfs : getAllDuplicates() ) {
            kfs.setSelectedToDelete( false );
            }

        //
        // Update current models
        //
        final Set<KeyFileState> kfs = getDuplicateFiles().get( this.key );

        if( kfs != null ) {
            setKeepDelete( this.key, kfs );
            }
        // else no values

        // TODO: update display ??
    }

    @Override
    public void refreshList()
    {
        final Iterator<Map.Entry<String, Set<KeyFileState>>> mainIterator = getDuplicateFiles().entrySet().iterator();

        int duplicatesCurrentIndex  = 0;
        int index0 = -1;
        int index1 = -1;

        LOGGER.info( "refreshList() begin : duplicateFiles.size() = " + getDuplicateFiles().size() );

        while( mainIterator.hasNext() ) {
            final Map.Entry<String,Set<KeyFileState>> entry       = mainIterator.next();
            final Set<KeyFileState>                   kfsSet      = entry.getValue();
            final Iterator<KeyFileState>              kfsIterator = kfsSet.iterator();

            while( kfsIterator.hasNext() ) {
                final KeyFileState kfs = kfsIterator.next();

                if( ! kfs.isFileExists() ) {
                    // File no more exist (delete by an other process)
                    LOGGER.info( "refreshList() : File \"" + kfs + "\" no more exist" );
                    kfsIterator.remove();
                    }
                }

            if( LOGGER.isTraceEnabled() ) {
                LOGGER.trace( "refreshList() : kfsSet.size() = " + kfsSet.size() );
            }

            if( kfsSet.size() < 2 ) { // $codepro.audit.disable numericLiterals
                // No more duplicate here !
                mainIterator.remove();

                if( index0 < 0 ) {
                    index0 = duplicatesCurrentIndex;
                    }
                index1 = duplicatesCurrentIndex;
                }

            duplicatesCurrentIndex++;
            }

        LOGGER.info( "refreshList() end : duplicateFiles.size() = " + getDuplicateFiles().size() + " * index0=" + index0 + " index1=" + index1 );

        if( index0 >= 0 ) {
            updateCache();
            super.fireContentsChanged( this, index0, index1 );
            }
    }
}
