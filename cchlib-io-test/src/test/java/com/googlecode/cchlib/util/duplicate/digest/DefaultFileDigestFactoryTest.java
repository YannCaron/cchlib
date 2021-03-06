package com.googlecode.cchlib.util.duplicate.digest;

import static org.fest.assertions.api.Assertions.assertThat;
import java.security.NoSuchAlgorithmException;
import org.junit.Test;

/**
 * @since 4.2
 */
public class DefaultFileDigestFactoryTest {

    @Test
    public void testDefaultFileDigestFactory() throws NoSuchAlgorithmException {
        final FileDigestFactory factory = new DefaultFileDigestFactory();
        final FileDigest instance = factory.newInstance();

        assertThat( instance ).isNotNull();
        assertThat( instance.getAlgorithm() ).isEqualTo( "MD5" );
        assertThat( instance.getBufferSize() ).isEqualTo( 8192 );
    }

    @Test
    public void testFileDigestFactory_SHA256() throws NoSuchAlgorithmException {
        final FileDigestFactory factory = new DefaultFileDigestFactory( MessageDigestAlgorithms.SHA_256, 4096 );
        final FileDigest instance = factory.newInstance();

        assertThat( instance ).isNotNull();
        assertThat( instance.getAlgorithm() ).isEqualTo( "SHA-256" );
        assertThat( instance.getBufferSize() ).isEqualTo( 4096 );
    }

    @Test
    public void testFileDigestFactory_SHA512() throws NoSuchAlgorithmException {
        final FileDigestFactory factory = new DefaultFileDigestFactory( "SHA-512", 2048 );
        final FileDigest instance = factory.newInstance();

        assertThat( instance ).isNotNull();
        assertThat( instance.getAlgorithm() ).isEqualTo( "SHA-512" );
        assertThat( instance.getBufferSize() ).isEqualTo( 2048 );
    }
}
