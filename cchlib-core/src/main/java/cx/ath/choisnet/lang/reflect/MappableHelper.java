package cx.ath.choisnet.lang.reflect;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;
import cx.ath.choisnet.ToDo;

/**
 * <p style="border:groove;">
 * <b>Warning:</b>
 * Insofar the code of this class comes from decompiling
 * my own code following the loss of source code, the use
 * of this class must do so under protest until I have
 * check its stability, it could be subject to significant
 * change.
 * </p>
 * Code review in progress.
 *
 * @author Claude CHOISNET
 */
@ToDo
public class MappableHelper
{
    /**
     * TODO: Doc!
     *
     * @author Claude CHOISNET
     */
    public enum Attributes {
        /**
         *  TODO: Doc!
         */
        ALL_PRIMITIVE_TYPE,
        /** TODO: Doc! */
        DO_RECURSIVE,
        /** TODO: Doc! */
        DO_ARRAYS,
        /** TODO: Doc! */
        DO_ITERATOR,
        /** TODO: Doc! */
        DO_ITERABLE,
        /** TODO: Doc! */
        DO_ENUMERATION,
        /** TODO: Doc! */
        DO_PARENT_CLASSES,
        /** TODO: Doc! */
        TRY_PRIVATE_METHODS,
        /** TODO: Doc! */
        TRY_PROTECTED_METHODS
    };

    /** TODO: Doc! */
    public static final EnumSet<Attributes> DEFAULT_ATTRIBUTES = EnumSet.of(
            Attributes.ALL_PRIMITIVE_TYPE,
            Attributes.DO_ARRAYS
            );
    /** TODO: Doc! */
    public static final EnumSet<Attributes> SHOW_ALL = EnumSet.of(
            Attributes.ALL_PRIMITIVE_TYPE
            );

    private final Pattern methodesNamePattern;
    private final Set<Class<?>> returnTypeClasses;
    private final EnumSet<Attributes> attributesSet;
    private final String toStringNullValue;
    private final MessageFormat messageFormatIteratorEntry;
    private final MessageFormat messageFormatIterableEntry;
    private final MessageFormat messageFormatEnumerationEntry;
    private final MessageFormat messageFormatArrayEntry;
    private final MessageFormat messageFormatMethodName;

    /**
     * TODO: Doc!
     *
     * @param factory
     */
    public MappableHelper(final MappableHelperFactory factory)
    {
        toStringNullValue = factory.getStringNullValue();
        messageFormatIteratorEntry = new MessageFormat(factory.getMessageFormatIteratorEntry());
        messageFormatIterableEntry = new MessageFormat(factory.getMessageFormatIterableEntry());
        messageFormatEnumerationEntry = new MessageFormat(factory.getMessageFormatEnumerationEntry());
        messageFormatArrayEntry = new MessageFormat(factory.getMessageFormatArrayEntry());
        messageFormatMethodName = new MessageFormat(factory.getMessageFormatMethodName());
        methodesNamePattern = factory.getMethodesNamePattern();
        returnTypeClasses = factory.getClasses();
        attributesSet = factory.getAttributes();
    }

//    @Deprecated
//    public MappableHelper(MappableHelperFactory factory, Pattern methodesNamePattern, Collection<Class<?>> returnTypeClasses, EnumSet<Attributes> attributesSet)
//    {
//        toStringNullValue = factory.getStringNullValue();
//        messageFormatIteratorEntry = new MessageFormat(factory.getMessageFormatIteratorEntry());
//        messageFormatIterableEntry = new MessageFormat(factory.getMessageFormatIterableEntry());
//        messageFormatEnumerationEntry = new MessageFormat(factory.getMessageFormatEnumerationEntry());
//        messageFormatArrayEntry = new MessageFormat(factory.getMessageFormatArrayEntry());
//        messageFormatMethodName = new MessageFormat(factory.getMessageFormatMethodName());
//
//        this.methodesNamePattern = methodesNamePattern;
//        this.returnTypeClasses = new HashSet<Class<?>>(returnTypeClasses);
//        this.attributesSet = attributesSet;
//    }

