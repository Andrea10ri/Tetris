package it.polimi.tetris.MODEL;

import it.polimi.tetris.MODEL.ENUMS.DurationTime;
import it.polimi.tetris.MODEL.ENUMS.EffectType;
import it.polimi.tetris.MODEL.ENUMS.GameStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.stream.Collectors;

/*Il contesto multiplayer. Gestisce: la lista di TetrisGame
(uno per giocatore), il timer globale della partita, la fase corrente (1/2/3)
e il suo avanzamento, il ranking/punteggi, e la distribuzione degli effetti —
ovvero quando un Effect viene triggerato, è Game che chiama apply(source, targets) passando gli avversari giusti.*/
public class Game {

    //attributes
    private int numOfPlayers; //number of players
    private ArrayList<Player> Players; //array list of players
    private int remainingTime; //timer
    private DurationTime durationTime; // time of the match
    private Player [] ranking; //ranking of the match
    private int gamePhase;
    private GameStatus status; // WAITING, RUNNING, ENDED


    //constructor

    public Game(int numOfPlayers, ArrayList<Player> players, DurationTime durationTime) {
        this.numOfPlayers = numOfPlayers;
        this.Players = players;
        this.durationTime = durationTime;
        this.gamePhase = 1;
        this.ranking = new Player[numOfPlayers];
        this.status= GameStatus.RUNNING;

        switch (durationTime) {
            case FIVE_MINUTES: this.remainingTime = 300; break;
            case TEN_MINUTES: this.remainingTime = 600; break;
            case THIRTY_MINUTES: this.remainingTime = 1800; break;
        }
    }



    //getter and setter
    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public ArrayList<Player> getPlayers() {
        return Players;
    }

    public void setPlayers(ArrayList<Player> players) {
        Players = players;
    }

    public DurationTime getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(DurationTime durationTime) {
        this.durationTime = durationTime;
    }

    public Player[] getRanking() {
        return ranking;
    }

    public void setRanking(Player[] ranking) {
        this.ranking = ranking;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getGamePhase() {
        return gamePhase;
    }

    public void setGamePhase(int gamePhase) {
        this.gamePhase = gamePhase;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }



    public void Start() {
        // imposta i callback per ogni giocatore
        for (Player player : Players) {
            TetrisMatch match = player.getTetrisMatch();

            match.setOnRowsCleared(cleared -> OnRowsCleared(player, cleared));
            match.setOnEffectTriggered(effect -> OnEffectTriggered(player, effect));
            match.setOnGameOver(m -> OnPlayerGameOver(player));

            // genera i primi tetromini
            match.GenerateNextTetronimo();
            match.SpawnNextTetronimo();
        }

        this.status = GameStatus.RUNNING;
    }



    private void OnRowsCleared(Player source, int cleared) {
        // fase 2: eliminare più di 2 righe aggiunge 1 riga a un avversario casuale
        if (gamePhase >= 2 && cleared > 2) {
            Player target = GetRandomOpponent(source);
            if (target != null)
                target.getTetrisMatch().getTetrisBoard().AddGarbageRow();
        }

        // fase 3: eliminare più di 2 righe aggiunge 2 righe a un avversario casuale
        if (gamePhase >= 3 && cleared > 2) {
            Player target = GetRandomOpponent(source);
            if (target != null)
                target.getTetrisMatch().getTetrisBoard().AddGarbageRow();
        }

        UpdateRanking();
    }

    private Player GetRandomOpponent(Player source) {
        List<Player> opponents = Players.stream()
                .filter(p -> p != source)
                .collect(Collectors.toList());
        if (opponents.isEmpty()) return null;
        return opponents.get(new Random().nextInt(opponents.size()));
    }

    private void UpdateRanking() {
        //ordina i giocatori per punteggio decrescente
        Players.sort((p1, p2) -> p2.getTetrisMatch().getScore() - p1.getTetrisMatch().getScore());
        // aggiorna il ranking
        ranking = Players.toArray(new Player[0]);
    }

    private void OnEffectTriggered(Player source, Effect effect) {
        // i bonus si applicano al giocatore stesso
        if (effect.getEffectType() == EffectType.BONUS) {
            effect.Apply(source.getTetrisMatch(), null);
        }
        // i malus si applicano agli avversari
        else {
            List<TetrisMatch> opponents = Players.stream()
                    .filter(p -> p != source)
                    .map(Player::getTetrisMatch)
                    .collect(Collectors.toList());

            effect.Apply(source.getTetrisMatch(), opponents);
        }
    }

    private void OnPlayerGameOver(Player player) {
        // aggiorna il ranking
        UpdateRanking();

        // controlla se tutti i giocatori sono in game over
        boolean allGameOver = Players.stream()
                .allMatch(p -> p.getTetrisMatch().getTetrisBoard().IsGameOver());

        if (allGameOver)
            EndGame();
    }

    public void TickTimer() {
        if (status != GameStatus.RUNNING) return;

        remainingTime--;

        // controlla se il tempo è scaduto
        if (remainingTime <= 0) {
            EndGame();
            return;
        }

        // gestione fasi in base alla durata
        switch (durationTime) {
            case FIVE_MINUTES:
                // una sola fase, niente da fare
                break;

            case TEN_MINUTES:
                // fase 2 al minuto 5 (300 secondi rimanenti)
                if (remainingTime == 300)
                    AdvancePhase();
                break;

            case THIRTY_MINUTES:
                // fase 2 al minuto 20 (600 secondi rimanenti)
                // fase 3 al minuto 10 (300 secondi rimanenti)
                if (remainingTime == 1200)
                    AdvancePhase();
                else if (remainingTime == 600)
                    AdvancePhase();
                break;
        }
    }

    private void AdvancePhase() {
        gamePhase++;
        // aumenta la velocità di caduta per tutti i giocatori
        for (Player player : Players)
            player.getTetrisMatch().setFallingVelocity(player.getTetrisMatch().getFallingVelocity() / 2);
    }

    private void EndGame() {
        this.status = GameStatus.ENDED;
        UpdateRanking();
    }


}
