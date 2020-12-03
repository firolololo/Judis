package com.stellar.judis.protocol;

import java.nio.charset.Charset;

/**
 * @author cloudwalk3212
 * @version 1.0
 * @date 2020/12/3 13:59
 */
public interface Protocol {
    String encode(String content);
    String decode(String content);
    Charset charset();
}
