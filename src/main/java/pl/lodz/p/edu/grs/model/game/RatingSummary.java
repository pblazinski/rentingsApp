package pl.lodz.p.edu.grs.model.game;

import lombok.Getter;
import pl.lodz.p.edu.grs.exceptions.RateAddException;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Embeddable
@Getter
public class RatingSummary {

    @Valid
    @ElementCollection(fetch = FetchType.EAGER, targetClass = Rate.class)
    private Set<Rate> rates = new HashSet<>();

    private double average;

    public void addRate(final Rate rate) {
        if (this.rates.contains(rate)) {
            throw new RateAddException("User can add only one rate for one game");
        }

        this.rates.add(rate);
        this.updateAverage();
    }

    private void updateAverage() {
        if (this.rates.size() > 0) {
            long sum = this.rates.stream().mapToLong(Rate::getRate).sum();
            this.average = sum / (this.rates.size() * 1.0);
        }
    }
}
