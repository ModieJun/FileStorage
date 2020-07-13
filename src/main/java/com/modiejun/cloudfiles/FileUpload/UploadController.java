package com.modiejun.cloudfiles.FileUpload;

import com.modiejun.cloudfiles.FileSharing.SharingService;
import com.modiejun.cloudfiles.FileUpload.POJO.FileResponse;
import com.modiejun.cloudfiles.FileUpload.POJO.FolderResponse;
import com.modiejun.cloudfiles.FileUpload.POJO.SavedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@Scope("session")
public class UploadController {
    private final StorageService storageService;

    private final FileObjService fileObjService;

    private final SharingService sharingService;

    @Autowired
    public UploadController(StorageService storageService, FileObjService fileObjService, SharingService sharingService) {
        this.storageService = storageService;
        this.fileObjService = fileObjService;
        this.sharingService = sharingService;
    }

    @RequestMapping("/")
    public String home(Model model, HttpSession httpSession, RedirectAttributes redirectAttributes) {

        String directory = "upload-dir";
        if (httpSession.getAttribute("currentFolderPath") == null) {
            httpSession.setAttribute("currentFolderPath", "upload-dir");
        } else {
            directory = httpSession.getAttribute("currentFolderPath").toString();
        }

        // ADD back link to move back to parent folder
        String parentDirectory = storageService.getParentFolderLink(directory);
        if (parentDirectory != null)
            model.addAttribute("parentFolder", MvcUriComponentsBuilder.fromMethodName(UploadController.class,
                    "moveOutDirectory", parentDirectory, httpSession, redirectAttributes).build().toUriString());

        // Add Folder links into model and view
        model.addAttribute("folders", storageService.loadAllSubdirectories(directory)
                .map(path ->
                        //creates Folder objects for response
                        new FolderResponse(path.getFileName().toString(),
                                MvcUriComponentsBuilder.fromMethodName(UploadController.class,
                                        "moveIntoDirectory", path.getFileName().toString(), httpSession, redirectAttributes).build().toUriString(),
                                MvcUriComponentsBuilder.fromMethodName(UploadController.class,
                                        "deleteFolder", path.getFileName().toString(), redirectAttributes).build().toUriString()
                        )
                ).collect(Collectors.toList()));

        //Add Files into model
        model.addAttribute("files", storageService.loadAllFilesInDirectory(directory).map(e ->
                new FileResponse(
                        //Download link
                        MvcUriComponentsBuilder.fromMethodName(UploadController.class,
                                "serveFile", e.getFileName().toString()).build().toUri().toString()
                        //File name
                        , e.getFileName().toString(),
                        // Delete Link
                        MvcUriComponentsBuilder.fromMethodName(UploadController.class,
                                "deleteFile", e.getFileName().toString(), redirectAttributes).build().toUriString()
                )).collect(Collectors.toList())
        );

        return "index";
    }

    /*
        Download the files as a resource, passed in through response body
     */
    @PreAuthorize("hasAuthority('file:download')")
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    /**
     * Link to delete the file, used to create the link for the linkage for deletion
     */
    @PreAuthorize("hasAuthority('file:delete')")
    @GetMapping("/delete/{filename:.+}")
    public String deleteFile(@PathVariable String filename, RedirectAttributes redirectAttributes) {
        sharingService.inValidateLink(filename); //invalidate the link if shared
        storageService.delete(filename); //delete the file from storage
        redirectAttributes.addFlashAttribute("message", "Successfully deleted : " + filename);
        return "redirect:/";
    }

    @PreAuthorize("hasAuthority('file:delete')")
    @GetMapping("/deletefolder/{foldername:.+}")
    public String deleteFolder(@PathVariable String foldername, RedirectAttributes redirectAttributes) {
        storageService.deleteDirectory(foldername);
        redirectAttributes.addFlashAttribute("message", "Successfully deleted folder : " + foldername);
        return "redirect:/";
    }

    /*
        Post Mapping for Multi File Upload
     */
    @PreAuthorize("hasAuthority('file:write')")
    @PostMapping("/fileUpload")
    public String handleMultiFileUpload(@RequestParam("files") MultipartFile[] files, HttpSession session, RedirectAttributes redirectAttributes) {
        //get directory for saving file
        String currentDirectory = session.getAttribute("currentFolderPath").toString();

        //Call the storage service to save files
        System.out.println(files.length);
        storageService.store(currentDirectory, files);
        //send to storage service for saving
        redirectAttributes.addFlashAttribute("message", "File(s) Uploaded and saved!");

        return "redirect:/";
    }


    /*
        Navigation link to move into the folder
     */
    @GetMapping("/moveInto/{directoryName:.+}")
    public String moveIntoDirectory(@PathVariable("directoryName") String directory,
                                    HttpSession httpSession, RedirectAttributes redirectAttributes) {
        String currentDirectory = httpSession.getAttribute("currentFolderPath").toString();
        httpSession.setAttribute("currentFolderPath", currentDirectory + File.separator + directory);

        redirectAttributes.addFlashAttribute("message",
                "Moves into subdirectory: " + directory);
        return "redirect:/";
    }

    /*
    Navigation link to move out of the folder -  GO back Up a folder
     */
    @GetMapping("/moveOut/{directoryName:.+}")
    public String moveOutDirectory(@PathVariable("directoryName") String directory, HttpSession httpSession, RedirectAttributes redirectAttributes) {
        String currentDirectory = httpSession.getAttribute("currentFolderPath").toString();
        httpSession.setAttribute("currentFolderPath", currentDirectory.substring(0, currentDirectory.lastIndexOf(File.separator)));

        redirectAttributes.addFlashAttribute("message",
                "Moved up into parent directory: " + directory);
        return "redirect:/";
    }

    /*
    Create a new directory in the current folder
     */
    @PreAuthorize("hasAuthority('file:write')")
    @PostMapping("/newDirectory")
    public String createNewDirectory(@RequestParam("folderName") String folderName,
                                     HttpSession httpSession,
                                     RedirectAttributes redirectAttributes) {
        String currentDirectory = httpSession.getAttribute("currentFolderPath").toString();

        //create New directory from the request
        storageService.createNewDirectory(currentDirectory + File.separator + folderName);

        redirectAttributes.addFlashAttribute("message",
                "New directory created");

        return "redirect:/";
    }

    @PreAuthorize("hasAuthority('file:read')")
    @GetMapping("/searchFile")
    public String searchFile(@RequestParam(value = "fileNameSearch") String filename, Model model) {
        assert filename != null;
        Optional<List<SavedFile>> result = fileObjService.searchByFileName(filename);
        result.ifPresent(savedFiles -> {
            model.addAttribute("files",
                    savedFiles.stream().map(file -> {
                        return new FileResponse(MvcUriComponentsBuilder.fromMethodName(UploadController.class,
                                "serveFile", file.getFileName()).build().toUri().toString()
                                , file.getFileName() //File name
                                ,null);
                    }).collect(Collectors.toList()));
            model.addAttribute("message", savedFiles.size() + " Files Found");
        });
        return "search";
    }

    //    Exception Handler when throwable found
    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException exception ,RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "You do not have permission to perform this Request");
        return "redirect:/";
    }

}