    /**
     * Build MappableHelper using default factory
     * @see MappableHelperDefaultFactory
     */
    public MappableHelper()
    {
        this(new MappableHelperDefaultFactory());
    }

    /**
     * Build Map according to specified factory.
     * <p>
     * keys String are sorted.
     * </p>
     */
    public Map<String,String> toMap(final Object object)
    {
        final HashMap<String,String>    hashMap = new HashMap<String,String>();
        final Class<?>                  clazz   = object.getClass();
        final Method[]                  methods = getDeclaredMethods(clazz);

        for( Method method : methods ) {
            if(
                    method.getParameterTypes().length != 0
                    || !methodesNamePattern.matcher(
                            method.getName()
                            ).matches()
                    ) {
                continue;
            }

            Class<?> returnType = method.getReturnType();
            Object   result0;

            if( isMappable( returnType ) ) {
                if( returnType.isArray() ) {
                    Mappable[] result1 = (Mappable[])invoke(object, method, hashMap, Mappable.class);

                    if(result1 == null) {
                        continue;
                    }

                    int     len         = Array.getLength(result1);
                    String  methodName  = method.getName();
                    int     i           = 0;

                    do {
                        if(i >= len) {
                            continue;// label0;
                        }

                        Mappable value = (Mappable)Array.get(result1, i);
                        String   name  = formatIterableEntry(methodName, i, len);

                        if(value == null) {
                            hashMap.put(name, null);

                        }
                        else {
                            MappableHelper.addRec(hashMap, name, value);
                        }
                        i++;
                    } while(true);
                }

                result0 = (Mappable)invoke(object, method, hashMap, Mappable.class);

                if(result0 != null) {
                    MappableHelper.addRec(
                            hashMap,
                            (new StringBuilder()).append(method.getName()).append("().").toString(),
                            ((Mappable) (result0) )
                            );
                }
                continue;
            }

            if(attributesSet.contains(Attributes.DO_ITERATOR) && Iterator.class.isAssignableFrom(returnType)) {
                Iterator<?> iter = (Iterator<?>)invoke(object, method, hashMap, Iterator.class);
                String name = method.getName();

                int i = 0;
                hashMap.put(
                        formatMethodName(name),
                        (new StringBuilder()).append("").append(iter).toString()
                        );
                while( iter.hasNext() ) {
                    hashMap.put(
                            formatIteratorEntry(name, i++, -1),
                            toString( iter.next())
                            );
                }
                continue;
            }

            if( attributesSet.contains( Attributes.DO_ITERABLE ) &&
                    Iterable.class.isAssignableFrom( returnType )
                    ) {
                Iterable<?> iterLst     = (Iterable<?>)invoke(object, method, hashMap, Iterable.class);
                Iterator<?> iter        = iterLst.iterator();
                String      methodName  = method.getName();

                int i = 0;
                hashMap.put(
                        formatMethodName( methodName ),
                        iter == null ? null : iter.toString()
                        );

                while( iter.hasNext() ) {
                    hashMap.put(
                            formatIterableEntry(methodName, i++, -1),
                            toString(iter.next())
                            );
                }
                continue;
            }

            if( attributesSet.contains(Attributes.DO_ENUMERATION) && Enumeration.class.isAssignableFrom(returnType)) {
                Enumeration<?>  enum0       = (Enumeration<?>)invoke(object, method, hashMap, Enumeration.class);
                String          methodName  = method.getName();

                int i = 0;
                hashMap.put(
                        formatMethodName( methodName ),
                        enum0 == null ? null : enum0.toString()
                        );

                if( enum0 != null ) {
                    while( enum0.hasMoreElements() ) {
                        hashMap.put(
                                formatEnumerationEntry(methodName, i++, -1),
                                toString( enum0.nextElement() )
                                );
                    }
                }
                continue;
            }

            if( !shouldEvaluate(returnType) ) {
                continue;
            }

//            System.err.println( "m:" + method );
            Object methodResult = invoke(object, method, hashMap, Object.class);
//            Enumeration<?> enum0 = (Enumeration<?>)invoke(object, method, hashMap, Object.class);
//
//            if(enum0 == null) {
//                continue;
//            }

            if( returnType.isArray() ) {
//                int len =Array.getLength(enum0);
                int len =Array.getLength( methodResult );

                String methodName = method.getName();

                for(int i = 0; i < len; i++) {
//                    Object value = Array.get(enum0, i);
                    Object value = Array.get( methodResult, i );

                    hashMap.put(
                            formatArrayEntry(methodName, i, len),
                            toString(value)
                            );
                }
            }
            else {
                if( methodResult == null ) {
                    hashMap.put(
                            formatMethodName(method.getName()),
                            null
                            );
                    }
                else {
                    hashMap.put(
                            formatMethodName(method.getName()),
                            methodResult.toString()
                            );
                    }
            }
        }

        return new TreeMap<String,String>(hashMap);
    }

