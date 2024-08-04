package id.awan.jee;

import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4SafeDecompressor;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Lz4Test {
    @Test
    void lz4Compress() throws UnsupportedEncodingException {


        String data = "123453452";

        String compress = compress(data);

        String decompress = decompress(compress);
        System.out.println(decompress);

    }

    public static String compress(String data) {
        LZ4Factory factory = LZ4Factory.fastestInstance();
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        LZ4Compressor compressor = factory.fastCompressor();
        int maxCompressedLength = compressor.maxCompressedLength(dataBytes.length);
        byte[] compressed = new byte[maxCompressedLength];
        int compressedLength = compressor.compress(dataBytes, 0, dataBytes.length, compressed, 0, maxCompressedLength);
        byte[] compressedData = new byte[compressedLength];
        System.arraycopy(compressed, 0, compressedData, 0, compressedLength);
        return Base64.getEncoder().encodeToString(compressedData);
    }

    public static String decompress(String compressedData) {
        LZ4Factory factory = LZ4Factory.fastestInstance();
        byte[] compressedBytes = Base64.getDecoder().decode(compressedData);
        LZ4SafeDecompressor decompressor = factory.safeDecompressor();
        byte[] restored = new byte[compressedBytes.length * 255]; // Over-sized buffer
        int decompressedLength = decompressor.decompress(compressedBytes, 0, compressedBytes.length, restored, 0);
        byte[] decompressedData = new byte[decompressedLength];
        System.arraycopy(restored, 0, decompressedData, 0, decompressedLength);
        return new String(decompressedData, StandardCharsets.UTF_8);
    }
}
