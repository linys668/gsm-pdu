package org.otter.gsmpdu.decoder;

import java.nio.charset.StandardCharsets;

public class EightBitDecoder implements IDecoder {
    @Override
    public String decode(byte[] raw) {
        return new String(raw, StandardCharsets.ISO_8859_1);
    }
}
