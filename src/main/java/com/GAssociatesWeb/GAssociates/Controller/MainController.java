package com.GAssociatesWeb.GAssociates.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class MainController {
    @GetMapping
    public String showCommonPage() {
        return "3GCommon";
    }

    @GetMapping("specialNotice/{wardNo}")
    public String showSpecialNotice(@PathVariable("wardNo") String propertyId, Model model) {
        model.addAttribute("wardNo", propertyId);
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

    @GetMapping("secondaryBatchAssessmentReport")
    public String showSecondaryBatchAssessmentReport(){return "3GSecondaryBatchAssessmentReport";}

    @GetMapping("objectionReciept")
    public String showObjectionReciept(){return "3GViewObjectionReciept";}

    @GetMapping("taxBill/{wardNo}")
    public String showTaxBill(@PathVariable("wardNo") String propertyId, Model model) {
        model.addAttribute("wardNo", propertyId);
        return "3GViewTaxBill";
    }

    @GetMapping("taxBill")
    public String showSingleTaxBill() {
        // Renders the same template; JS will detect ?newPropertyNo and fetch single bill
        return "3GViewTaxBill";
    }

    @GetMapping("rtcc/{pdNewpropertyno}")
    public String showRtcc(@PathVariable("pdNewpropertyno") String newPropertyNumber, Model model) {
        model.addAttribute("newPropertyNumber", newPropertyNumber);
        return "3GRealtimeCC"; // Thymeleaf template or just HTML
    }
}
