package com.janetfilter.plugins.power;

import com.janetfilter.core.commons.DebugInfo;
import com.janetfilter.core.enums.RuleType;
import com.janetfilter.core.models.FilterRule;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResultFilter {
    private static final Map<String, BigInteger> cached = new ConcurrentHashMap<>();
    private static Map<String, String> map;

    public static void setRules(List<FilterRule> rules) {
        map = new HashMap<>();

        for (FilterRule rule : rules) {
            if (rule.getType() != RuleType.EQUAL) {
                continue;
            }

            String[] sections = rule.getRule().split("->", 2);
            if (2 != sections.length) {
                DebugInfo.output("Invalid record: " + rule + ", skipped.");
                continue;
            }

            map.put(sections[0], sections[1]);
        }
    }

    public static BigInteger testFilter(BigInteger x, BigInteger y, BigInteger z) {
        String key = String.format("%s,%s,%s", x, y, z);
        String i = map.get(key);
        if (null == i) {
            return null;
        }

        return cached.computeIfAbsent(key, k -> new BigInteger(i));
    }
}
