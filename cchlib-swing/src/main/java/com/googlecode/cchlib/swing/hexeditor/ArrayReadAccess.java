package com.googlecode.cchlib.swing.hexeditor;

/**
 * TODOC
 */
public interface ArrayReadAccess
{
	/**
	 * TODOC
	 * @return TODOC
	 */
    public int getLength();
    
    /**
     * TODOC
     * @param index
     * @return TODOC
     */
    public byte getByte(int index);
    
    /**
     * TODOC
     * @param index
     * @return TODOC
     */
    public char getChar(int index);
}