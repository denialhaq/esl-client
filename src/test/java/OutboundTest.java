import com.google.common.base.Throwables;

import org.freeswitch.esl.client.dptools.Execute;
import org.freeswitch.esl.client.dptools.ExecuteException;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.internal.Context;
import org.freeswitch.esl.client.outbound.IClientHandler;
import org.freeswitch.esl.client.outbound.SocketClient;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.freeswitch.esl.client.transport.message.EslHeaders.Name;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

public class OutboundTest {
    private static Logger logger = LoggerFactory.getLogger(OutboundTest.class);
    //fusionpbx
//    private static String sb = "/usr/share/freeswitch/sounds/en/us/callie/ivr/8000/";
    //source freeswitch
private static String sb = "/usr/local/freeswitch/sounds/en/us/callie/ivr/8000/";
//    String prompt = sb + "ivr-please_enter_extension_followed_by_pound.wav";
    String prompt = sb + "ivr-welcome_to_freeswitch.wav";
    String failed = sb + "ivr-that_was_an_invalid_entry.wav";

    public static void main(String[] args) {
        new OutboundTest();
    }

    public OutboundTest() {
        try {

            final SocketClient outboundServer = new SocketClient(
                    new InetSocketAddress("0.0.0.0", 8084),
                    () -> new IClientHandler() {
                        @Override
                        public void onConnect(Context context,
                                              EslEvent eslEvent) {


                            logger.warn(nameMapToString(eslEvent));

                            String uuid = eslEvent.getEventHeaders()
                                    .get("Unique-ID");

                            logger.warn(
                                    "Creating execute app for uuid {}",
                                    uuid);

                            Execute exe = new Execute(context, uuid);

                            try {

                                logger.warn("Answering for uuid {}", uuid);

                                exe.answer();
                                exe.playback(prompt);
//                                exe.echo();
//                                exe.transfer("1002", "XML", "192.168.1.106");
                                exe.recordSession("/tmp/myfile.wav");
                                exe.bridge("user/1002");

                                exe.ApiCommand("uuid_transfer", uuid + " -both 1002 XML 192.168.1.106");
//                                String digits = exe.playAndGetDigits(3,
//                                        5, 10, 10 * 1000, "#", prompt,
//                                        failed, "^\\d+", 10 * 1000);
//                                logger.warn("Digits collected: {}",
//                                        digits);


                            } catch (ExecuteException e) {
                                logger.error(
                                        "Could not prompt for digits",
                                        e);

                            } finally {
                                try {
                                    exe.hangup(null);
                                } catch (ExecuteException e) {
                                    logger.error(
                                            "Could not hangup",e);
                                }
                            }

                        }

                        @Override
                        public void onEslEvent(Context ctx,
                                EslEvent event) {
                            logger.info("OUTBOUND onEslEvent: {}",
                                    event.getEventName());

                        }
                    });
            outboundServer.startAsync();

        } catch (Throwable t) {
            Throwables.propagate(t);
        }
    }

    public static String nameMapToString(EslEvent eslEvent) {
        StringBuilder sb = new StringBuilder("\nHeaders:\n");

        eslEvent.getMessageHeaders().forEach((k, v) -> {
            if (k != null) {
                sb.append(k.toString());
                sb.append("\t\t\t = \t ");
                sb.append(v);
                sb.append("\n");
            }
        });

        eslEvent.getEventHeaders().forEach( (k,v) -> {
            sb.append(k);
            sb.append("\t\t\t = \t ");
            sb.append(v);
            sb.append("\n");
        });

        if (eslEvent.getEventBodyLines() != null && eslEvent.getEventBodyLines().size() > 0) {
            sb.append("Body Lines:\n");
            for (String line : eslEvent.getEventBodyLines()) {
                sb.append(line);
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
