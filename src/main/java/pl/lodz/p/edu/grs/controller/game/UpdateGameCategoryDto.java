package pl.lodz.p.edu.grs.controller.game;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class UpdateGameCategoryDto {

    @NotNull
    private Long id;

    @NotNull
    private Long categoryId;
}
