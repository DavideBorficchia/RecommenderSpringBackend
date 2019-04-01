package com.app.recommender.records;

import com.app.recommender.Model.PhysicalActivityRecord;
import com.app.recommender.Model.RecordsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class RecordController {

    @Autowired
    private RecordService recordService;

    @GetMapping(value = "/testrecords")
    public double test() {
        return 12.45;
    }

    @PostMapping(value = "/{userId}/activities/{physicalActivityId}")
    public ResponseEntity postRecord(@PathVariable(value = "userId") String userId,
                                     @PathVariable(value = "physicalActivityId") String physicalActivityId,
                                     @RequestBody PhysicalActivityRecord record) {
        record.setUserId(userId);
        record.setPhysicalActivityId(physicalActivityId);
        PhysicalActivityRecord physicalActivityRecord = this.recordService.createNewPhysicalActivityRecord(record);
        return ResponseEntity.status(HttpStatus.CREATED).body(physicalActivityRecord);
    }

    @GetMapping(value = "/{userId}/activities/{physicalActivityId}")
    public ResponseEntity getRecordsAmongDates(@PathVariable(value = "userId") String userId,
                                               @PathVariable(value = "physicalActivityId") String physicalActivityId,
                                               @RequestParam(value = "startDate") String startDate,
                                               @RequestParam(value = "endDate") String endDate) {
        List<PhysicalActivityRecord> records;
        try {

            records = this.recordService.getAllRecordsBetweenDates(userId, startDate, endDate, physicalActivityId);
        } catch (RecordsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(records);
    }
}