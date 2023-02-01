package app.foot.controller;

import app.foot.controller.rest.Player;
import app.foot.controller.rest.mapper.PlayerRestMapper;
import app.foot.service.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.PutMapping;
=======
>>>>>>> origin/dev
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class PlayerController {
    private final PlayerRestMapper mapper;
    private final PlayerService service;

    @GetMapping("/players")
    public List<Player> getPlayers() {
        return service.getPlayers().stream()
                .map(mapper::toRest).toList();
    }

    @PostMapping("/players")
    public List<Player> addPlayers(@RequestBody List<Player> toCreate) {
        List<app.foot.model.Player> domain = toCreate.stream()
<<<<<<< HEAD
                .map(mapper::toDomain).toList();
        return service.createPlayers(domain).stream()
                .map(mapper::toRest).toList();
    }


    @PutMapping("/players")
    public List<Player> editPlayers(@RequestBody List<Player> players){
        return service.editPlayers(players).stream()
                .map(mapper::toRest)
                .toList();
    }
=======
                .map(mapper::toDomain)
                .collect(Collectors.toUnmodifiableList());
        return service.createPlayers(domain).stream()
                .map(mapper::toRest)
                .collect(Collectors.toUnmodifiableList());
    }

    //TODO: add PUT /players where you can modify the name and the guardian status of a player
    // Don't forget to add integration tests for this
>>>>>>> origin/dev
}
