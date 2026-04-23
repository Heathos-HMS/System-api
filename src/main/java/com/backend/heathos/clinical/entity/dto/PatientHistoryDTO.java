package com.backend.heathos.clinical.entity.dto;

import com.backend.heathos.clinical.entity.Vitals;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PatientHistoryDTO {

    // Last 10 clinical notes, each with prescriptions and lab orders inside
    private List<NoteWithDetailsDTO> notes;

    // Last 5 vitals readings
    private List<Vitals> recentVitals;
}
