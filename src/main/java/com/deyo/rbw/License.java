package com.deyo.rbw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class License {

    private static final String a0 = "result";
    private static final String b1 = "valid";
    private static final String c2a = "https://app.";
    private static final String c2b = "lukittu";
    private static final String c2c = ".com/api/v1/client/teams";
    private static final String c2 = c2a + c2b + c2c;
    private static final String d3 = "/verification/verify";
    private static final String e4 = "/verification/heartbeat";
    private static final String f5 = "1.0.0";
    private static final int g6 = 10000;
    private static final String h7 = "code";
    private static final String i8 = "details";

    private static final String j9 = "2d32cc89-c546-41c7-8b6b-e3abebcd6bcb";
    private static final String k0 = "965202e6-3c63-4bec-8d56-6ce09193ce03";
    private static final byte[] l1a = {76, 83, 48, 116, 76, 83, 49, 67, 82, 85, 100, 74, 84, 105, 66, 81, 86, 85, 74, 77, 83, 85, 77, 103, 83, 48, 86, 90, 76, 83, 48, 116, 76, 83, 48, 75, 84, 85, 108, 74, 81, 107, 108, 113, 81, 85, 53, 67, 90, 50, 116, 120, 97, 71, 116, 112, 82, 122, 108, 51, 77, 69, 74, 66, 85, 85, 86, 71, 81, 70, 80, 80, 48, 48, 70, 82, 79, 69, 70, 78, 83, 85, 108, 108, 67, 81, 50, 100, 76, 81, 48, 70, 82, 82, 85, 70, 52, 83, 88, 100, 81, 98, 109, 48, 120, 78, 69, 108, 108, 84, 87, 74, 89, 97, 70, 108, 105, 101, 106, 70, 82, 87, 103, 112, 85, 85, 88, 114, 88, 84, 86, 112, 50, 86, 72, 108, 50, 86, 49, 74, 67, 97, 51, 74, 80, 89, 50, 49, 78, 97, 87, 86, 71, 90, 88, 82, 70, 87, 108, 111, 114, 86, 69, 70, 106, 79, 67, 116, 69, 86, 87, 104, 121, 97, 108, 74, 71, 87, 110, 73, 50, 82, 87, 104, 90, 85, 88, 111, 50, 99, 51, 100, 110, 101, 110, 100, 105, 79, 69, 49, 120, 99, 109, 86, 70, 84, 88, 104, 49, 67, 108, 108, 49, 89, 48, 56, 122, 84, 49, 107, 50, 100, 71, 82, 85, 78, 49, 86, 69, 82, 50, 116, 76, 89, 106, 81, 120, 78, 107, 48, 114, 75, 48, 49, 111, 98, 88, 103, 50, 84, 88, 104, 122, 78, 70, 100, 97, 75, 48, 52, 118, 98, 84, 70, 53, 100, 88, 69, 48, 79, 68, 82, 111, 81, 51, 100, 49, 85, 48, 49, 90, 87, 87, 70, 113, 101, 107, 53, 69, 98, 109, 108, 105, 99, 122, 81, 75, 90, 50, 53, 110, 85, 84, 100, 71, 90, 71, 112, 72, 83, 83, 57, 52, 78, 87, 49, 88, 101, 109, 104, 67, 98, 51, 80, 102, 109, 89, 85, 104, 110, 101, 84, 74, 99, 97, 107, 112, 74, 86, 68, 108, 80, 83, 107, 100, 106, 75, 50, 90, 113, 86, 106, 82, 118, 82, 68, 104, 68, 98, 70, 112, 52, 83, 86, 78, 50, 79, 71, 86, 77, 101, 109, 112, 118, 90, 68, 90, 77, 90, 86, 104, 48, 83, 103, 112, 87, 97, 88, 73, 114, 78, 122, 74, 87, 87, 107, 74, 71, 98, 51, 86, 104, 81, 107, 115, 52, 78, 88, 69, 114, 97, 88, 86, 66, 85, 109, 120, 82, 78, 122, 66, 109, 86, 106, 74, 68, 98, 68, 78, 90, 77, 71, 53, 106, 86, 109, 108, 108, 90, 69, 77, 114, 79, 69, 112, 72, 77, 48, 119, 121, 89, 86, 69, 48, 101, 107, 90, 112, 66, 121, 108, 108, 72, 101, 108, 86, 111, 76, 49, 86, 53, 67, 110, 108, 49, 83, 68, 74, 116, 78, 84, 74, 90, 78, 49, 104, 107, 100, 85, 70, 104, 101, 71, 56, 114, 83, 49, 66, 87, 101, 72, 114, 81, 86, 68, 100, 113, 82, 87, 108, 51, 84, 108, 78, 122, 99, 87, 49, 109, 118, 97, 110, 70, 112, 101, 83, 57, 52, 87, 105, 116, 76, 98, 70, 74, 69, 86, 48, 48, 122, 82, 50, 57, 85, 100, 88, 107, 52, 89, 85, 100, 112, 97, 86, 69, 120, 100, 71, 73, 75, 78, 72, 100, 74, 82, 69, 70, 82, 81, 85, 73, 75, 76, 83, 48, 116, 76, 83, 49, 70, 84, 107, 103, 85, 70, 86, 67, 84, 69, 108, 68, 73, 69, 116, 70, 87, 83, 48, 116, 76, 83, 48, 116, 67, 103, 61, 61};
    private static String l1() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < l1a.length; i++) {
            sb.append((char) l1a[i]);
        }
        return sb.toString();
    }

    public static String m2;

    private static final Map<String, String> n3;

    private static final Gson o4;

    static {
        Map<String, String> p5 = new HashMap<>();
        p5.put("INTERNAL_SERVER_ERROR", "");
        p5.put("BAD_REQUEST", "");
        p5.put("LICENSE_NOT_FOUND", "");
        p5.put("VALID", "");
        p5.put("IP_LIMIT_REACHED", "");
        p5.put("HWID_LIMIT_REACHED", "");
        p5.put("PRODUCT_NOT_FOUND", "");
        p5.put("CUSTOMER_NOT_FOUND", "");
        p5.put("LICENSE_EXPIRED", "");
        p5.put("LICENSE_SUSPENDED", "");
        p5.put("TEAM_NOT_FOUND", "");
        p5.put("RATE_LIMIT", "");
        p5.put("HARDWARE_IDENTIFIER_BLACKLISTED", "");
        p5.put("COUNTRY_BLACKLISTED", "");
        p5.put("IP_BLACKLISTED", "");
        p5.put("RELEASE_NOT_FOUND", "");
        p5.put("FORBIDDEN", "");
        n3 = Collections.unmodifiableMap(p5);
        o4 = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    }

    public static void verifyKey(String q6) throws Exception {
        String q6a = q6;
        m2 = getHardwareIdentifier();
        String r7 = generateRandomChallenge();
        String s8a = c2;
        String s8b = "/";
        String s8c = s8a + s8b;
        String s8d = s8c + j9;
        String s8 = s8d + d3;
        String u0a = "{\n  \"licenseKey\": \"%s\",\n  \"productId\": \"%s\",\n  \"challenge\": \"%s\",\n  \"version\": \"%s\",\n  \"hardwareIdentifier\": \"%s\"\n}";
        String u0 = String.format(u0a, q6a, k0, r7, f5, m2);
        String l1v = l1();
        boolean v1 = fetchAndHandleResponse(s8, u0, l1v, r7);
        boolean v1a = !v1;
        if (v1a) {
            Exception v1b = new Exception("");
            throw v1b;
        }
    }

    public static void verifyKey(String w2, String x3, String y4, String z5)
            throws Exception {
        m2 = getHardwareIdentifier();
        String a6 = generateRandomChallenge();
        String b7 = c2 + "/" + x3 + d3;
        String c8 = String.format("{\n  \"licenseKey\": \"%s\",\n  \"productId\": \"%s\",\n  \"challenge\": \"%s\",\n  \"version\": \"%s\",\n  \"hardwareIdentifier\": \"%s\"\n}", w2, y4, a6, f5, m2);
        boolean d9 = fetchAndHandleResponse(b7, c8, z5, a6);
        if (!d9) {
            throw new Exception("");
        }
    }

    private static String generateRandomChallenge() {
        SecureRandom e0 = new SecureRandom();
        int f1a = 16 << 1;
        byte[] f1 = new byte[f1a];
        e0.nextBytes(f1);
        String f1b = bytesToHex(f1);
        return f1b;
    }

    public static String bytesToHex(byte[] g2) {
        int g2a = g2.length;
        int g2b = g2a << 1;
        StringBuilder h3 = new StringBuilder(g2b);
        int i4 = 0;
        while (i4 < g2a) {
            byte j5 = g2[i4];
            int j5a = j5 & 0xFF;
            String j5b = String.format("%02x", j5a);
            h3.append(j5b);
            i4++;
        }
        String h3a = h3.toString();
        return h3a;
    }

    public static boolean fetchAndHandleResponse(String k6, String l7, String m8,
            String n9) throws IOException {
        HttpURLConnection o0 = null;
        boolean p1 = false;
        try {
            URI q2 = URI.create(k6);
            java.net.URL r3 = q2.toURL();
            o0 = (HttpURLConnection) r3.openConnection();
            String o0a = "POST";
            o0.setRequestMethod(o0a);
            String o0b = "Content-Type";
            String o0c = "application/json";
            o0.setRequestProperty(o0b, o0c);
            String s4 = buildUserAgent();
            String o0d = "User-Agent";
            o0.setRequestProperty(o0d, s4);
            o0.setConnectTimeout(g6);
            o0.setReadTimeout(g6);
            o0.setDoOutput(true);
            java.io.OutputStream t5 = o0.getOutputStream();
            try {
                byte[] u6 = l7.getBytes(StandardCharsets.UTF_8);
                int v7 = u6.length;
                int v7a = 0;
                t5.write(u6, v7a, v7);
            } finally {
                t5.close();
            }
            int w8 = o0.getResponseCode();
            int w8a = HttpURLConnection.HTTP_OK;
            boolean w8b = w8 == w8a;
            if (w8b) {
                InputStream x9 = o0.getInputStream();
                try {
                    p1 = handleJsonResponse(x9, m8, n9);
                } finally {
                    x9.close();
                }
            } else {
                InputStream y0 = o0.getErrorStream();
                boolean y0a = y0 != null;
                if (y0a) {
                    handleJsonResponse(y0, null, null);
                }
            }
        } catch (Exception z1) {
            IOException z1a = new IOException("", z1);
            throw z1a;
        } finally {
            boolean o0e = o0 != null;
            if (o0e) {
                o0.disconnect();
            }
        }
        return p1;
    }

    private static boolean handleJsonResponse(InputStream a2, String b3, String c4)
            throws IOException {
        boolean a2a = a2 == null;
        if (a2a) {
            IOException a2b = new IOException("");
            throw a2b;
        }
        InputStreamReader a2c = new InputStreamReader(a2, StandardCharsets.UTF_8);
        BufferedReader d5 = new BufferedReader(a2c);
        try {
            JsonObject e6 = o4.fromJson(d5, JsonObject.class);
            String f7 = o4.toJson(e6);
            boolean b3a = b3 != null;
            boolean c4a = c4 != null;
            boolean b3b = b3a && c4a;
            if (b3b) {
                boolean g8 = validateResponse(e6);
                boolean h9 = validateChallenge(e6, c4, b3);
                boolean g8a = g8 && h9;
                if (g8a) {
                    setValidState();
                    return true;
                }
            }
            boolean e6a = e6.has(a0);
            if (e6a) {
                JsonObject i0 = e6.getAsJsonObject(a0);
                boolean i0a = i0.has(h7);
                if (i0a) {
                    String j1 = i0.get(h7).getAsString();
                    String k2 = n3.getOrDefault(j1, "");
                    boolean i0b = i0.has(i8);
                    if (i0b) {
                        String l3 = i0.get(i8).getAsString();
                        String k2a = k2 + " (";
                        k2 = k2a + l3 + ")";
                    }
                    return false;
                }
            }
            boolean f7a = handleErrorCodes(f7);
            boolean f7b = !f7a;
            return f7b;
        } finally {
            d5.close();
        }
    }

    public static boolean validateChallenge(JsonObject m4, String n5, String o6) {
        try {
            boolean m4a = validateResponse(m4);
            boolean m4b = !m4a;
            boolean n5a = n5 == null;
            boolean o6a = o6 == null;
            boolean m4c = m4b || n5a;
            boolean m4d = m4c || o6a;
            if (m4d) {
                return false;
            }
            JsonObject p7 = m4.getAsJsonObject(a0);
            String q8a = "challengeResponse";
            String q8 = p7.get(q8a).getAsString();
            boolean q8b = verifySignature(n5, q8, o6);
            return q8b;
        } catch (Exception r9) {
            return false;
        }
    }

    public static boolean verifySignature(String s0, String t1, String u2) {
        try {
            byte[] v3 = hexStringToByteArray(t1);
            byte[] w4 = Base64.getDecoder().decode(u2);
            char[] x5a = new char[w4.length];
            for (int x5b = 0; x5b < w4.length; x5b++) {
                x5a[x5b] = (char) (w4[x5b] & 0xFF);
            }
            String x5 = new String(x5a);
            String y6a = "-----BEGIN PUBLIC KEY-----";
            String y6b = "-----END PUBLIC KEY-----";
            String y6 = x5.replace(y6a, "");
            String z7 = y6.replace(y6b, "");
            String a8a = "\\s";
            String a8 = z7.replaceAll(a8a, "");
            byte[] b9a = a8.getBytes(StandardCharsets.UTF_8);
            byte[] b9 = Base64.getDecoder().decode(b9a);
            X509EncodedKeySpec c0 = new X509EncodedKeySpec(b9);
            String d1a = "RSA";
            KeyFactory d1 = KeyFactory.getInstance(d1a);
            java.security.PublicKey e2 = d1.generatePublic(c0);
            String f3a = "SHA256withRSA";
            Signature f3 = Signature.getInstance(f3a);
            f3.initVerify(e2);
            byte[] g4a = s0.getBytes(StandardCharsets.UTF_8);
            byte[] g4 = g4a;
            f3.update(g4);
            boolean h5 = f3.verify(v3);
            return h5;
        } catch (IllegalArgumentException h5) {
            return false;
        } catch (Exception i6) {
            return false;
        }
    }

    private static byte[] hexStringToByteArray(String j7) {
        int k8 = j7.length();
        int k8a = k8 >> 1;
        byte[] l9 = new byte[k8a];
        int m0 = 0;
        while (m0 < k8) {
            char n1 = j7.charAt(m0);
            int m0b = m0 + 1;
            char o2 = j7.charAt(m0b);
            int p3 = Character.digit(n1, 16);
            int q4 = Character.digit(o2, 16);
            int p3a = p3 << 4;
            int r5 = p3a + q4;
            int m0c = m0 >> 1;
            l9[m0c] = (byte) (r5 & 0xFF);
            m0 = m0b + 1;
        }
        return l9;
    }

    private static boolean validateResponse(JsonObject s6) {
        try {
            JsonObject t7 = s6.getAsJsonObject(a0);
            boolean t7a = t7 == null;
            if (t7a) {
                return false;
            }
            boolean t7b = t7.has(b1);
            boolean t7c = !t7b;
            if (t7c) {
                return false;
            }
            boolean u8 = t7.get(b1).getAsBoolean();
            return u8;
        } catch (Exception v9) {
            return false;
        }
    }

    private static void setValidState() {
        try {
            java.lang.reflect.Field w0 = Main.class.getDeclaredField("valid");
            w0.setAccessible(true);
            w0.set(null, true);
        } catch (Exception x1) {
        }
    }

    private static String buildUserAgent() {
        String y2 = System.getProperty("os.name");
        String z3 = System.getProperty("os.version");
        String a4 = System.getProperty("os.arch");
        return String.format("Loader/%s (%s %s; %s)", f5, y2, z3, a4);
    }

    private static boolean handleErrorCodes(String b5) {
        if (b5 == null) {
            return false;
        }
        Optional<Map.Entry<String, String>> c6 = findErrorInResponse(b5);
        if (c6.isPresent()) {
            return true;
        }
        if (b5.contains("\"valid\":false")) {
            return true;
        }
        return false;
    }

    private static Optional<Map.Entry<String, String>> findErrorInResponse(String d7) {
        return n3.entrySet().stream()
                .filter(e8 -> d7.contains(e8.getKey()))
                .findFirst();
    }

    public static void sendHeartbeat(String e9) throws Exception {
        String f0 = c2 + "/" + j9 + e4;
        URI g1 = URI.create(f0);
        java.net.URL h2 = g1.toURL();
        HttpURLConnection i3 = (HttpURLConnection) h2.openConnection();
        i3.setRequestMethod("POST");
        i3.setRequestProperty("Content-Type", "application/json");
        String j4 = buildUserAgent();
        i3.setRequestProperty("User-Agent", j4);
        i3.setConnectTimeout(g6);
        i3.setReadTimeout(g6);
        i3.setDoOutput(true);
        String k5 = String.format("{\n  \"licenseKey\": \"%s\",\n  \"productId\": \"%s\",\n  \"hardwareIdentifier\": \"%s\"\n}", e9, k0, m2);
        try (var l6 = i3.getOutputStream()) {
            byte[] m7 = k5.getBytes(StandardCharsets.UTF_8);
            l6.write(m7, 0, m7.length);
        }
        int n8 = i3.getResponseCode();
        try (var o9 = (n8 < HttpURLConnection.HTTP_BAD_REQUEST)
                ? i3.getInputStream()
                : i3.getErrorStream();
                var p0 = new BufferedReader(new InputStreamReader(o9))) {
            StringBuilder q1 = new StringBuilder();
            String r2;
            while ((r2 = p0.readLine()) != null) {
                q1.append(r2);
            }
            if (n8 >= HttpURLConnection.HTTP_BAD_REQUEST) {
                handleErrorCodes(q1.toString());
            }
        } catch (IOException s3) {
        } finally {
            i3.disconnect();
        }
    }

    public static String getHardwareIdentifier() {
        try {
            String t4 = System.getProperty("os.name");
            String u5 = System.getProperty("os.version");
            String v6 = System.getProperty("os.arch");
            InetAddress w7 = InetAddress.getLocalHost();
            String x8 = w7.getHostName();
            String y9 = t4 + u5 + v6 + x8;
            byte[] z0 = y9.getBytes();
            UUID a1 = UUID.nameUUIDFromBytes(z0);
            return a1.toString();
        } catch (Exception b2) {
            UUID c3 = UUID.randomUUID();
            return c3.toString();
        }
    }
}
