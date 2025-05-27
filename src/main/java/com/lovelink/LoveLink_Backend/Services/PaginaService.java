package com.lovelink.LoveLink_Backend.Services;

import com.lovelink.LoveLink_Backend.Dto.PaginaRequestDto;
import com.lovelink.LoveLink_Backend.Dto.PaginaResponseDto;
import com.lovelink.LoveLink_Backend.Models.Pagina;
import com.lovelink.LoveLink_Backend.Repositorios.PaginaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaginaService {
    @Autowired
    PaginaRepository paginaRepository;

    @Autowired
    EmailService emailService;
    public Pagina registraNovaPagina(PaginaRequestDto pagina){
        Pagina novaPagina = new Pagina(pagina);
        BeanUtils.copyProperties(pagina, novaPagina);
        Pagina paginaRetorno = paginaRepository.save(novaPagina);
      //  if(paginaRetorno != null){
     //       emailService.enviarRegistroPagina(paginaRetorno);
      //  }
        return paginaRetorno;
    }

    public boolean deletaPagina(Pagina pagina){
        paginaRepository.delete(pagina);
        return true;
    }

    public Optional<Pagina> getPaginaByPagamentoId(Long pagamentoId){
        return paginaRepository.findByPagamentoId(pagamentoId);
    }

    public List<Pagina> getPaginasPorParceiro(UUID idParceiro){
        return paginaRepository.findAllByIdParceiro(idParceiro);
    }

    public Pagina salvaPagina(Pagina pagina){
        return this.paginaRepository.save(pagina);
    }

    public Optional<Pagina> getPagina(String slug, Long id){
        Optional<Pagina> opPagina = paginaRepository.findByIdAndSlug(id,slug);
        if(opPagina.isPresent()){
            Pagina encontrada = opPagina.get();
            if(encontrada.getPlanoSelecionado().equalsIgnoreCase("anual")){
                LocalDate dataExpiracao = encontrada.getDataCriacao().plusYears(1);
                if (LocalDate.now().isAfter(dataExpiracao) && !"expirado".equalsIgnoreCase(encontrada.getStatus())) {
                    encontrada.setStatus("expirado");
                    paginaRepository.save(encontrada);
                }
            }
            return  Optional.of(encontrada);
        }
        return  Optional.empty();
    }
}
