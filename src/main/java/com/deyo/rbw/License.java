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
    private static final String l1 = "-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxIwPnm14IeMbXhYbz1QZ\nTQrWMZvTyvWRBkrOcmMieFetEZZ+TAc8+DUh2jRFZr6EhYQz6swgzwb8MqreEMxu\nYucO3OY6tdT7UDGkKb416M++Mhmx6Mxs4WZ+N/m1yuq484hCwuSMYYajzNDnibs4\ngngQ7FdjGI/x5mWzhBopfaHgy2cjJIT9OJGc+fjV4oD8ClZxISv8eLzjod6LeXtJ\nVir+72VZBFouaBK85q+iuARlQ70fV2Cl3Y0ncViedC+8JG3L2aQ4zFiBYGzUh/Vy\nyuH2m52Y7Xdu1axo+KPVxtPT7jEiwNSsqmojqOy/xZ+KlRDWM3GoTuy8aGiiQ1tb\n4wIDAQAB\n-----END PUBLIC KEY-----";

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
        boolean v1 = fetchAndHandleResponse(s8, u0, l1, r7);
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
            if (!p7.has(q8a)) {
                return false;
            }
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
            String y6a = "-----BEGIN PUBLIC KEY-----";
            String y6b = "-----END PUBLIC KEY-----";
            String y6 = u2.replace(y6a, "");
            String z7 = y6.replace(y6b, "");
            String a8a = "\\s";
            String a8 = z7.replaceAll(a8a, "");
            byte[] b9 = Base64.getDecoder().decode(a8);
            X509EncodedKeySpec c0 = new X509EncodedKeySpec(b9);
            String d1a = "RSA";
            KeyFactory d1 = KeyFactory.getInstance(d1a);
            java.security.PublicKey e2 = d1.generatePublic(c0);
            String f3a = "SHA256withRSA";
            Signature f3 = Signature.getInstance(f3a);
            f3.initVerify(e2);
            byte[] g4 = s0.getBytes(StandardCharsets.UTF_8);
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
            boolean s6a = s6.has(a0);
            if (!s6a) {
                return false;
            }
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
