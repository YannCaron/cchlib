package com.googlecode.cchlib.apps.editresourcesbundle.load;

import com.googlecode.cchlib.util.EnumHelper;

//NOT public
enum LoadDialogAction
{
    ACTIONCMD_OK_BUTTON,
    ACTIONCMD_CANCEL_BUTTON,
    ACTIONCMD_SELECT_PREFIX,

    ACTION_FT_Properties,
    ACTION_FT_FormattedProperties,
    ACTION_FT_ini,
    ACTION_Change_isUseLeftHasDefault,
    ACTION_Change_ShowLineNumbers,
    ACTION_Change_CUT_LINE_AFTER_HTML_BR,
    ACTION_Change_CUT_LINE_AFTER_HTML_END_P,
    ACTION_Change_CUT_LINE_AFTER_NEW_LINE,
    ACTION_Change_CUT_LINE_BEFORE_HTML_BEGIN_P,
    ACTION_Change_CUT_LINE_BEFORE_HTML_BR;

    public String getActionCommand( final int index )
    {
        if( this == ACTIONCMD_SELECT_PREFIX ) {
            return name() + index;
            }

        throw new IllegalStateException();
    }

    public Integer getIndex( final String value )
    {
        return EnumHelper.getSuffixInteger( this, value );
    }
}