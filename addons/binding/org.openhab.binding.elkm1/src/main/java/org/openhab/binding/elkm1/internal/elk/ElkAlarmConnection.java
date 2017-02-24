package org.openhab.binding.elkm1.internal.elk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.openhab.binding.elkm1.internal.config.ElkAlarmConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class ElkAlarmConnection {
    private final Logger logger = LoggerFactory.getLogger(ElkAlarmConnection.class);
    private final ElkAlarmConfig config;
    private final ElkMessageFactory factory;
    private Socket socket;
    private boolean running = false;
    private Thread elkAlarmThread;
    private List<ElkListener> listeners = Lists.newArrayList();

    public ElkAlarmConnection(ElkAlarmConfig config, ElkMessageFactory factory) {
        this.config = config;
        this.factory = factory;
    }

    public boolean initialize() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }
            } };

            SSLContext context = SSLContext.getInstance("SSL");
            context.init(new KeyManager[] {}, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory factory = context.getSocketFactory();
            // Create a socket with the ssl socket factory.
            // socket = (SSLSocket) factory.createSocket(config.ipAddress, config.port);
            // socket.startHandshake();
            socket = new Socket(config.ipAddress, 2101);
            running = true;
            elkAlarmThread = new Thread(new ReadingDataThread());
            elkAlarmThread.start();
        } catch (IOException e) {
            logger.error("Unable to open connection to the elk alarm {}:{}", config.ipAddress, config.port, e);
            return false;
        } catch (NoSuchAlgorithmException e) {
            logger.error("Unable to open connection to the elk alarm {}:{}", config.ipAddress, config.port, e);
            return false;
        } catch (KeyManagementException e) {
            logger.error("Unable to open connection to the elk alarm {}:{}", config.ipAddress, config.port, e);
            return false;
        }

        return socket != null && !socket.isClosed();
    }

    /**
     * Called to shutdown the running threads and close the socket.
     */
    public void shutdown() {
        running = false;
        try {
            socket.close();
        } catch (IOException e) {
            logger.error("Closing the socket", config.ipAddress, config.port, e);
        }
    }

    public void sendCommand(ElkMessage message) {
        String toSend = message.getSendableMessage() + "\r\n";
        try {
            socket.getOutputStream().write(toSend.getBytes(StandardCharsets.US_ASCII));
            socket.getOutputStream().flush();
            logger.error("Writing {} to alarm", toSend);
        } catch (IOException e) {
            logger.error("Errpr writing to elk alarm {}:{}", config.ipAddress, config.port, e);
            running = false;
            try {
                socket.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                logger.error("Error closing socket to elk alarm {}:{}", config.ipAddress, config.port, e);
            }
            socket = null;
        }
    }

    /**
     * Adds the elk listener into the list of things listening for messages.
     */
    public void addElkListener(ElkListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * Removes the elk listener into the list of things listening for messages.
     */
    public void removeElkListener(ElkListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    class ReadingDataThread implements Runnable {

        @Override
        public void run() {
            logger.info("Starting to run the reading thread.");
            BufferedReader buff;
            try {
                buff = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.US_ASCII));
            } catch (IOException e1) {
                logger.error("Unable to setup the reader for the elk alarm {}:{}", config.ipAddress, config.port, e1);
                running = false;
                return;
            }
            while (running) {
                try {
                    String line = buff.readLine();
                    logger.error("Received {} from alarm", line);
                    // Got our line. Yay.
                    ElkMessage message = factory.createMessage(line);
                    if (message != null) {
                        synchronized (listeners) {
                            for (ElkListener listen : listeners) {
                                listen.handleElkMessage(message);
                            }
                        }
                        logger.info("Dispatched elk message {} {}", message.toString(), line);
                    } else {
                        logger.info("Unknown elk message {}", line);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    logger.error("Error reading line from elk alarm {}:{}", config.ipAddress, config.port, e);
                }
            }
        }
    }
}
