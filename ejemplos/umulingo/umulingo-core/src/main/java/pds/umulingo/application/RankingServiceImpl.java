package pds.umulingo.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import pds.umulingo.domain.model.ranking.model.PosicionUsuario;
import pds.umulingo.domain.model.ranking.model.Ranking;
import pds.umulingo.domain.model.ranking.repository.RankingRepository;
import pds.umulingo.domain.model.usuario.model.Usuario;
import pds.umulingo.domain.model.usuario.repository.UsuarioRepository;
import pds.umulingo.domain.ports.input.RankingService;
import pds.umulingo.domain.ports.input.dto.RankingInfoDTO;

@Service
public class RankingServiceImpl implements RankingService {
	private static final Logger log = LoggerFactory.getLogger(RankingServiceImpl.class);
	
	private UsuarioRepository usuarioRepository;
	private RankingRepository rankingRepository;

	public RankingServiceImpl(UsuarioRepository usuarioRepository, RankingRepository rankingRepository) {
		this.rankingRepository = rankingRepository;
		this.usuarioRepository = usuarioRepository;
	}
	
	@Override
	public RankingInfoDTO rankingDeUsuario(String usuarioId) {
		throw new UnsupportedOperationException("No implementado todavía");
	}

	@Override
	public RankingInfoDTO rankingGlobal() {
		Ranking ranking = this.rankingRepository.getRanking();
		List<RankingInfoDTO.PosicionDTO> posiciones = new ArrayList<>();
		for (PosicionUsuario posicion: ranking.getPosicionesOrdenadas()) {
			Optional<Usuario> usuario = usuarioRepository.findById(posicion.getUsuarioId());
			if (usuario.isPresent()) {
				String nombreUsuario = usuario.get().getNombre();
				posiciones.add(new RankingInfoDTO.PosicionDTO(posicion.getUsuarioId().valor(), nombreUsuario, posicion.getRespuestasCorrectas()));
			} else {
				log.error("Usuario en ranking no encontrado: {}", posicion.getUsuarioId());
			}
		}
		
		return new RankingInfoDTO(posiciones);
	}
}
