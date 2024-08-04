package id.awan.jee.util;

import io.jsonwebtoken.io.CompressionAlgorithm;
import net.jpountz.lz4.*;

import java.io.InputStream;
import java.io.OutputStream;

public class LZ4CompressionAlgorithm implements CompressionAlgorithm {

    LZ4Factory factory = LZ4Factory.fastestInstance();

    @Override
    public OutputStream compress(OutputStream out) {
        LZ4Compressor compressor = factory.fastCompressor();
        return new LZ4BlockOutputStream(out, 64 * 1024, compressor);
    }

    @Override
    public InputStream decompress(InputStream in) {
        LZ4FastDecompressor lz4FastDecompressor = factory.fastDecompressor();
        return new LZ4BlockInputStream(in, lz4FastDecompressor);
    }

    @Override
    public String getId() {
        return "LZ4";
    }
}
