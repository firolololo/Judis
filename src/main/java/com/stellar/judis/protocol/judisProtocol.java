package com.stellar.judis.protocol;

import com.stellar.judis.exception.JudisProtocolException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author cloudwalk3212
 * @version 1.0
 * @date 2020/12/3 10:02
 */
public class judisProtocol implements Protocol {
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
        String[] strs = content.split(" ");
        if (strs.length == 0) {
            throw new RuntimeException("invalid content");
        }
        Command command = checkCommand(strs[0]);
        return judisCommand(command, Arrays.copyOfRange(strs, 1, strs.length));
    }

    @Override
    public String decode(String content) {
        return null;
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

    private String judisCommand(final Command command, final String... args) {
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
            return resp;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
