package br.com.jdorigao.apidfe.controller;

import br.com.jdorigao.apidfe.entity.Empresa;
import br.com.jdorigao.apidfe.service.EmpresaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/empresa")
@Slf4j
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Empresa empresa) {
        try{
            empresaService.salvar(empresa);
            return ResponseEntity.ok(empresa);
        }catch (Exception e){
            log.error("Erro ao salvar Empresa", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try{
            return ResponseEntity.ok(empresaService.listaTudo());
        }catch (Exception e){
            log.error("Erro ao listar Empresas", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<?> listarPorId(@PathVariable("id") Long idEmpresa) {
        try{
            return ResponseEntity.ok(empresaService.listarPorId(idEmpresa));
        }catch (Exception e){
            log.error("Erro ao listar Empresa", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> deletar(@PathVariable("id") Long idEmpresa) {
        try{
            empresaService.deletar(idEmpresa);
            return ResponseEntity.ok("Empresa deletada com sucesso");
        }catch (Exception e){
            log.error("Erro ao deletar Empresa", e);
            return ResponseEntity.badRequest().body("Erro ao deletar Empresa: "+e.getMessage());
        }
    }
}
