package org.otter.gsmpdu.decoder;

import java.nio.charset.StandardCharsets;

public class UnicodeDecoder implements IDecoder {
    @Override
    public String decode(byte[] raw) {
        return new String(raw, StandardCharsets.UTF_16BE);
    }
}
