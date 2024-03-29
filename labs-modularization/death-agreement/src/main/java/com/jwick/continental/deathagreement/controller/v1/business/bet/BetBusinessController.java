package com.jwick.continental.deathagreement.controller.v1.business.bet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/continental/bet")
public class BetBusinessController {

    @Autowired private CreateBetService createBetService;
    @Autowired private ConfirmBetBusinessService confirmBetBusinessService;
    @Autowired private ViewJackpotBusinessService viewJackpotBusinessService;
    @Autowired private DeletePendingBetBusinessService pendingBetBusinessService;
    @Autowired private DeleteTargetWithNoBetBusinessService deleteTargetWithNoBetBusinessService;
    @Autowired private DeleteDueDeathDateBetBusinessService deleteDueDeathDateBetBusinessService;

    @PostMapping
    public ResponseEntity makeBet(@RequestBody @Valid BetRequest betRequest) {
        final UUID pid = UUID.randomUUID();
        return ResponseEntity.ok().body(createBetService.execute(pid, betRequest));
    }

    @GetMapping("/confirm/{ticket}")
    public ResponseEntity confirmBet(@PathVariable @Valid UUID ticket) {
        final UUID pid = UUID.randomUUID();
        return ResponseEntity.ok().body(confirmBetBusinessService.execute(pid, ticket));
    }
    @GetMapping("/view/{target}")
    public ResponseEntity viewJackpot(@PathVariable(name = "target") @Valid UUID target) {
        final UUID pid = UUID.randomUUID();
        return ResponseEntity.ok().body(viewJackpotBusinessService.execute(pid, target));
    }
    @DeleteMapping
    public ResponseEntity purgePendingBets() {
        final UUID pid = UUID.randomUUID();
        return ResponseEntity.ok().body(pendingBetBusinessService.execute(pid, null));
    }
    @DeleteMapping("/object")
    public ResponseEntity deleteObjectTarget() {
        final UUID pid = UUID.randomUUID();
        return ResponseEntity.ok().body(deleteTargetWithNoBetBusinessService.execute(pid, null));
    }
    @DeleteMapping("/dueDeathDateBet")
    public ResponseEntity deleteDueDeathDateBet() {
        final UUID pid = UUID.randomUUID();
        return ResponseEntity.ok().body(deleteDueDeathDateBetBusinessService.execute(pid, null));
    }
}
