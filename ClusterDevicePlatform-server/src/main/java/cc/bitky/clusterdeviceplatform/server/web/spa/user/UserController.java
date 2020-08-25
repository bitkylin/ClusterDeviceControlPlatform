package cc.bitky.clusterdeviceplatform.server.web.spa.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cc.bitky.clusterdeviceplatform.server.utils.ResMsg;
import cc.bitky.clusterdeviceplatform.server.pojo.user.Token;
import cc.bitky.clusterdeviceplatform.server.pojo.user.UserInfo;
import cc.bitky.clusterdeviceplatform.server.pojo.user.UserLogin;


@CrossOrigin
@RestController
@RequestMapping(value = "/server/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping(path = "/login", consumes = "application/json")
    public ResMsg userLogin(@RequestBody UserLogin userLogin) {
        ResMsg resMsg = new ResMsg(new Token());
        logger.info("userLogin");
        return resMsg;
    }

    @PostMapping("/logout")
    public ResMsg userLogout() {
        logger.info("userLogout");
        return new ResMsg("success");
    }

    @GetMapping("/info")
    public ResMsg userInfo(@RequestParam String token) {
        ResMsg resMsg = new ResMsg(new UserInfo());
        logger.info("userInfo");
        return resMsg;
    }

    @GetMapping("/test")
    public String test() {
        return "success";
    }
}
