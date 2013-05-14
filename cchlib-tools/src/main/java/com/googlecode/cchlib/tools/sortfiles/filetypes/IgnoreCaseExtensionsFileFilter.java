package com.googlecode.cchlib.tools.sortfiles.filetypes;

import java.io.File;

public class IgnoreCaseExtensionsFileFilter extends AbstractExtensionsFileFilter implements XFileFilter
{
    private static final long serialVersionUID = 1L;

    public IgnoreCaseExtensionsFileFilter( String extension, String...others )
    {
        super( extension, others );
//        
//        String[] endsWiths = getEndsWiths();
//        
//        for( int i = 0; i<getEndsWiths().length; i++ ) {
//            endsWiths[ i ] = endsWiths[ i ].toLowerCase();
//            }
    }

    @Override
    public boolean accept( File file )
    {
        if( file.isFile() ) {
            final String name = file.getName().toLowerCase();
            
            for( String endsWith : getEndsWiths() ) {
                if( name.endsWith( endsWith ) ) {
                    return true;
                    }
                }
            }

        return false;
    }

    @Override
    protected String customiseExtension( String extension )
    {
        return "." + extension.toLowerCase();
    }
}