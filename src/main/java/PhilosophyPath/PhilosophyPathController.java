package PhilosophyPath;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

import static PhilosophyPath.PhilosophyPath.getPathToPhilosophy;

//Currently we have functionality for /GET and /POST and the ?title query parameter

@RestController
@CrossOrigin("*")
public class PhilosophyPathController {

    @Autowired
    PhilosophyPathRepository philosophyPathRepository;

    @RequestMapping("/philosophypath")
    public PhilosophyPath queryPhilosophyPath(@RequestParam(value = "title", defaultValue = "Galileo_Galilei") String title) {

        if(getPhilosophyPathById(title).getBody() == null){
            List<String> pathToPhilosophy = new ArrayList<>();

            pathToPhilosophy.add(title);

            List<String> completePathToPhilosophy = getPathToPhilosophy(pathToPhilosophy);

            String pathStarter = completePathToPhilosophy.get(0);

            PhilosophyPath path = new PhilosophyPath(pathStarter, completePathToPhilosophy.toString());

            this.createPath(path);

            return new PhilosophyPath(pathStarter, completePathToPhilosophy.toString());
        } else {
            return getPhilosophyPathById(title).getBody();
        }

    }

    @GetMapping(value="/philosophypath/{id}")
    public ResponseEntity<PhilosophyPath> getPhilosophyPathById(@PathVariable("id") String id) {

        PhilosophyPath path = philosophyPathRepository.findOne(id);

        if(path == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(path, HttpStatus.OK);
        }
    }

    @PostMapping("/philosophypath")
    public PhilosophyPath createPath(@Valid @RequestBody PhilosophyPath philosophyPath) {

        philosophyPath.setCompleted(false);

        return philosophyPathRepository.save(philosophyPath);
    }

}
