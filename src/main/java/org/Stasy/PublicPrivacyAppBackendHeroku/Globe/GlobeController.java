package org.Stasy.PublicPrivacyAppBackendHeroku.Globe;

import org.Stasy.PublicPrivacyAppBackendHeroku.Globe.POJO.CoordinatesWithInfo;
import org.Stasy.PublicPrivacyAppBackendHeroku.Opinion.OpinionRepository.CustomOpinionRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GlobeController {

    private final CustomOpinionRepositoryImpl customOpinionRepositoryImpl;


    @Autowired
    public GlobeController(CustomOpinionRepositoryImpl customOpinionRepositoryImpl) {
        this.customOpinionRepositoryImpl = customOpinionRepositoryImpl;
    }

     @GetMapping("/coordinates-with-info")
     @CrossOrigin(origins = "http://localhost:3000")
     public List<CoordinatesWithInfo> getCoordinatesWithInfo() {
        System.out.println("this is it:"+customOpinionRepositoryImpl.fetchCoordinatesWithInfoForOpinions());
        return customOpinionRepositoryImpl.fetchCoordinatesWithInfoForOpinions();
    }

//    @GetMapping("/coordinates")
//    public ResponseEntity<List<CustomOpinionRepositoryImpl.Coordinates>> getOpinionCoordinates() {
//        List<CustomOpinionRepositoryImpl.Coordinates> coordinatesList = customOpinionRepositoryImpl.fetchCoordinatesForOpinions();
//        return ResponseEntity.ok(coordinatesList);
//    }
}


