// $codepro.audit.disable numericLiterals, importOrder
package com.googlecode.cchlib.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.junit.Test;

public class SortedListTest
{
    private static final Logger LOGGER = Logger.getLogger( SortedListTest.class );

    @Test
    public void testAddT()
    {
        final SortedList<MyType> test1 = new SortedList<MyType>();
        final SortedList<MyType> test2 = new SortedList<MyType>( new MyTypeComparator() );

        test1.add( new MyType( 9 ) );
        test2.add( new MyType( 9 ) );

        assertEquals( 1, test1.size() );
        assertEquals( 1, test2.size() );

        add(test1, 8, 5, 6, 2, 1, 10, 7 );
        add(test2, 8, 5, 6, 2, 1, 10, 7 );

        assertEquals( 8, test1.size() );
        assertEquals( 8, test2.size() );

        {
            final Iterator<MyType> test1Iter = test1.iterator();
            final Iterator<MyType> test2Iter = test2.iterator();

            int prev = Integer.MIN_VALUE;

            while( test1Iter.hasNext() && test2Iter.hasNext() ) {
                final int v1 = test1Iter.next().getContent();
                final int v2 = test2Iter.next().getContent();

                LOGGER.info( "entry: " + v1 );
                assertEquals( v1, v2 );
                assertTrue( v1 > prev );
                prev = v1;
                }

            assertFalse( test1Iter.hasNext() );
            assertFalse( test2Iter.hasNext() );
        }

        final boolean exist = test1.containsElement( new MyType( 8 ) );
        assertTrue( exist );

        final boolean notExist = test1.containsElement( new MyType( 3 ) );
        assertFalse( notExist );

        LOGGER.info( "done" );
    }

    private void add(
        final SortedList<MyType> l,
        final int...             values
        )
    {
        for( final int v : values ) {
            l.add( new MyType( v ) );
            }
    }

}
