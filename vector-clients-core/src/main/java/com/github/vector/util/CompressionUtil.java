package com.github.vector.util;

import java.io.ByteArrayOutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

/**
 * pre-embedding(context)
 */
public class CompressionUtil {
    public static byte[] compress(final String context) {
        try(ByteArrayOutputStream out = new ByteArrayOutputStream();
            DeflaterOutputStream deflater = new DeflaterOutputStream(out)) {
            final byte[] contextBytes = context.getBytes();
            deflater.write(contextBytes);
            deflater.finish();
            return out.toByteArray();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decompress(final byte[] compressedContent) {
        try(ByteArrayOutputStream out = new ByteArrayOutputStream();
            InflaterOutputStream inflater = new InflaterOutputStream(out)) {
            inflater.write(compressedContent);
            inflater.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
