package pl.lodz.p.edu.grs.model.game;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Embeddable
@Getter
@EqualsAndHashCode
public class Rate {

    @NotNull
    private Long userId;
    @Max(10)
    private Long rate;
    private String comment;

    protected Rate() {
    }

    public Rate(final Long userId, final Long rate) {
        this(userId, rate, null);
    }

    public Rate(final long userId, final long rate, final String comment) {
        this.userId = userId;
        this.rate = rate;
        this.comment = comment;
    }

}
