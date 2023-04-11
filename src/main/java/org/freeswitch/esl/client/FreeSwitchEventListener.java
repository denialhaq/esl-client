package org.freeswitch.esl.client;

import com.google.common.base.Throwables;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.inbound.IEslEventListener;
import org.freeswitch.esl.client.internal.Context;
import org.freeswitch.esl.client.internal.IModEslApi.EventFormat;
//import org.freeswitch.esl.client.outbound.Context;
import org.freeswitch.esl.client.outbound.IClientHandler;
import org.freeswitch.esl.client.outbound.IClientHandlerFactory;
import org.freeswitch.esl.client.outbound.SocketClient;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class FreeSwitchEventListener {

    private static Logger logger = LoggerFactory.getLogger(FreeSwitchEventListener.class);

    public static void main(String[] args) {
        try {

            final Client inboudClient = new Client();
            inboudClient.connect(new InetSocketAddress("192.168.1.35", 8021), "ClueCon", 10);
            inboudClient.addEventListener(new IEslEventListener() {
                @Override
                public void onEslEvent(Context ctx, EslEvent event) {

                }


            });
            inboudClient.setEventSubscriptions(EventFormat.PLAIN, "all");

            final SocketClient outboundServer = new SocketClient(
                    new InetSocketAddress("0.0.0.0", 8084),
                    new IClientHandlerFactory() {
                        @Override
                        public IClientHandler createClientHandler() {
                            return new IClientHandler() {
                                @Override
                                public void onEslEvent(Context ctx, EslEvent event) {

                                }

//                                @Override
//                                public void handleEslEvent(Context context, EslEvent eslEvent) {
//                                }

                                @Override
                                public void onConnect(Context context, EslEvent eslEvent) {
                                }
                            };
                        }
                    });


        } catch (Throwable t) {
            Throwables.propagate(t);
        }
    }

}
