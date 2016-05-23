package com.begentgroup.gcm;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Random;

/**
 * Created by dongja94 on 2016-05-17.
 */
public class DeviceGroupSender {
    private String key;
    private String senderId;

    private static final int DEFAULT_RETRY_COUNT = 3;
    private int retryCount = DEFAULT_RETRY_COUNT;

    protected static final String UTF8 = "UTF-8";

    /**
     * Initial delay before first retry, without jitter.
     */
    protected static final int BACKOFF_INITIAL_DELAY = 1000;
    /**
     * Maximum delay before a retry.
     */
    protected static final int MAX_BACKOFF_DELAY = 1024000;

    public static final String GCM_DEVICE_GROUP_ENDPOINT =
            "https://android.googleapis.com/gcm/notification";

    public DeviceGroupSender(String key, String senderId){
        this.key = key;
        this.senderId = senderId;
    }

    public NotificationKey createDeviceGroup(String notificationKeyName, String... registrationIds) throws IOException {
        return actionDeviceGroupMessage(DeviceGroupMessage.OPERATION_CREATE, notificationKeyName, null, registrationIds);
    }

    public NotificationKey createDeviceGroup(String notificationKeyName, List<String> registrationIds) throws IOException {
        return actionDeviceGroupMessage(DeviceGroupMessage.OPERATION_CREATE, notificationKeyName, null, registrationIds);
    }

    public NotificationKey addDeviceGroup(String notificationKeyName, String notificationKey, String... registrationIds) throws IOException {
        return actionDeviceGroupMessage(DeviceGroupMessage.OPERATION_ADD, notificationKeyName, notificationKey, registrationIds);
    }

    public NotificationKey addDeviceGroup(String notificationKeyName, String notificationKey, List<String> registrationIds) throws IOException {
        return actionDeviceGroupMessage(DeviceGroupMessage.OPERATION_ADD, notificationKeyName, notificationKey, registrationIds);
    }

    public NotificationKey removeDeviceGroup(String notificationKeyName, String notificationKey, String... registrationIds) throws IOException {
        return actionDeviceGroupMessage(DeviceGroupMessage.OPERATION_REMOVE, notificationKeyName, notificationKey, registrationIds);
    }

    public NotificationKey removeDeviceGroup(String notificationKeyName, String notificationKey, List<String> registrationIds) throws IOException {
        return actionDeviceGroupMessage(DeviceGroupMessage.OPERATION_REMOVE, notificationKeyName, notificationKey, registrationIds);
    }

    public NotificationKey actionDeviceGroupMessage(String operation, String notificationKeyName, String notificationKey, String... registrationIds) throws IOException {
        DeviceGroupMessage message = new DeviceGroupMessage.Builder(operation)
                .notificationKeyName(notificationKeyName)
                .notificationKey(notificationKey)
                .registrationIds(registrationIds)
                .build();
        return send(message, retryCount);
    }

    public NotificationKey actionDeviceGroupMessage(String operation, String notificationKeyName, String notificationKey, List<String> registrationIds) throws IOException {
        DeviceGroupMessage message = new DeviceGroupMessage.Builder(operation)
                .notificationKeyName(notificationKeyName)
                .notificationKey(notificationKey)
                .registrationIds(registrationIds)
                .build();
        return send(message, retryCount);
    }

    protected final Random random = new Random();

    public NotificationKey send(DeviceGroupMessage message, int retries) throws IOException {
        int attempt = 0;
        NotificationKey result;
        int backoff = BACKOFF_INITIAL_DELAY;
        boolean tryAgain;
        do {
            attempt++;
            result = sendNoRetry(message);
            tryAgain = result == null && attempt <= retries;
            if (tryAgain) {
                int sleepTime = backoff / 2 + random.nextInt(backoff);
                sleep(sleepTime);
                if (2 * backoff < MAX_BACKOFF_DELAY) {
                    backoff *= 2;
                }
            }
        } while (tryAgain);
        if (result == null) {
            throw new IOException("Could not send message after " + attempt +
                    " attempts");
        }
        return result;
    }

    Gson gson = new Gson();

    public NotificationKey sendNoRetry(DeviceGroupMessage message) throws InvalidRequestException {
        if (nonNull(message.getNotificationKeyName()).length() == 0) {
            throw new IllegalArgumentException("registrationIds cannot be empty");
        }
        String jsonRequest = gson.toJson(message);
        String responseBody = makeGcmHttpRequest(jsonRequest);
        if (responseBody == null) {
            return null;
        }
        return gson.fromJson(responseBody, NotificationKey.class);
    }

    private String makeGcmHttpRequest(String requestBody) throws InvalidRequestException {
        HttpURLConnection conn;
        int status;
        try {
            conn = post(GCM_DEVICE_GROUP_ENDPOINT, "application/json", requestBody);
            status = conn.getResponseCode();
        } catch (IOException e) {
            return null;
        }
        String responseBody;
        if (status != 200) {
            try {
                responseBody = getAndClose(conn.getErrorStream());
            } catch (IOException e) {
                responseBody = "N/A";
            }
            throw new InvalidRequestException(status, responseBody);
        }
        try {
            responseBody = getAndClose(conn.getInputStream());
        } catch(IOException e) {
            return null;
        }
        return responseBody;
    }

    /**
     * Convenience method to convert an InputStream to a String.
     * <p>
     * If the stream ends in a newline character, it will be stripped.
     * <p>
     * If the stream is {@literal null}, returns an empty string.
     */
    protected static String getString(InputStream stream) throws IOException {
        if (stream == null) {
            return "";
        }
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(stream));
        StringBuilder content = new StringBuilder();
        String newLine;
        do {
            newLine = reader.readLine();
            if (newLine != null) {
                content.append(newLine).append('\n');
            }
        } while (newLine != null);
        if (content.length() > 0) {
            // strip last newline
            content.setLength(content.length() - 1);
        }
        return content.toString();
    }

    private static String getAndClose(InputStream stream) throws IOException {
        try {
            return getString(stream);
        } finally {
            if (stream != null) {
                close(stream);
            }
        }
    }

    /**
     * Makes an HTTP POST request to a given endpoint.
     *
     * <p>
     * <strong>Note: </strong> the returned connected should not be disconnected,
     * otherwise it would kill persistent connections made using Keep-Alive.
     *
     * @param url endpoint to post the request.
     * @param contentType type of request.
     * @param body body of the request.
     *
     * @return the underlying connection.
     *
     * @throws IOException propagated from underlying methods.
     */
    protected HttpURLConnection post(String url, String contentType, String body)
            throws IOException {
        if (url == null || contentType == null || body == null) {
            throw new IllegalArgumentException("arguments cannot be null");
        }
        byte[] bytes = body.getBytes(UTF8);
        HttpURLConnection conn = getConnection(url);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setFixedLengthStreamingMode(bytes.length);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", contentType);
        conn.setRequestProperty("Authorization", "key=" + key);
        conn.setRequestProperty("project_id",senderId);
        OutputStream out = conn.getOutputStream();
        try {
            out.write(bytes);
        } finally {
            close(out);
        }
        return conn;
    }

    /**
     * Gets an {@link HttpURLConnection} given an URL.
     */
    protected HttpURLConnection getConnection(String url) throws IOException {
        return (HttpURLConnection) new URL(url).openConnection();
    }
    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }


    static <T> T nonNull(T argument) {
        if (argument == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        return argument;
    }

    void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
