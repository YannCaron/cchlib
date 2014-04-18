package com.googlecode.cchlib.apps.duplicatefiles.gui;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import com.googlecode.cchlib.swing.combobox.XComboBoxPattern;

/**
 *
 */
public class XComboBoxPatternBuilder implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Color        errorColor    = null;
    private List<String> regExpList    = new ArrayList<String>();

    /**
     *
     */
    public XComboBoxPatternBuilder()
    {
        // empty
    }

    /**
     *
     * @param errorColor
     * @return current object for chaining initialization
     */
    public XComboBoxPatternBuilder setErrorColor(
        final Color errorColor
        )
    {
        this.errorColor = errorColor;
        return this;
    }

    /**
     *
     * @param regExps
     * @return current object for chaining initialization
     */
    public XComboBoxPatternBuilder add(
        final String...regExps
        )
    {
        for( String s : regExps ) {
            this.regExpList.add( s );
            }

        return this;
    }

    /**
     *
     * @param regExpCollection
     * @return current object for chaining initialization
     */
    public XComboBoxPatternBuilder add(
        final Collection<String> regExpCollection
        )
    {
        this.regExpList.addAll( regExpCollection );
        return this;
    }

    /**
     *
     * @param pattern
     * @return current object for chaining initialization
     */
    public XComboBoxPatternBuilder add( final Pattern pattern )
    {
        final String regExp = pattern.pattern();
        this.regExpList.add( regExp );
        return this;
    }

    /**
     *
     * @param patternCollection
     * @return current object for chaining initialization
     */
    public XComboBoxPatternBuilder addPatternCollection(
        final Collection<Pattern> patternCollection
        )
    {
        for( Pattern p : patternCollection ) {
            this.regExpList.add( p.pattern() );
            }
        return this;
    }

    /**
     * Returns a new XComboBoxPattern according to
     * giving configuration.
     * @return new XComboBoxPattern
     */
    public XComboBoxPattern createXComboBoxPattern()
    {
        final XComboBoxPattern xcbp = new XComboBoxPattern();

        if( errorColor != null ) {
            xcbp.setErrorBackGroundColor( errorColor );
            }

        for( String regExp : regExpList ) {
            xcbp.addItem( regExp );
            }

        return xcbp;
    }
}