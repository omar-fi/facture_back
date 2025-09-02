package org.example.stage_back.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DataSeedService {

    private final JdbcTemplate jdbcTemplate;

    public DataSeedService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void insertSampleData() {
        // TypeNavire
        jdbcTemplate.update(
                "INSERT IGNORE INTO type_navire (id, libelle) VALUES " +
                        "(1, 'Navires de commerce')," +
                        "(2, 'Navires pétroliers & gaziers')," +
                        "(3, 'Navires passagers & ferries (RO-PAX)')," +
                        "(4, 'Navires pétroliers & gaziers')," +
                        "(5, 'Navires spéciaux')"
        );

        // Port
        jdbcTemplate.update(
                "INSERT IGNORE INTO port (id, nom, ville, tauxrk, admin_id) VALUES " +
                        "(1, 'ANP-CASA', 'Casablanca', 0.03, NULL)," +
                        "(43, 'ANP-AGADIR', 'Agadir', 0.04, NULL)," +
                        "(44, 'ANP-TANGER', 'Tanger', 0.04, NULL)," +
                        "(45, 'ANP-KENITRA', 'Kenitra', 0.05, NULL)," +
                        "(46, 'ANP-MOHAMMEDIA', 'Mohammedia', 0.025, NULL)," +
                        "(47, 'ANP-JORF', 'Jorf Lasfar', 0.02, NULL)," +
                        "(48, 'ANP-NADOR', 'Nador', 0.03, NULL)," +
                        "(49, 'ANP-SAFI', 'Safi', 0.04, NULL)," +
                        "(50, 'ANP-LAAYOUNE', 'Laayoune', 0.05, NULL)," +
                        "(51, 'ANP SMIR', 'Tétouan', 0.025, NULL)," +
                        "(52, 'CHMAALA ANP', 'El Hoceima', 0.02, NULL)," +
                        "(53, 'ANP-DAKHLA', 'Dakhla', 0.03, NULL)," +
                        "(54, 'ALHOCEIMA', 'Al Hoceima', 0.04, NULL)," +
                        "(55, 'TANTAN', 'Tantan', 0.05, NULL)," +
                        "(56, 'ELJADIDA', 'El Jadida', 0.025, NULL)," +
                        "(57, 'TARFAYA', 'Tarfaya', 0.02, NULL)," +
                        "(58, 'ASILAH', 'Asilah', 0.03, NULL)," +
                        "(59, 'SIDI IFNI', 'Sidi Ifni', 0.04, NULL)," +
                        "(60, 'MDIQ', 'Mdiq', 0.05, NULL)," +
                        "(61, 'BOUJDOUR', 'Boujdour', 0.025, NULL)," +
                        "(62, 'ESSAOUIRA', 'Essaouira', 0.02, NULL)," +
                        "(63, 'JEBHA', 'Jebha', 0.03, NULL)," +
                        "(64, 'IMESSOUANE', 'Imessouane', 0.04, NULL)," +
                        "(65, 'KSAR SGHIR', 'Ksar Sghir', 0.05, NULL)," +
                        "(66, 'KABILA', 'Kabila', 0.025, NULL)"
        );

        // Navire
        jdbcTemplate.update(
                "INSERT IGNORE INTO navire (id, type_id, nom, num_lloyd) VALUES " +
                        "(1, 1, 'TANGER EXPRESS', 'IMO9450363')," +
                        "(2, 1, 'CASABLANCA STAR', 'IMO9512345')," +
                        "(3, 1, 'AGADIR TRADER', 'IMO9638527')," +
                        "(4, 2, 'JORF LASFAR BULK', 'IMO9724685')," +
                        "(5, 2, 'NADOR CARRIER', 'IMO9876543')," +
                        "(6, 3, 'ATLANTIC MAROC', 'IMO9517536')," +
                        "(7, 3, 'MEDITERRANEAN QUEEN', 'IMO9245781')," +
                        "(8, 1, 'MAERSK ALGECIRAS', 'IMO9709136')," +
                        "(9, 1, 'MSC TANGER', 'IMO9832467')," +
                        "(10, 4, 'FES RO-RO', 'IMO9234671')"
        );

        // Escale
        jdbcTemplate.update(
                "INSERT IGNORE INTO escale (id, navire_id, date_arrivee, date_depart, port_id) VALUES " +
                        "(3, 3, '2025-08-20 00:00:00', '2025-08-21 00:00:00', 50)," +
                        "(4, 5, '2025-08-25 00:00:00', '2025-08-28 00:00:00', 46)," +
                        "(201462236, 5, '2025-08-15 00:00:00', '2025-08-18 00:00:00', 46)," +
                        "(201462733, 3, '2025-08-10 00:00:00', '2025-08-12 00:00:00', 50)"
        );
    }
}



