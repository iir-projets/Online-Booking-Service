package org.example.myspringapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PDFContentDTO {
    String ServiceName;
    Double ServicePrice;
}
