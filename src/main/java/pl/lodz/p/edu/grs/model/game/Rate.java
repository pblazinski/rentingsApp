package pl.lodz.p.edu.grs.model.game;

import lombok.Getter;

import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Embeddable
@Getter
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rate rate = (Rate) o;

        return userId != null ? userId.equals(rate.userId) : rate.userId == null;
    }

    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }
}
