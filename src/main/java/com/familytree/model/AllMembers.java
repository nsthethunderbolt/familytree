package com.familytree.model;

import java.util.HashMap;
import java.util.Map;

public class AllMembers {
    private static Map<Long, Member> members_byid = new HashMap<>();
    private static Map<String, Member> members_bynn = new HashMap<>();
    public static long godId = 0L;

    public static Member get(String nn) {
        return members_bynn.get(nn);
    }

    public static Member get(long id) {
        return members_byid.get(id);
    }

    public static void put(long id, Member member) {
        members_byid.put(id, member);
        members_bynn.put(member.getNickName(), member);
    }

    public static void put(String nn, Member member) {
        members_bynn.put(nn, member);
        members_byid.put(member.getId(), member);
    }

    public static String showAll() {
        return members_byid.toString();
    }

    public static boolean isEmpty() {
        return members_bynn.isEmpty() && members_byid.isEmpty();
    }
}
