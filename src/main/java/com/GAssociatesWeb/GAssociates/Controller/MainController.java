package com.GAssociatesWeb.GAssociates.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class MainController {
    @GetMapping
    public String showCommonPage() {
        return "3GCommon";
    }

    @GetMapping("specialNotice")
    public String showSpecialNotice() {
        return "3GViewSpecialNotice";
    }

    @GetMapping("orderSheet")
    public String showSpecial() {
        return "3GViewOrderSheet";
    }

    @GetMapping("citizenLogin")
    public String showCitizenLogin(){return "3GCitizenPage";}

    @GetMapping("hearingNotice")
    public String showHearingNotice(){return "3GViewHearingNotice";}

    @GetMapping("objectionReciept")
    public String showObjectionReciept(){return "3GViewObjectionReciept";}
}
