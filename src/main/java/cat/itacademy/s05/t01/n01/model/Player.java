    package cat.itacademy.s05.t01.n01.model;

    import org.springframework.data.annotation.Id;
    import org.springframework.data.relational.core.mapping.Column;
    import org.springframework.data.relational.core.mapping.Table;
    import lombok.*;

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Table("players")
    public class Player {

        @Id
        private Long id;

        @Column("name")
        private String name;

        @Builder.Default
        @Column("total_wins")
        private int totalWins = 0;

    }