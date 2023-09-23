package br.com.jcv.microservice.invoice.manager.controller.v1.sendinvoice;

import br.com.jcv.microservice.invoice.manager.analyser.IAnalyser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/invoice")
@Slf4j
public class SendInvoiceController {

    private @Autowired @Qualifier("analyserCpf") IAnalyser<String> analyserCpf;

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        log.info("getStatus :: Microservice invoice is UP.");
        return ResponseEntity.ok("Microservice invoice is UP");
    }

    @GetMapping(params = "cpf")
    public ResponseEntity sendInvoice(@RequestParam("cpf") String cpf) {
        analyserCpf.execute(cpf);
        return ResponseEntity.ok(cpf);
    }

}
