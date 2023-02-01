package app.foot.service;

import app.foot.exception.BadRequestException;
import app.foot.exception.NotFoundException;
import app.foot.model.Player;
import app.foot.repository.PlayerRepository;
import app.foot.repository.entity.PlayerEntity;
import app.foot.repository.mapper.PlayerMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PlayerService {
    private final PlayerRepository repository;
    private final PlayerMapper mapper;

    public List<Player> getPlayers() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    public List<Player> createPlayers(List<Player> toCreate) {
        List<PlayerEntity> entities = toCreate.stream().map(mapper::toEntity).toList();

        return repository.saveAll(entities).stream()
                .map(mapper::toDomain)
                .toList();
    }

    public List<Player> editPlayers(List<app.foot.controller.rest.Player> playersToEdit) {
        List<PlayerEntity> entities = playersToEdit.stream().map(player -> {
            if (player.getId() == null) {
                throw new BadRequestException("an Id must be provided");
            }

            Optional<PlayerEntity> optionalEntity = repository.findById(player.getId());

            if (optionalEntity.isEmpty()) {
                throw new NotFoundException("Player#" + player.getId() + " not found.");
            }

            PlayerEntity entity = optionalEntity.get();

            entity.setName(Objects.requireNonNullElse(player.getName(), entity.getName()));
            entity.setGuardian(Objects.requireNonNullElse(player.getIsGuardian(), entity.isGuardian()));

            return entity;
        }).toList();


        return repository.saveAll(entities).stream()
                .map(mapper::toDomain)
                .toList();
    }

    public List<Player> createPlayers(List<Player> toCreate) {
        return repository.saveAll(toCreate.stream()
                        .map(mapper::toEntity)
                        .collect(Collectors.toUnmodifiableList())).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toUnmodifiableList());
    }
}
