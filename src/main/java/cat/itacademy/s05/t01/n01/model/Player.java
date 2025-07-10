package cat.itacademy.s05.t01.n01.model;

import jakarta.persistence.*;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY);
    private long id;

    private String name;

    private int totalWins;

    public Player(long id, String name, int totalWins) {
        this.id = id;
        this.name = name;
        this.totalWins = totalWins;
    }

    public Player(String name, int totalWins) {
        this.name = name;
        this.totalWins = totalWins;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }
}
