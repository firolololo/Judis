package com.stellar.judis.protocol;

import com.stellar.judis.model.JudisSymbol;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author cloudwalk3212
 * @version 1.0
 * @date 2020/12/3 10:02
 */
public class JudisProtocol implements Protocol {
    public static final Charset CHARTSET = StandardCharsets.UTF_8;
    private static final String SIMPLE_STRING_BYTE = "+";
    private static final String ERROR_BYTE = "-";
    private static final String INTEGER_BYTE = ":";
    private static final String ARRAY_BYTE = "*";
    private static final String BATCH_STRING_BYTE = "$";
    private static final String CR_BYTE = "\r";
    private static final String LF_BYTE = "\n";

    @Override
    public String encode(String content) {
        String[] strs = content.split(JudisSymbol.SEG_SIGN.getSymbol());
        if (strs.length == 0) {
            throw new RuntimeException("invalid content");
        }
        Command command = checkCommand(strs[0]);
        return judisCommand(strs);
    }

    @Override
    public String parse(String encodeContent) {
        String[] strs = encodeContent.split(CR_BYTE + LF_BYTE);
        if (strs.length == 1) return strs[0].substring(1);
        try {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < strs.length; i++) {
                if (ARRAY_BYTE.equals(strs[i].substring(0, 1))) continue;
                if (BATCH_STRING_BYTE.equals(strs[i].substring(0, 1))) {
                    stringBuilder.append(strs[i + 1]);
                    stringBuilder.append(JudisSymbol.SEG_SIGN.getSymbol());
                }
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            throw new RuntimeException("judis protocol parse fail");
        }
    }

    @Override
    public String decode(String content) {
        return judisResponse(content);
    }

    @Override
    public Charset charset() {
        return CHARTSET;
    }

    public enum Command {
        SET, GET
    }

    private Command checkCommand(String opt) {
        for (Command command: Command.values()) {
            if (command.name().equals(opt)) return command;
        }
        throw new RuntimeException("command not found");
    }

    private String judisCommand(final String... args) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(ARRAY_BYTE)
                    .append(1 + args.length)
                    .append(CR_BYTE)
                    .append(LF_BYTE);
            for (String arg: args) {
                stringBuilder.append(BATCH_STRING_BYTE)
                        .append(arg.length())
                        .append(CR_BYTE)
                        .append(LF_BYTE);
                stringBuilder.append(arg)
                        .append(CR_BYTE)
                        .append(LF_BYTE);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String judisResponse(String resp) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(SIMPLE_STRING_BYTE)
                    .append(resp)
                    .append(CR_BYTE)
                    .append(LF_BYTE);
            return stringBuilder.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
