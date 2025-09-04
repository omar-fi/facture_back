package org.example.stage_back.dto;



import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class FactureRequestDTO {
    private Date dateEmissionFact;
    private Date dateReglementFact;
    private Long manifesteId;
    private Long escaleId;

    private List<DetailDTO> details;

    @Data
    public static class DetailDTO {
        private Integer categorieId;
        private Integer numeroLigneFact;
        private BigDecimal montantHT;
        private BigDecimal montantTVA;
        private BigDecimal montantTR;
        private BigDecimal montantTTC;
    }
}
