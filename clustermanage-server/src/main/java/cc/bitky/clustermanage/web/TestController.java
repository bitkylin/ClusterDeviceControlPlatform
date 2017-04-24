package cc.bitky.clustermanage.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import cc.bitky.clustermanage.server.bean.KyServerCenterHandler;

@RestController
public class TestController {

    private final KyServerCenterHandler kyServerCenterHandler;

    @Autowired
    public TestController(KyServerCenterHandler kyServerCenterHandler) {
        super();
        this.kyServerCenterHandler = kyServerCenterHandler;
    }

//    @RequestMapping(value = "/lml", method = RequestMethod.GET)
//    public String getStr() {
//
//        long[] longs = new long[16];
//        longs[0] = 72340172838076673L;
//        longs[1] = 217020518514230019L;
//        longs[2] = 506381209866536711L;
//        longs[3] = 1663823975275763479L;
//        longs[4] = 3978709506094217015L;
//        longs[5] = 72624976668147840L;
//
//        WebMsgDeployFreeCardNumber webMsgDeployFreeCardNumber = new WebMsgDeployFreeCardNumber(0x01, 0x02, longs);
//        List<IMessage> messages = new ArrayList<>();
//        messages.add(webMsgDeployFreeCardNumber);
//        kyServerCenterHandler.sendMsgToTcp(messages);
//
//        return "lml";
//    }
}