    /**
     * TODO: Doc!
     *
     * @param methodeName
     * @param index
     * @param max
     * @return
     */
    protected String formatIterableEntry(String methodeName, int index, int max)
    {
        String params[] = {
            methodeName, Integer.toString(index), Integer.toString(max)
        };

        return messageFormatIterableEntry.format(params);
    }

    /**
     *
     * @param methodeName
     * @param index
     * @param max
     * @return
     */
    protected String formatIteratorEntry(String methodeName, int index, int max)
    {
        String[] params = {
            methodeName,
            Integer.toString(index),
            Integer.toString(max)
        };

        return messageFormatIteratorEntry.format(params);
    }

    /**
     *
     * @param methodeName
     * @param index
     * @param max
     * @return
     */
    protected String formatEnumerationEntry(String methodeName, int index, int max)
    {
        final String[] params = {
            methodeName,
            Integer.toString(index),
            Integer.toString(max)
        };

        return messageFormatEnumerationEntry.format(params);
    }

    /**
     *
     * @param methodeName
     * @param index
     * @param max
     * @return
     */
    protected String formatArrayEntry(String methodeName, int index, int max)
    {
        final String[] params = {
            methodeName,
            Integer.toString(index),
            Integer.toString(max)
        };

        return messageFormatArrayEntry.format(params);
    }

    /**
     *
     * @param methodeName
     * @return
     */
    protected String formatMethodName(String methodeName)
    {
        final String[] params = {
            methodeName
        };

        return messageFormatMethodName.format(params);
    }

    private final Method[] getDeclaredMethods(Class<?> clazz)
    {
        if(!attributesSet.contains(Attributes.DO_PARENT_CLASSES)) {
            return clazz.getDeclaredMethods();
        }

        Set<Method> methodsSet = new HashSet<Method>();
        Method arr0$[] = clazz.getDeclaredMethods();
        int len0$ = arr0$.length;

        for(int i$ = 0; i$ < len0$; i$++) {
            Method m = arr0$[i$];
            methodsSet.add(m);
        }

        for(Class<?> c = clazz.getSuperclass(); c != null; c = c.getSuperclass()) {
            Method arr1$[] = c.getDeclaredMethods();
            int len1$ = arr1$.length;
            for(int i$ = 0; i$ < len1$; i$++) {
                Method m = arr1$[i$];
                methodsSet.add(m);
            }
        }
        Method[] methods = new Method[methodsSet.size()];
        int i = 0;
        for(Iterator<Method> i$ = methodsSet.iterator(); i$.hasNext();) {
            Method m = i$.next();
            methods[i++] = m;
        }

        return methods;
    }

    /**
     *
     * @param clazz
     * @return
     */
    protected final boolean isMappable(Class<?> clazz)
    {
        if(!attributesSet.contains(Attributes.DO_RECURSIVE)) {
            return false;
        }

        if(clazz.isArray()) {
            return Mappable.class.isAssignableFrom(clazz.getComponentType());
        }
        else {
            return Mappable.class.isAssignableFrom(clazz);
        }
    }

