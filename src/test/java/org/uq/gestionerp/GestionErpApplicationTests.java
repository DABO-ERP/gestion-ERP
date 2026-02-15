package org.uq.gestionerp;

import com.daboerp.gestion.GestionErpApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = GestionErpApplication.class)
@ActiveProfiles("test")
class GestionErpApplicationTests {

    @Test
    void contextLoads() {
    }

}
