package cx.ath.choisnet.lang.reflect;

import java.io.File;
import java.net.URL;
import java.util.Map;
import cx.ath.choisnet.ToDo;

/**
 * <p style="border:groove;">
 * <b>Warning:</b>
 * Insofar the code of this class comes from decompiling
 * my own code following the loss of source code, the use
 * of this class must do so under protest until I have
 * check its stability, it could be subject to significant
 * change.
 * <br/>
 * <br/>
 * <b>Attention:</b>
 * Dans la mesure o� le code de cette classe est issue de
 * la d�compilation de mon propre code, suite � la perte
 * du code source, l'utilisation de cette classe doit ce
 * faire sous toute r�serve tant que je n'ai pas v�rifier
 * sa stabilit�, elle est donc sujette � des changements 
 * importants.
 * </p>
 *
 * @author Claude CHOISNET
 *
 */
@ToDo(action=ToDo.Action.DOCUMENTATION)
public abstract class AbstractMappable
    implements Mappable
{

    public AbstractMappable()
    {

    }

    public Map<String,String> toMap()
    {
        MappableHelper mappableHelper = new MappableHelper(
                (new MappableHelperFactory())
                    .setMethodesNamePattern("(get|is).*")
                    .addClasses(new Class<?>[] { String.class, File.class, URL.class })
                    .addAttribute( MappableHelper.Attributes.ALL_PRIMITIVE_TYPE )
                    );

        return mappableHelper.toMap(this);
    }
}
