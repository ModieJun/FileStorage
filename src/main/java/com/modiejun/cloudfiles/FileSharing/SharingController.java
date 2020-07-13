package com.modiejun.cloudfiles.FileSharing;


import com.modiejun.cloudfiles.FileSharing.Model.SharedLink;
import com.modiejun.cloudfiles.FileSharing.Model.SharedLinkExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller(value = "sharingController")
@RequestMapping("/share")
public class SharingController {

    private final SharingService sharingService;
    private final String KEY="thisisnewkeythisisnewkey";

    @Autowired
    public SharingController(@Qualifier(value = "sharingService") SharingService service) {
        this.sharingService = service;
    }

    @GetMapping()
    public String displaySharedResource(@RequestParam("shareCode") String code,Model model) throws MissingServletRequestParameterException {
        //validate Code
        this.validateCodeExist(code);
        SharedLink sharedLink= sharingService.fetchFilePathForSharing(code);
        model.addAttribute("fileName",sharedLink.getFileToBeAccessed());
        model.addAttribute("downloadLink",sharedLink.getDownloadLink());
        return "viewShared";

    }

    @PostMapping("/generateLink")
    @PreAuthorize("hasAuthority('file:read')")
    public String generateShareLink(@RequestParam(name = "filePath") String filePath,@RequestParam(name = "fileName") String fileName, Principal principal, RedirectAttributes redirectAttributes,Model m) {
        String username=principal.getName();

        //Save code and stuff into the DB and then schedule a worker to invalidate the code
        String shareCode=sharingService.createSharableLink(username,fileName,filePath);
        String link = MvcUriComponentsBuilder.fromMethodName(SharingController.class,
                "displaySharedResource", shareCode,m).build().toUriString();
        redirectAttributes.addFlashAttribute("shareCode",link);
        return "redirect:/";
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public String invalidShareCode(MissingServletRequestParameterException exception,RedirectAttributes redirectAttributes){
        return exception.getMessage();
    }

    private void validateCodeExist(String code) throws MissingServletRequestParameterException {
        if (code.isEmpty()) throw new MissingServletRequestParameterException("shareCode","String type");
    }

    @ExceptionHandler(SharedLinkExistException.class)
    public String sharedLinkExistsHandler(SharedLinkExistException exception,RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message","Redirect Link already created Before: "+exception.getMessage());
        return "redirect:/";
    }
}
