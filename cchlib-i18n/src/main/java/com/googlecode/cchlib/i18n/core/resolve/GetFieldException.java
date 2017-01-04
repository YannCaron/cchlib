package com.googlecode.cchlib.i18n.core.resolve;

public class GetFieldException extends I18nFieldException 
{
    private static final long serialVersionUID = 1L;

    public GetFieldException( String message, Throwable cause )
    {
        super( message, cause );
    }
    
    public GetFieldException( Throwable cause )
    {
        super( cause );
    }
}
