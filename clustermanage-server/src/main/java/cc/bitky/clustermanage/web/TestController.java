package cc.bitky.clustermanage.web;

import cc.bitky.clustermanage.tcp.server.netty.channelhandler.ServerChannelInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @Autowired
  ServerChannelInitializer serverChannelInitializer;

  @RequestMapping(value = "/lml", method = RequestMethod.GET)
  public String getStr() {

    return "lml";
  }
}
