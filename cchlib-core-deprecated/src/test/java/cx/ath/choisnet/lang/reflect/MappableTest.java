package cx.ath.choisnet.lang.reflect;

import java.util.Map;
import org.junit.Test;

@Deprecated
public class MappableTest
{
    private MappableBuilder mbDefault = MappableBuilder.createMappableBuilder();
    private MappableBuilder mb1 = new MappableBuilder(
            new DefaultMappableBuilderFactory()
                .setMethodesNamePattern( "(is|get|allows).*" )
                .add( MappableBuilder.CLASSES_STANDARDS_TYPES )
                .add( MappableBuilder.MAPPABLE_ITEM_SHOW_ALL )
                );
    private MappableBuilder mb2 = new MappableBuilder(
            new DefaultMappableBuilderFactory()
                .setMethodesNamePattern( ".*" )
                .add( MappableBuilder.CLASSES_SHOW_ALL )
                .add( MappableBuilder.MAPPABLE_ITEM_SHOW_ALL )
                );

    @Test
    public void testX()
    {
        final StringBuilder object = new StringBuilder( "test" );

        {
            Map<String, String> map = mbDefault.toMap( object );
            System.out.println( "map = " + map );
        }
        {
            Map<String, String> map = mb1.toMap( object );
            System.out.println( "map = " + map );
        }
        {
            Map<String, String> map = mb2.toMap( object );
            System.out.println( "map = " + map );
        }
    }
}