    private final boolean shouldEvaluate(Class<?> returnType)
    {
        int modifier = returnType.getModifiers();

        if(Modifier.isPrivate(modifier) && !attributesSet.contains(Attributes.TRY_PRIVATE_METHODS)) {
            return false;
        }
        if(Modifier.isProtected(modifier) && !attributesSet.contains(Attributes.TRY_PROTECTED_METHODS)) {
            return false;
        }
        if(attributesSet.contains(Attributes.ALL_PRIMITIVE_TYPE) && returnType.isPrimitive()) {
            return true;
        }
        if(attributesSet.contains(Attributes.DO_ARRAYS) && returnType.isArray()) {
            return true;
        }

        for(Iterator<Class<?>> i$ = returnTypeClasses.iterator(); i$.hasNext();) {
            Class<?> c = i$.next();

            if(c.isAssignableFrom(returnType)) {
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @param object
     * @return
     */
    public String toString(final Object object)
    {
        if(object == null) {
            return toStringNullValue;
        }
        else {
            return MappableHelper.toString(attributesSet, object);
        }
    }

    private static final void addRec(
            Map<String,String>  hashMap,
            String              methodName,
            Mappable            object
            )
    {
        Set<Map.Entry<String,String>> set = object.toMap().entrySet();

        Map.Entry<String,String> entry;

        for(
                Iterator<Map.Entry<String,String>> i$ = set.iterator();
                i$.hasNext();
                hashMap.put(
                        (new StringBuilder()).append(methodName).append(entry.getKey()).toString(),
                        entry.getValue()
                        )
                     ) {
            entry = i$.next();
        }
    }

    // TODO: Optimizations: remove some StringBuilder
    private static final String toString(
            EnumSet<Attributes> attributesSet,
            Object              object
            )
    {
        if(object.getClass().isArray()) {
            StringBuilder   sb      = new StringBuilder();
            Object[]        array   = (Object[])object;
            boolean first = true;
            Object[] arr$ = array;
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; i$++) {
                Object o = arr$[i$];

                if(first) {
                    first = false;
                }
                else {
                    sb.append(',');
                }
                sb.append( MappableHelper.toString(attributesSet, o) );
            }

            return (new StringBuilder())
                .append('[')
                .append(sb)
                .append(']')
                .toString();
        }

        if(attributesSet.contains(Attributes.DO_ITERATOR)) {
            if(object instanceof java.util.Iterator) {
                Iterator<?> iter = (Iterator<?>)object;
                StringBuilder sb = new StringBuilder();
                boolean first = true;

                for(; iter.hasNext(); sb.append(MappableHelper.toString(attributesSet, iter.next()))) {
                    if(first) {
                        first = false;
                    }
                    else {
                        sb.append(',');
                    }
                }

                return (new StringBuilder())
                    .append("Iterator[")
                    .append(sb)
                    .append(']')
                    .toString();
            }
        }
        else if(attributesSet.contains(Attributes.DO_ENUMERATION) && (object instanceof java.util.Enumeration)) {
            Enumeration<?> enum0 = (Enumeration<?>)object;
            StringBuilder sb = new StringBuilder();
            boolean first = true;

            for(; enum0.hasMoreElements(); sb.append(cx.ath.choisnet.lang.reflect.MappableHelper.toString(attributesSet, enum0.nextElement()))) {
                if(first) {
                    first = false;
                }
                else {
                    sb.append(',');
                }
            }

            return (new StringBuilder())
                .append("Enumeration[")
                .append(sb.toString())
                .append(']')
                .toString();
        }

        return object.toString();
    }

    /**
     *
     * @param object
     * @param method
     * @param hashMap
     * @param resultClass
     * @return
     */
    protected final Object invoke(
            final Object object,
            final Method method,
            final Map<String,String> hashMap,
            final Class<?> resultClass
            )
    {
        Object result = null;

        try {
            result = method.invoke( object, (Object[])null );

            if( result == null ) {
                hashMap.put(
                        formatMethodName( method.getName() ),
                        toStringNullValue
                        );

                return null;
            }

            return resultClass.cast( result );
        }
        catch( ClassCastException improbable ) {
            throw new RuntimeException( (new StringBuilder())
                    .append( "method.getName() - ClassCastException: " )
                    .append( result )
                    .toString(),
                    improbable
                    );
        }
        catch( IllegalArgumentException e ) {
            throw e;
        }
        catch( NullPointerException e ) {
            throw e;
        }
        catch( ExceptionInInitializerError e ) {
            hashMap.put(
                    formatMethodName( method.getName() ),
                    (new StringBuilder())
                            .append( "ExceptionInInitializerError: " )
                            .append( e.getCause() )
                            .toString()
                            );
            return null;
        }
        catch( IllegalAccessException ignore ) {
            return null;
        }
        catch( InvocationTargetException e ) {
            hashMap.put(
                    formatMethodName( method.getName() ),
                    (new StringBuilder())
                            .append( "InvocationTargetException: " )
                            .append( e.getCause() )
                            .toString()
                            );
        }

        return null;
    }

    /**
     *
     * @param factory
     * @param object
     * @return
     */
    public static Map<String,String> toMap(MappableHelperFactory factory, Object object)
    {
        return new MappableHelper(factory).toMap(object);
    }

//    @Deprecated
//    public static Map<String,String> toMap(Object object, Pattern methodesNamePattern, Class<?> returnTypeClasses[], EnumSet<Attributes> attributesSet)
//    {
//        MappableHelper instance = new MappableHelper(new MappableHelperFactory(), methodesNamePattern, Arrays.asList(returnTypeClasses), attributesSet);
//
//        return instance.toMap(object);
//    }

    /**
     *
     */
    public static void toXML(Appendable out, Class<?> clazz, Map<String,String> map)
        throws java.io.IOException
    {
        if(map == null) {
            out.append((new StringBuilder()).append("<class name=\"").append(clazz.getName()).append("\" /><!-- NULL OBJECT -->\n").toString());
        }
        else if(map.size() == 0) {
            out.append((new StringBuilder()).append("<class name=\"").append(clazz.getName()).append("\" /><!-- EMPTY -->\n").toString());
        }
        else {
            out.append((new StringBuilder()).append("<class name=\"").append(clazz.getName()).append("\">\n").toString());
            String name;
            for(
                    Iterator<String> i$ = map.keySet().iterator();
                    i$.hasNext();
                    out.append((new StringBuilder()).append("  <value name=\"").append(name).append("\">").append(map.get(name)).append("</value>\n").toString())
                    ) {
                name = i$.next();
            }

            out.append("</class>\n");
        }
    }

    /**
     *
     * @param out
     * @param clazz
     * @param aMappableObject
     * @throws java.io.IOException
     */
    public static void toXML(Appendable out, Class<?> clazz, Mappable aMappableObject)
        throws java.io.IOException
    {
        MappableHelper.toXML(out, clazz, aMappableObject != null ? aMappableObject.toMap() : null);
    }

    /**
     *
     * @param out
     * @param aMappableObject
     * @throws java.io.IOException
     */
    public static void toXML(Appendable out, Mappable aMappableObject)
        throws java.io.IOException
    {
        MappableHelper.toXML(out, aMappableObject.getClass(), aMappableObject);
    }

    /**
     *
     * @param clazz
     * @param aMappableObject
     * @return
     */
    public static String toXML(Class<?> clazz, Mappable aMappableObject)
    {
        StringBuilder sb = new StringBuilder();

        try {
            MappableHelper.toXML( sb, clazz, aMappableObject);
        }
        catch(java.io.IOException improbable) {
            throw new RuntimeException(improbable);
        }

        return sb.toString();
    }

    /**
     *
     * @param aMappableObject
     * @return
     */
    public static String toXML(Mappable aMappableObject)
    {
        return MappableHelper.toXML(aMappableObject.getClass(), aMappableObject);
    }
}
