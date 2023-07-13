package org.otter.gsmpdu.decoder;

import org.otter.gsmpdu.beans.StdAlphabet;

import java.io.ByteArrayOutputStream;

/// <summary>
/// 7Bit解码器。
/// </summary>
public class SevenBitDecoder implements IDecoder {

    public String decode(byte[] raw) {
        return converString(raw);
    }

    public static String converString(byte[] raw) {
        byte[] buffer = decompress(raw);
        StringBuilder sb = new StringBuilder();
        int bufferLen = buffer.length;
        for (int i = 0; i < bufferLen; i++) {
            byte c = buffer[i];
            //remove
            if (c == 27 && ++i < bufferLen) {
                switch (buffer[i]) {
                    case 10:
                        sb.append("\\x0c");
                        continue;
                    case 20:
                        sb.append("\\x5e");
                        continue;
                    case 40:
                        sb.append("\\x7b");
                        continue;
                    case 41:
                        sb.append("\\x7d");
                        continue;
                    case 47:
                        sb.append("\\x5c");
                        continue;
                    case 60:
                        sb.append("\\x5b");
                        continue;
                    case 61:
                        sb.append("\\x7e");
                        continue;
                    case 62:
                        sb.append("\\x5d");
                        continue;
                    case 64:
                        sb.append("\\x7c");
                        continue;
                    case 101:
                        sb.append("\u20ac");
                        continue;
                    default:
                        --i;
                        break;
                }
            }
            sb.append(StdAlphabet.stdAlphabet[c]);
        }

        return sb.toString();
    }

    static byte[] decompress(byte[] raw) {
        ByteArrayOutputStream ms = new ByteArrayOutputStream();
        int nLeft = 0;
        int nByte = 0;

        for (byte b : raw) {
            int a = b & 0xFF;
            byte sevenBits = (byte) (((a << nByte) | nLeft) & 0x7f);

            ms.write(sevenBits);

            nLeft = (a >> (7 - nByte));
            nByte++;

            if (nByte == 7) {
                if (nLeft != 0) ms.write(nLeft);
                nLeft = 0;
                nByte = 0;
            }
        }

        return ms.toByteArray();
    }
}