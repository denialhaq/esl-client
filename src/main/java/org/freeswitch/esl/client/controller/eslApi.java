package org.freeswitch.esl.client.controller;

import org.freeswitch.esl.client.dptools.Execute;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.inbound.InboundConnectionFailure;
import org.freeswitch.esl.client.transport.message.EslMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetSocketAddress;

@RestController
//@RequestMapping(value = "/esl")
public class eslApi {
    @GetMapping("/demo")
    public String demo(){
        return "Hello";
    }
    @GetMapping("/esl")
    public String eslCommand() {
        final Client inboudClient = new Client();
        try {
            inboudClient.connect(new InetSocketAddress("192.168.1.104", 8021), "ClueCon", 10);
            EslMessage eslMessage = inboudClient.sendApiCommand("status", "");
            if (eslMessage.getBodyLines().size() > 0) {
                String message = "";
                for (int i=0; i<eslMessage.getBodyLines().size(); i++){
                    message += eslMessage.getBodyLines().get(i) + "<br>";
                }
                return message;
            }
        } catch (InboundConnectionFailure e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
