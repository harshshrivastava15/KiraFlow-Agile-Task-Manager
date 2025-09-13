// src/main/java/com/example/Kiraflow/util/UuidUtil.java
package com.example.Kiraflow.util;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class UuidUtil {
    private UuidUtil() {}

    private static final Pattern HEX32 = Pattern.compile("([0-9a-fA-F]{32})");
    private static final Pattern UUID36 = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");

    public static UUID parseFlexibleUuid(String raw) {
        if (raw == null) throw new IllegalArgumentException("UUID string is null");

        String s = raw.trim();

        // if already standard 36-char UUID
        Matcher m36 = UUID36.matcher(s);
        if (m36.find()) return UUID.fromString(m36.group());

        // strip 0x prefix if present, remove non-hex chars
        if (s.startsWith("0x") || s.startsWith("0X")) s = s.substring(2);
        s = s.replaceAll("[^0-9a-fA-F]", "");

        // if 32 hex chars, insert dashes
        if (s.length() == 32) {
            String uuidStr = s.replaceFirst(
                    "([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{12})",
                    "$1-$2-$3-$4-$5");
            return UUID.fromString(uuidStr);
        }

        // if longer/contains a 32-hex substring, extract it
        Matcher m32 = HEX32.matcher(s);
        if (m32.find()) {
            String hex = m32.group(1);
            String uuidStr = hex.replaceFirst(
                    "([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{12})",
                    "$1-$2-$3-$4-$5");
            return UUID.fromString(uuidStr);
        }

        // last attempt: try direct parse (will throw if invalid)
        return UUID.fromString(raw);
    }
}
